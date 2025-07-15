package com.kantares.store.payments;

import com.kantares.store.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusResponse {
    private Long orderId;
    private OrderStatus orderStatus;
}
