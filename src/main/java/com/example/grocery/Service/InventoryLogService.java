package com.example.grocery.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.grocery.Entity.InventoryLog;
import com.example.grocery.Entity.Product;
import com.example.grocery.Repository.InventoryLogRepository;
import com.example.grocery.Repository.ProductRepository;

@Service
public class InventoryLogService {
    
    @Autowired
    private InventoryLogRepository inventoryLogRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void importProduct(Long productId, Integer quantity, String note) {
        // Kiểm tra sản phẩm tồn tại
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Tạo log nhập hàng
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setEventType("ADD");
        log.setQuantity(quantity);
        log.setEvent_time(new Date());
        log.setNote(note);
        inventoryLogRepository.save(log);

        // Cập nhật số lượng tồn kho
        product.setQuantityInStock(product.getQuantityInStock() + quantity);
        productRepository.save(product);
    }

    @Transactional
    public void exportProduct(Long productId, Integer quantity, String note) {
        // Kiểm tra sản phẩm tồn tại
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Kiểm tra số lượng tồn kho
        if (product.getQuantityInStock() < quantity) {
            throw new RuntimeException("Số lượng tồn kho không đủ");
        }

        // Tạo log xuất hàng
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setEventType("REMOVE");
        log.setQuantity(quantity);
        log.setEvent_time(new Date());
        log.setNote(note);
        inventoryLogRepository.save(log);

        // Cập nhật số lượng tồn kho
        product.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(product);
    }
    
    // Example method to get all inventory logs
    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogRepository.findAll();
    }
    
    // Example method to save an inventory log
    public InventoryLog saveInventoryLog(InventoryLog inventoryLog) {
        return inventoryLogRepository.save(inventoryLog);
    }

    // Example method to delete an inventory log by ID
    public void deleteInventoryLog(Long id) {
        inventoryLogRepository.deleteById(id);
    }

    // Example method to find inventory logs by product ID
    public List<InventoryLog> findByProductId(Long productId) {
        return inventoryLogRepository.findByProductId(productId);
    }

}
