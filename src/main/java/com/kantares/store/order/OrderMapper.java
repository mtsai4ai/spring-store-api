package com.kantares.store.order;

import com.kantares.store.payments.CheckoutResponse;
import com.kantares.store.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "orderId")
    CheckoutResponse toCheckoutResponse(Order order);

    OrderDto toOrderDto(Order order);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    OrderProductDto toOrderProductDto(Product product);

    List<OrderDto> toDto(List<Order> orders);
}
