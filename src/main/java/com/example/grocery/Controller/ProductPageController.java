package com.example.grocery.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.grocery.Entity.Product;
import com.example.grocery.Repository.CategoryRepository;
import com.example.grocery.Repository.InventoryLogRepository;
import com.example.grocery.Repository.ProductRepository;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    @GetMapping
    public String showProductsPage(Model model) {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            if (product.getName() == null) {
                product.setName("Tên không xác định");
            }
        });
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryRepository.findAll());
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "add_product"; // templates/add_product.html
    }

    @PostMapping
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (inventoryLogRepository.existsByProductId(id)) {
            // Nếu có dữ liệu tồn kho -> chỉ chuyển sang inactive
            Product product = productRepository.findById(id).orElse(null);
            if (product != null) {
                product.setStatus("inactive");
                productRepository.save(product);
                redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được chuyển sang trạng thái 'inactive' vì có dữ liệu liên quan.");
            }
        } else {
            // Nếu không liên quan thì xóa bình thường
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa.");
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepository.findAll());
        }
        return "edit_products"; // templates/edit_product.html
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }


}