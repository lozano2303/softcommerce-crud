package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Order;

public interface IOrder extends JpaRepository<Order, Integer> {

    // Buscar órdenes por el ID del usuario
    @Query("SELECT o FROM order o WHERE o.userID.userID = :userID")
    List<Order> findByUserID(@Param("userID") int userID);

    // Buscar órdenes por estado
    @Query("SELECT o FROM order o WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") boolean status);

    // Buscar órdenes por nombre de usuario
    @Query("SELECT o FROM order o WHERE o.userID.name = :name")
    List<Order> findByUserName(@Param("name") String name);

    // Buscar órdenes por lista de IDs de usuario
    List<Order> findByUserID_UserIDIn(List<Integer> userIds);
}