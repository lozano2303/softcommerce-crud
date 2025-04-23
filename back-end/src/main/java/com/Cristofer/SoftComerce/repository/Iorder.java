package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.order;

public interface Iorder extends JpaRepository<order, Integer> {
    
    // Buscar órdenes por el ID del usuario (versión con userID)
    @Query("SELECT o FROM order o WHERE o.userID.userID = :userID")
    List<order> findByUserId(@Param("userID") int userID);
    
    // Buscar órdenes por el ID del usuario (versión con userId - nombre alternativo)
    @Query("SELECT o FROM order o WHERE o.userID.userID = :userId")
    List<order> findByUserID(@Param("userId") int userId);
    
    // Buscar órdenes por estado
    @Query("SELECT o FROM order o WHERE o.status = :status")
    List<order> findByStatus(@Param("status") boolean status);
}