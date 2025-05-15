package com.kantares.store.cart;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {

        Cart cart = cartService.createCart();
        URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartMapper.toDto(cart));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable(name = "cartId") UUID cartId) {
        Cart cart = cartService.getCart(cartId);

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable(name = "cartId") UUID cartId,
            @RequestBody AddItemToCartRequest request) {
        logger.debug("Adding item to cart: cartId = {}, request = {}", cartId, request);

        CartItem cartItem = cartService.addToCart(cartId, request.getProductId());
        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        CartItem item = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartMapper.toDto(item));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId) {
        logger.debug("Deleting item from cart: cartId = {}, productId = {}", cartId, productId);

        cartService.deleteItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable(name = "cartId") UUID cartId) {

        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
