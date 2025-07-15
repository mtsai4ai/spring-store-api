package com.kantares.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;
}
