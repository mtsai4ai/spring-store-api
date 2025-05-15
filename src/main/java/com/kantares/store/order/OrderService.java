package com.kantares.store.order;

import com.kantares.store.cart.CartService;
import com.kantares.store.user.User;
import com.kantares.store.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final UserService userService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        User currentUser = userService.getCurrentUser();
        if (!order.isPlacedBy(currentUser))
            throw new AccessDeniedException("You don't have permission to access this order");
        return order;
    }

    public List<Order> getUserOrders() {
        User customer = userService.getCurrentUser();
        return orderRepository.findByCustomer(customer);
    }

    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }
}
