package com.example.grocery.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.Entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {}
