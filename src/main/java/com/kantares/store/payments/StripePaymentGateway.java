package com.kantares.store.payments;

import com.kantares.store.order.Order;
import com.kantares.store.order.OrderItem;
import com.kantares.store.product.Product;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import static com.stripe.param.checkout.SessionCreateParams.LineItem;
import static com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import static com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {
    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGateway.class);
    public static final String STRIPE_SIGNATURE_HEADER = "stripe-signature";

    @Value("${website.url}")
    private String websiteUrl;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            Session session = getPaymentSession(order);
            return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            logger.error("Error creating Stripe checkout session", e);
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            String payload = request.getPayload();
            String signature = request.getHeaders().get(STRIPE_SIGNATURE_HEADER);
            Event event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);

            return switch (event.getType()) {
                case "payment_intent.succeeded"
                    -> Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
                case "payment_intent.canceled"
                    -> Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.CANCELED));
                case "payment_intent.payment_failed"
                    -> Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
                default
                    -> Optional.empty();
            };
        }
        catch (SignatureVerificationException e) {
            logger.error("Stripe webhook signature verification failed", e);
            throw new PaymentException("Invalid signature");
        }
    }

    private static Long extractOrderId(Event event) {
        StripeObject stripeObject = event.getDataObjectDeserializer().getObject()
            .orElseThrow(() -> new PaymentException("Invalid Stripe event"));
        PaymentIntent paymentIntent = (PaymentIntent)stripeObject;
        logger.debug("PaymentIntent ID: {}", paymentIntent.getId());
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private Session getPaymentSession(Order order) throws StripeException {
        SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(websiteUrl + "/checkout-cancel")
                .putMetadata("order_id", String.valueOf(order.getId()));

        order.getItems()
                .stream()
                .map(StripePaymentGateway::buildLineItem)
                .forEach(builder::addLineItem);
        SessionCreateParams sessionParams = builder.build();

        return Session.create(sessionParams);
    }

    public static LineItem buildLineItem(OrderItem orderItem) {
        return LineItem.builder()
                .setPriceData(buildPriceData(orderItem))
                .setQuantity(Long.valueOf(orderItem.getQuantity()))
                .build();
    }

    public static PriceData buildPriceData(OrderItem orderItem) {
        return PriceData.builder()
                .setCurrency("usd")
                .setProductData(buildProductData(orderItem.getProduct()))
                .setUnitAmountDecimal(BigDecimal.valueOf(100).multiply(orderItem.getUnitPrice()))
                .build();
    }

    public static ProductData buildProductData(Product product) {
        return ProductData.builder()
                .setName(product.getName())
                .setDescription(product.getDescription())
                .build();
    }
}
