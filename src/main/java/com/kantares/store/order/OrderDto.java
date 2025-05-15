package com.kantares.store.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private Set<OrderItemDto> items;
    private BigDecimal totalPrice;
}
