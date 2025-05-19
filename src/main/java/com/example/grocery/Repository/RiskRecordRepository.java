package com.example.grocery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.Entity.RiskRecord;

public interface RiskRecordRepository extends JpaRepository<RiskRecord, Long> {
}