package com.example.grocery.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_log")
public class InventoryLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "event_type")
    private String eventType; // "ADD" or "REMOVE"
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "event_time")
    private Date event_time; // You can use LocalDateTime for better date handling
    @Column(name = "note")
    private String note;

    public InventoryLog() {
    }

    public InventoryLog(Long id, Long productId, String eventType, int quantity, Date event_time, String note) {
        this.id = id;
        this.productId = productId;
        this.eventType = eventType;
        this.quantity = quantity;
        this.event_time = event_time;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Date event_time) {
        this.event_time = event_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
