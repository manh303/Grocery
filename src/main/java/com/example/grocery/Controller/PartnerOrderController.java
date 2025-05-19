package com.example.grocery.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.DTO.PartnerDetailDTO;
import com.example.grocery.DTO.PartnerOrderDTO;
import com.example.grocery.Entity.PartnerOrder;
import com.example.grocery.Service.PartnerOrderService;

@RestController
@RequestMapping("/api/orders")
public class PartnerOrderController {

    @Autowired
    private PartnerOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<PartnerOrderDTO> getOrder(@PathVariable Long id) {
        Optional<PartnerOrder> optionalOrder = orderService.getOrderById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PartnerOrder order = optionalOrder.get();

        List<PartnerDetailDTO> detailDTOs = order.getItems().stream().map(detail ->
            new PartnerDetailDTO(
                detail.getProduct().getName(),
                detail.getQuantity(),
                detail.getUnitPrice()
            )
        ).collect(Collectors.toList());

        PartnerOrderDTO dto = new PartnerOrderDTO(
            order.getId(),
            order.getPartner().getName(),
            order.getOrderDate(),
            order.getTotalAmount(),
            order.getPaymentStatus(),
            detailDTOs
        );

        return ResponseEntity.ok(dto);
    }
}