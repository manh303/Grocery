package com.example.grocery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.Entity.PartnerOrder;

public interface PartnerOrderRepository extends JpaRepository<PartnerOrder, Long> {
    List<PartnerOrder> findByPartnerId(Long partnerId);
}