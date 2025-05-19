package com.example.grocery.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.grocery.Entity.PartnerOrder;
import com.example.grocery.Service.PartnerOrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private PartnerOrderService orderService;

    @GetMapping
    public String getAllOrders(Model model) {
        List<PartnerOrder> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order_history";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PartnerOrder> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "order_detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
            return "redirect:/orders";
        }
    }

    @PostMapping("/{id}/update-status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            RedirectAttributes redirectAttributes) {
        try {
            Optional<PartnerOrder> orderOpt = orderService.getOrderById(id);
            if (orderOpt.isPresent()) {
                PartnerOrder order = orderOpt.get();
                String status = request.get("status");
                if (status == null || (!status.equals("PAID") && !status.equals("PENDING"))) {
                    return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
                }
                order.setPaymentStatus(status);
                orderService.saveOrder(order);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PartnerOrder> createOrder(@RequestBody PartnerOrder order) {
        PartnerOrder savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }
}