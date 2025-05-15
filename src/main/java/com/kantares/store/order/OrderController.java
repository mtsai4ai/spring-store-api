package com.kantares.store.order;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/all")
    public List<OrderDto> geAllOrders() {
        System.out.println("geAllOrders: Fetching orders...");
        List<Order> orders = orderService.getUserOrders();
        return orderMapper.toDto(orders);
    }

    @GetMapping
    public List<OrderDto> getUserOrders() {
        System.out.println("getUserOrders: Fetching orders...");
        List<Order> order = orderService.getUserOrders();
        return orderMapper.toDto(order);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId) {
        System.out.println("Fetching order with ID: " + orderId);
        Order order = orderService.getOrderById(orderId);
        return orderMapper.toOrderDto(order);
    }
}
