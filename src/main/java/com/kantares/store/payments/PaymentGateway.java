package com.kantares.store.payments;

import com.kantares.store.order.Order;

import java.util.Optional;

public interface PaymentGateway {
    public CheckoutSession createCheckoutSession(Order order);

    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
