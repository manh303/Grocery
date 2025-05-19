package com.example.grocery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.grocery.Entity.InventoryLog;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    // Custom query methods can be defined here if needed

    // For example, you can add methods to find inventory logs by product ID or date range
    List<InventoryLog> findByProductId(Long productId);
    List<InventoryLog> findByEventType(String eventType);
    boolean existsByProductId(Long productId);


}