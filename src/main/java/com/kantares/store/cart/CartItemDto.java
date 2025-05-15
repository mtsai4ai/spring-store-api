package com.kantares.store.cart;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartItemDto implements Serializable {
    CartProductDto product;
    Integer quantity;
    private BigDecimal totalPrice;
}