package com.example.grocery.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.Entity.Partner;
import com.example.grocery.Repository.PartnerRepository;

@Service
public class PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public Partner savePartner(Partner partner) {
        return partnerRepository.save(partner);
    }

    public Optional<Partner> getPartnerById(Long id) {
        return partnerRepository.findById(id); // ✅
    }

    public void deletePartner(Long id) {
        partnerRepository.deleteById(id);
    }

    
}