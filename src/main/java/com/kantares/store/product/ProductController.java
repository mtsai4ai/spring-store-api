package com.kantares.store.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("/products")
    public List<ProductDto> getProducts(@RequestParam(name = "categoryId", required = false) Byte categoryId) {
        System.out.println("categoryId = " + categoryId);

        List<Product> products;
        if (categoryId == null) {
            Page<Product> page = productRepository.findAll(PageRequest.of(0, 2, Sort.by("price")));
            products = page.getContent();
//            List<Product> x = productRepository.findAll();
        }
        else {
//            products = productRepository.findProductsWithCategoryId(categoryId);
//            products = productRepository.findProductsByCategory(new Category(categoryId));
            products = productRepository.findProductsByCategoryId(categoryId);
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            ProductDto productDto = productMapper.toDto(product);
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.status(201).body(productDto);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
//        product.setCategory(category);
        productRepository.save(product);

        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
