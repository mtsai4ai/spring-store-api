package com.kantares.store.common;

import com.kantares.store.cart.CartNotFoundException;
import com.kantares.store.order.OrderNotFoundException;
import com.kantares.store.payments.PaymentException;
import com.kantares.store.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadable() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Invalid request body"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound(CartNotFoundException ex) {
        String error = ex.getMessage();
        error = error != null ? error : "Cart not found";
         return ResponseEntity.badRequest().body(new ErrorDto(error));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(ProductNotFoundException ex) {
        String error = ex.getMessage();
        error = error != null ? error : "Product not found";
        return ResponseEntity.status(404).body(new ErrorDto(error));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFound(OrderNotFoundException ex) {
        String error = ex.getMessage();
        error = error != null ? error : "Order not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(error));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
        String error = ex.getMessage();
        error = error != null ? error : "Access denied";
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(error));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(PaymentException ex) {
        String error = ex.getMessage();
        error = error != null ? error : "Payment failed";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(error));
    }
}
