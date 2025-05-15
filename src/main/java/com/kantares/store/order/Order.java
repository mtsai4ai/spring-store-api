package com.kantares.store.order;

import com.kantares.store.cart.Cart;
import com.kantares.store.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<OrderItem> items = new LinkedHashSet<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        items.add(orderItem);
    }

    public boolean isPlacedBy(User user) {
        return this.customer != null && this.customer.equals(user);
    }

    public static Order fromCart(Cart cart, User customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);

        cart.getItems()
                .stream()
                .map(OrderItem::fromCartItem)
                .forEach(order::addOrderItem);
        BigDecimal totalPrice = order.getItems()
                .stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        return order;
    }
}