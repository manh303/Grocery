package com.example.grocery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query method to find products by name
    List<Product> findByName(String name);

    // Custom query method to find products by category ID
    List<Product> findByCategoryId(Long categoryId);

    // Custom query method to find products by barcode
    List<Product> findByBarcode(String barcode);

    List<Product> findByStatus(String status);
    
}