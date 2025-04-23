package com.Cristofer.SoftComerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.order;

import java.util.List;

public interface Iorder extends JpaRepository<order, Integer> {
    
    // Buscar órdenes por el ID del usuario
    @Query("SELECT o FROM order o WHERE o.userID.userID = :userID")
    List<order> findByUserID(@Param("userID") int userID);
    
    // Buscar órdenes por estado
    @Query("SELECT o FROM order o WHERE o.status = :status")
    List<order> findByStatus(@Param("status") boolean status);
}