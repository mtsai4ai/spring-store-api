package com.kantares.store.cart;

import com.kantares.store.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
     CartDto toDto(Cart cart);

    CartItemDto toDto(CartItem cartItem);

    CartProductDto toDto(Product product);
}
