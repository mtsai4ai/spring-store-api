package com.kantares.store.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    @EntityGraph(attributePaths = {"category"})
//    List<Product> findAllWithCategory();

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :categoryId")
    List<Product> findProductsWithCategoryId(@Param("categoryId") Byte categoryId);

    @EntityGraph(attributePaths = {"category"})
    List<Product> findProductsByCategory(Category category);

    @EntityGraph(attributePaths = "category")
    List<Product> findProductsByCategoryId(Byte categoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.price ASC")
    List<Product> findAllByOrderByPriceAsc();
}