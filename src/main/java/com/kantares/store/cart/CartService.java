package com.kantares.store.cart;

import com.kantares.store.product.Product;
import com.kantares.store.product.ProductNotFoundException;
import com.kantares.store.order.OrderMapper;
import com.kantares.store.order.OrderRepository;
import com.kantares.store.product.ProductRepository;
import com.kantares.store.user.User;
import com.kantares.store.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public Cart createCart() {
        Cart cart = new Cart();
        cartRepository.save(cart);

        return cart;
    }

    public Cart getCart(UUID cartId) {
        return cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
    }

    public CartItem addToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        CartItem item = cart.addItem(product);

        cartRepository.save(cart);
        return item;
    }

    public CartItem updateCartItem(UUID cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        CartItem item = cart.updateItem(productId, quantity);
        cartRepository.save(cart);
        return item;
    }

    public void deleteItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        Cart cart = cartRepository
                .getCartWithItems(cartId)
                .orElseThrow(CartNotFoundException::new);

        cart.clear();
        cartRepository.save(cart);
    }
}
