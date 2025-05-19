package com.example.grocery.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.grocery.Entity.InventoryLog;
import com.example.grocery.Entity.Product;
import com.example.grocery.Service.InventoryLogService;
import com.example.grocery.Service.ProductService;

@Controller
public class InventoryController {
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryLogService inventoryLogService;

    @GetMapping("/import-products")
    public String showImportForm(Model model) {
        model.addAttribute("products", productService.getAllProducts().stream()
            .filter(p -> p.getStatus().equals("active"))
            .collect(java.util.stream.Collectors.toList()));
        return "import_product";
    }

    @GetMapping("/inventory-history")
    public String showInventoryHistory(Model model) {
        List<InventoryLog> history = inventoryLogService.getAllInventoryLogs();
        
        // Lấy danh sách sản phẩm để map ID với tên
        Map<Long, String> productNames = productService.getAllProducts().stream()
            .collect(Collectors.toMap(Product::getId, Product::getName));
        
        model.addAttribute("history", history);
        model.addAttribute("productNames", productNames);
        return "inventory_history";
    }

    @PostMapping("/import-products")
    public String importProduct(
        @RequestParam("productId") Long productId,
        @RequestParam("quantity") Integer quantity,
        @RequestParam("note") String note,
        RedirectAttributes redirectAttributes
    ) {
        try {
            inventoryLogService.importProduct(productId, quantity, note);
            redirectAttributes.addFlashAttribute("success", "Nhập hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/import-products";
    }
}
