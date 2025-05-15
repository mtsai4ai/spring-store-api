package com.kantares.store.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Product ID cannot be null")
    @Min(value = 1, message = "Product ID must be greater than 0")
    @Max(value = 100, message = "Product ID must be less than 100")
    private Integer quantity;
}
