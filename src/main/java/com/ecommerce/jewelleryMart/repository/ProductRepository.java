package com.ecommerce.jewelleryMart.repository;

import com.ecommerce.jewelleryMart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT p.metalType FROM Product p WHERE p.category = :category AND p.metalType IS NOT NULL")
    List<String> getAllMetalTypes(@Param("category") String category);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
    List<String> getAllCategories();
}
