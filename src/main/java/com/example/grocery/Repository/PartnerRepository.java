package com.example.grocery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.Entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}