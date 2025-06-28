package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Order;

public interface IOrder extends JpaRepository<Order, Integer> {
    
    // Buscar órdenes por el ID del usuario (versión con userID)
    @Query("SELECT o FROM Order o WHERE o.userID.userID = :userID")
    List<Order> findByUserId(@Param("userID") int userID);
    
    // Buscar órdenes por el ID del usuario (versión con userId - nombre alternativo)
    @Query("SELECT o FROM Order o WHERE o.userID.userID = :userId")
    List<Order> findByUserID(@Param("userId") int userId);
    
    // Buscar órdenes por estado
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") boolean status);

    // Buscar órdenes por nombre de usuario
    @Query("SELECT o FROM Order o WHERE o.userID.name = :name")
    List<Order> findByUserName(@Param("name") String name); 

    // Buscar órdenes por lista de IDs de usuario
    List<Order> findByUserIDIn(List<Long> userIds);
}