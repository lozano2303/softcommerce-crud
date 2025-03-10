package com.Cristofer.SoftComerce.repository;

import com.Cristofer.SoftComerce.model.paymentorder;
import com.Cristofer.SoftComerce.model.paymentorderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Ipaymentorder extends JpaRepository<paymentorder, paymentorderId> {
    // Métodos personalizados si es necesario
}