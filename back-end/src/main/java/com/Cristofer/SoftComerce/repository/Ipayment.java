package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Cristofer.SoftComerce.model.payment;

public interface Ipayment extends JpaRepository<payment, Integer> {
    /* CRUD methods */
}
