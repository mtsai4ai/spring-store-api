package com.kantares.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;
}
