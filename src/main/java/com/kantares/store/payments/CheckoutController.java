package com.kantares.store.payments;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(response);

    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
       logger.debug("====>>>> headers: {}", headers);
        checkoutService.handleWebhookRequest(new WebhookRequest(headers, payload));
    }
}
