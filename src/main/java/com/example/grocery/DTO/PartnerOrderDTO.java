package com.example.grocery.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class PartnerOrderDTO {
    private Long orderId;
    private String partnerName;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String paymentStatus;
    private List<PartnerDetailDTO> items;

    public PartnerOrderDTO(Long orderId, String partnerName, LocalDateTime orderDate,
                           double totalAmount, String paymentStatus, List<PartnerDetailDTO> items) {
        this.orderId = orderId;
        this.partnerName = partnerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.items = items;
    }

    // Getters and setters
    
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<PartnerDetailDTO> getItems() {
        return items;
    }

    public void setItems(List<PartnerDetailDTO> items) {
        this.items = items;
    }
}
