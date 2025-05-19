package com.example.grocery.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.Entity.PartnerOrder;
import com.example.grocery.Repository.PartnerOrderRepository;

@Service
public class PartnerOrderService {
    @Autowired
    private PartnerOrderRepository orderRepo;

    public PartnerOrder saveOrder(PartnerOrder order) {
        return orderRepo.save(order);
    }

    public List<PartnerOrder> getOrdersByPartner(Long partnerId) {
        return orderRepo.findByPartnerId(partnerId);
    }

    public Optional<PartnerOrder> getOrderById(Long id) {
        return orderRepo.findById(id);
    }

    public List<PartnerOrder> getAllOrders() {
        return orderRepo.findAll();
    }
}