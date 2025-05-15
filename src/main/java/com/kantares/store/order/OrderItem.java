package com.kantares.store.order;

import com.kantares.store.cart.CartItem;
import com.kantares.store.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem(Product product, BigDecimal price, Integer quantity) {
        this.product = product;
        this.unitPrice = price;
        this.quantity = quantity;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem fromCartItem(CartItem cartItem) {
        return new OrderItem(
            cartItem.getProduct(),
            cartItem.getProduct().getPrice(),
            cartItem.getQuantity()
        );
    }
}