package com.example.grocery.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.grocery.Entity.Category;
import com.example.grocery.Service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service) { this.service = service; }

    @GetMapping
    public String list(Model m) {
        m.addAttribute("categories", service.listAll());
        return "categories";
    }

    @PostMapping
    public String add(Category c) {
        service.save(c);
        return "redirect:/categories";
    }

    @PostMapping("/update")
    public String update(Category c) {
        service.save(c); // dùng chung hàm save để cập nhật
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id); // cần thêm hàm delete trong service
        return "redirect:/categories";
    }
}