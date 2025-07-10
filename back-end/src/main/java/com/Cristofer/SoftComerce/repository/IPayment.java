package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Payment;

public interface IPayment extends JpaRepository<Payment, Integer> {

    // Obtener todos los pagos activos
    @Query("SELECT p FROM payment p WHERE p.status = true")
    List<Payment> getListPaymentActive();

    @Query("SELECT p FROM payment p WHERE p.method LIKE %:method%")
    List<Payment> findByMethod(@Param("method") String method);

    // Filtrar pagos por parámetros opcionales
    @Query("""
        SELECT p
        FROM payment p
        WHERE (:userId IS NULL OR p.user.userID = :userId)
            AND (:method IS NULL OR p.method LIKE %:method%)
            AND (:status IS NULL OR p.status = :status)
    """)
    List<Payment> filterPayment(
        @Param("userId") Integer userId,
        @Param("method") String method,
        @Param("status") Boolean status
    );
}