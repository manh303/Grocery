package com.example.grocery.Service;

import java.util.List;

import com.example.grocery.Entity.Category;

public interface CategoryService {
    List<Category> listAll();
    Category getCategoryById(Long id);
    Category save(Category c);
    void delete(Long id);
}
