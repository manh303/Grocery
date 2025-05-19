package com.example.grocery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.grocery.Entity.PartnerDetail;

@Repository
public interface PartnerDetailRepository extends JpaRepository<PartnerDetail, Long> {
    List<PartnerDetail> findByOrderId(Long orderId);
}