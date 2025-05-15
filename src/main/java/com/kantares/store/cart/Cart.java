package com.kantares.store.cart;

import com.kantares.store.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(Product product) {
        CartItem cartItem = getItem(product.getId());
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(this);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            items.add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        return cartItem;
    }

    public CartItem updateItem(Long productId, Integer quantity) {
        CartItem cartItem = getItem(productId);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
        }

        return cartItem;
    }

    public CartItem removeItem(Long productId) {
        CartItem cartItem = getItem(productId);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }

        return cartItem;
    }

    public void clear() {
        for (CartItem item : items) {
            item.setCart(null);
        }
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}