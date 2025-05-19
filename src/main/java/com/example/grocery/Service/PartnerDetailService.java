package com.example.grocery.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.Entity.PartnerDetail;
import com.example.grocery.Repository.PartnerDetailRepository;

@Service
public class PartnerDetailService {
    @Autowired
    private PartnerDetailRepository detailRepo;

    public List<PartnerDetail> getDetailsByOrder(Long orderId) {
        return detailRepo.findByOrderId(orderId);
    }

    public PartnerDetail saveDetail(PartnerDetail detail) {
        return detailRepo.save(detail);
    }
}