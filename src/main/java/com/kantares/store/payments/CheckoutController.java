package com.kantares.store.payments;

import com.kantares.store.order.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        logger.debug("***** Checkout request: {}", checkoutRequest);

        CheckoutResponse response = checkoutService.checkout(checkoutRequest);
        logger.debug("***** Checkout response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
//        logger.debug("====>>>> Received Webhook -> headers: {}", headers);
//        logger.debug("====>>>> Received Webhook -> payload: {}", payload);
        checkoutService.handleWebhookRequest(new WebhookRequest(headers, payload));
    }

    @GetMapping("/redirect")
    public void redirect(HttpServletResponse response) {
        try {
            response.sendRedirect("http://localhost:8010/checkout/success");
        } catch (IOException e) {
            logger.error("Error redirecting to success page", e);
        }
    }

    @RequestMapping("/success")
    public ResponseEntity<OrderStatusResponse> success(
            HttpServletRequest request,
            @RequestParam(name = "orderId", required = false) Long orderId) {
        logger.debug("***** Checkout success: method = {}", request.getMethod());
        logger.info("Payment successful for order ID: {}", orderId);
        return ResponseEntity.ok(new OrderStatusResponse(orderId, OrderStatus.PAID));
    }

    @RequestMapping("/cancel")
    public ResponseEntity<OrderStatusResponse> cancel(
            HttpServletRequest request,
            @RequestParam(name = "orderId", required = false) Long orderId) {
        logger.debug("***** Payment canceled: method = {}", request.getMethod());
        logger.info("Payment canceled for order ID: {}", orderId);
        return ResponseEntity.ok(new OrderStatusResponse(orderId, OrderStatus.CANCELED));
    }
}
