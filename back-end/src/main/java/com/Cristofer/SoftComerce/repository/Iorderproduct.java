package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;

@Repository
public interface Iorderproduct extends JpaRepository<orderproduct, orderproductId> {
    // Métodos personalizados si es necesario
}