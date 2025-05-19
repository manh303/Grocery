package com.example.grocery.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.grocery.Entity.Partner;
import com.example.grocery.Service.PartnerService;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @GetMapping
    public List<Partner> getAll() {
        return partnerService.getAllPartners();
    }

    @PostMapping
    public Partner create(@RequestBody Partner partner) {
        return partnerService.savePartner(partner);
    }

    // Add other methods as needed, such as update or delete partners
    // @PutMapping("/{id}")
    // public ResponseEntity<Partner> update(@PathVariable Long id, @RequestBody Partner partner) {
    //     Optional<Partner> existingPartner = partnerService.getPartnerById(id);
}
