package com.example.grocery.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.grocery.Entity.Category;
import com.example.grocery.Entity.PartnerDetail;
import com.example.grocery.Entity.PartnerOrder;
import com.example.grocery.Entity.Product;
import com.example.grocery.Service.CategoryService;
import com.example.grocery.Service.InventoryLogService;
import com.example.grocery.Service.PartnerOrderService;
import com.example.grocery.Service.PartnerService;
import com.example.grocery.Service.ProductService;

@Controller
public class HomeController {

    @Autowired
    private PartnerOrderService orderService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InventoryLogService inventoryLogService;

    // Trang chủ hiển thị danh sách hóa đơn
    @GetMapping("/")
    public String viewHome(Model model) {
        // Lấy danh sách đơn hàng
        List<PartnerOrder> orders = orderService.getAllOrders();
        System.out.println("Đã lấy " + orders.size() + " đơn hàng từ DB");
        model.addAttribute("orders", orders);

        // Lấy danh sách sản phẩm
        List<Product> products = productService.getAllProducts().stream()
            .filter(p -> p.getStatus().equals("active"))
            .collect(Collectors.toList());
        model.addAttribute("products", products);

        // Lấy danh sách danh mục cho filter
        model.addAttribute("categories", categoryService.listAll());

        // Lấy danh sách sản phẩm sắp hết (số lượng < 10)
        List<Product> lowStockProducts = productService.getAllProducts().stream()
            .filter(p -> p.getQuantityInStock() < 10 && p.getStatus().equals("active"))
            .collect(Collectors.toList());
        model.addAttribute("lowStockProducts", lowStockProducts);

        // Tính tổng doanh thu
        double totalRevenue = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .mapToDouble(PartnerOrder::getTotalAmount)
            .sum();
        model.addAttribute("totalRevenue", totalRevenue);

        // Tính doanh thu theo tháng
        double monthlyRevenue = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .filter(order -> order.getOrderDate().getMonth() == LocalDateTime.now().getMonth())
            .mapToDouble(PartnerOrder::getTotalAmount)
            .sum();
        model.addAttribute("monthlyRevenue", monthlyRevenue);

        return "home"; // Trả về view home.html
    }

    @GetMapping("/create-order")
    public String showCreateForm(Model model) {
        PartnerOrder order = new PartnerOrder();
        order.setOrderDate(LocalDateTime.now());
        order.setItems(new ArrayList<>()); // Khởi tạo danh sách items

        Map<Long, Double> productPrices = productService.getAllProducts().stream()
            .filter(p -> p.getStatus().equals("active"))
            .collect(Collectors.toMap(Product::getId, Product::getPrice));
        model.addAttribute("productPrices", productPrices);
        model.addAttribute("order", order);
        model.addAttribute("partners", partnerService.getAllPartners());
        model.addAttribute("products", productService.getAllProducts().stream()
            .filter(p -> p.getStatus().equals("active"))
            .collect(Collectors.toList()));
        
        return "create_order";
    }

    // Xử lý tạo đơn hàng mới
    @PostMapping("/create-order")
    public String saveOrder(@RequestParam("partner.id") Long partnerId,
                          @RequestParam("productIds") List<Long> productIds,
                          @RequestParam("quantities") List<Integer> quantities,
                          @RequestParam("paymentStatus") String paymentStatus,
                          RedirectAttributes redirectAttributes) {
        try {
            PartnerOrder order = new PartnerOrder();
            order.setOrderDate(LocalDateTime.now());
            order.setPaymentStatus(paymentStatus);
            order.setPartner(partnerService.getPartnerById(partnerId).orElseThrow(
                () -> new RuntimeException("Không tìm thấy đối tác")));
            
            List<PartnerDetail> items = new ArrayList<>();
            double totalAmount = 0;

            // Kiểm tra và cập nhật số lượng tồn kho
            for (int i = 0; i < productIds.size(); i++) {
                Long productId = productIds.get(i);
                Integer quantity = quantities.get(i);
                
                Product product = productService.getProductById(productId);
                if (product == null) {
                    throw new RuntimeException("Không tìm thấy sản phẩm");
                }

                // Tạo chi tiết đơn hàng
                PartnerDetail detail = new PartnerDetail();
                detail.setProduct(product);
                detail.setQuantity(quantity);
                detail.setUnitPrice(product.getPrice());
                detail.setOrder(order);
                items.add(detail);

                // Tạo log xuất hàng
                inventoryLogService.exportProduct(productId, quantity, 
                    "Xuất hàng cho đơn hàng #" + order.getId() + " - Đối tác: " + order.getPartner().getName());

                totalAmount += product.getPrice() * quantity;
            }

            order.setItems(items);
            order.setTotalAmount(totalAmount);
            orderService.saveOrder(order);
            
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/create-order";
        }
        return "redirect:/";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        PartnerOrder order = orderService.getOrderById(orderId).orElse(null);
        if (order != null) {
            order.setPaymentStatus(status);
            orderService.saveOrder(order);
        }
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        // Lấy danh sách đơn hàng
        List<PartnerOrder> orders = orderService.getAllOrders();
        
        // Tính tổng doanh thu
        double totalRevenue = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .mapToDouble(PartnerOrder::getTotalAmount)
            .sum();
        model.addAttribute("totalRevenue", totalRevenue);

        // Tính doanh thu theo tháng
        double monthlyRevenue = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .filter(order -> order.getOrderDate().getMonth() == LocalDateTime.now().getMonth())
            .mapToDouble(PartnerOrder::getTotalAmount)
            .sum();
        model.addAttribute("monthlyRevenue", monthlyRevenue);

        // Tính tổng vốn
        double totalCost = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .flatMap(order -> order.getItems().stream())
            .mapToDouble(item -> item.getProduct().getImportPrice() * item.getQuantity())
            .sum();
        model.addAttribute("totalCost", totalCost);

        // Tính vốn theo tháng
        double monthlyCost = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .filter(order -> order.getOrderDate().getMonth() == LocalDateTime.now().getMonth())
            .flatMap(order -> order.getItems().stream())
            .mapToDouble(item -> item.getProduct().getImportPrice() * item.getQuantity())
            .sum();
        model.addAttribute("monthlyCost", monthlyCost);

        // Tính lãi
        double totalProfit = totalRevenue - totalCost;
        double monthlyProfit = monthlyRevenue - monthlyCost;
        model.addAttribute("totalProfit", totalProfit);
        model.addAttribute("monthlyProfit", monthlyProfit);

        // Thống kê số lượng đơn hàng
        long totalOrders = orders.size();
        long paidOrders = orders.stream()
            .filter(order -> order.getPaymentStatus().equals("PAID"))
            .count();
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("paidOrders", paidOrders);

        // Thống kê sản phẩm sắp hết hàng
        List<Product> lowStockProducts = productService.getAllProducts().stream()
            .filter(p -> p.getQuantityInStock() < 10 && p.getStatus().equals("active"))
            .collect(Collectors.toList());
        model.addAttribute("lowStockProducts", lowStockProducts);

        return "dashboard";
    }

    @GetMapping("/create-product")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.listAll());
        return "create_product";
    }

    @PostMapping("/create-product")
    public String createProduct(@ModelAttribute Product product, 
                              @RequestParam("category.id") Long categoryId,
                              RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra danh mục
            if (categoryId == null) {
                throw new RuntimeException("Vui lòng chọn danh mục sản phẩm");
            }

            // Lấy danh mục từ database
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                throw new RuntimeException("Không tìm thấy danh mục sản phẩm");
            }
            product.setCategory(category);

            // Kiểm tra tên sản phẩm
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                throw new RuntimeException("Vui lòng nhập tên sản phẩm");
            }

            // Kiểm tra giá
            if (product.getPrice() == null || product.getPrice() <= 0) {
                throw new RuntimeException("Giá sản phẩm phải lớn hơn 0");
            }

            // Kiểm tra số lượng
            if (product.getQuantityInStock() == null || product.getQuantityInStock() < 0) {
                throw new RuntimeException("Số lượng sản phẩm không được âm");
            }

            // Set trạng thái mặc định nếu chưa có
            if (product.getStatus() == null) {
                product.setStatus("active");
            }

            // Lưu sản phẩm
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/create-product";
        }
        return "redirect:/";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.listAll());
        return "edit_products";
    }

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, 
                              RedirectAttributes redirectAttributes) {
        try {
            Product existingProduct = productService.getProductById(id);
            if (existingProduct == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm");
            }
            
            // Cập nhật thông tin sản phẩm
            existingProduct.setName(product.getName());
            existingProduct.setBarcode(product.getBarcode());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantityInStock(product.getQuantityInStock());
            existingProduct.setExpirationDate(product.getExpirationDate());
            existingProduct.setStatus(product.getStatus());
            
            productService.saveProduct(existingProduct);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/delete-product/{id}")
    @ResponseBody
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm");
            }
            
            // Kiểm tra xem sản phẩm có trong đơn hàng nào không
            List<PartnerOrder> orders = orderService.getAllOrders();
            boolean isInOrder = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .anyMatch(item -> item.getProduct().getId().equals(id));
            
            if (isInOrder) {
                throw new RuntimeException("Không thể xóa sản phẩm đã có trong đơn hàng");
            }
            
            product.setStatus("inactive");
            productService.saveProduct(product);
            
            response.put("success", true);
            response.put("message", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

}