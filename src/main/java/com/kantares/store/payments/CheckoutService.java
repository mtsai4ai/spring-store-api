package com.kantares.store.payments;

import com.kantares.store.cart.Cart;
import com.kantares.store.order.Order;
import com.kantares.store.order.OrderStatus;
import com.kantares.store.cart.CartNotFoundException;
import com.kantares.store.order.OrderRepository;
import com.kantares.store.cart.CartService;
import com.kantares.store.order.OrderService;
import com.kantares.store.user.User;
import com.kantares.store.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final PaymentGateway paymentGateway;
    private final UserService userService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        Cart cart = cartService.getCart(checkoutRequest.getCartId());
        if (cart.isEmpty())
            throw new CartNotFoundException("Cart is empty");

        User customer = userService.getCurrentUser();
        Order order = Order.fromCart(cart, customer);
        orderRepository.save(order);

        try {
            CheckoutSession session = paymentGateway.createCheckoutSession(order);
            String url = session.getCheckoutUrl();
            System.out.println("===========> Checkout URL: " + url);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), url);
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handleWebhookRequest(WebhookRequest request) {
        paymentGateway
            .parseWebhookRequest(request)
            .ifPresent(paymentResult -> {
                Long orderId = paymentResult.getOrderId();
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new PaymentException("Order not found"));

                switch (paymentResult.getPaymentStatus()) {
                    case PAID
                        -> orderService.updateOrderStatus(orderId, OrderStatus.PAID);
                    case CANCELED
                        -> orderService.updateOrderStatus(orderId, OrderStatus.CANCELED);
                    case FAILED
                        -> orderService.updateOrderStatus(orderId, OrderStatus.FAILED);
                    default
                        -> throw new PaymentException("Unknown payment status");
                }
            });
    }
}