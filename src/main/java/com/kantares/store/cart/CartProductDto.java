package com.kantares.store.cart;

import lombok.Data;

@Data
public class CartProductDto {
    private Long id;
    private String name;
    private Double price;
}
