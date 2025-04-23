package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.paymentorder;
import com.Cristofer.SoftComerce.model.paymentorderId;

public interface Ipaymentorder extends JpaRepository<paymentorder, paymentorderId> {

    // Método para filtrar relaciones payment-order
    @Query("""
        SELECT po
        FROM paymentorder po
        WHERE (:paymentID IS NULL OR po.payment.paymentID = :paymentID)
        AND (:orderID IS NULL OR po.order.orderID = :orderID)
    """)
    List<paymentorder> filterPaymentOrders(
        @Param("paymentID") Integer paymentID,
        @Param("orderID") Integer orderID
    );
}