package com.example.grocery.DTO;

public class PartnerDetailDTO {
    private String productName;
    private int quantity;
    private double unitPrice;

    public PartnerDetailDTO(String productName, int quantity, double unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
        public String getProductName() {
            return productName;
        }
    
        public void setProductName(String productName) {
            this.productName = productName;
        }
    
        public int getQuantity() {
            return quantity;
        }
    
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    
        public double getUnitPrice() {
            return unitPrice;
        }
    
        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }
}