package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;

public interface Iorderproduct extends JpaRepository<orderproduct, orderproductId> {

    // Obtener todas las relaciones activas (si hay un campo de estado relacionado)
    @Query("SELECT op FROM orderproduct op WHERE op.order.status != false")
    List<orderproduct> getActiveOrderProducts();

    // Filtrar relaciones por ID de orden, ID de producto, cantidad o subtotal
    @Query("""
        SELECT op
        FROM orderproduct op
        WHERE (:orderID IS NULL OR op.order.id = :orderID)
            AND (:productID IS NULL OR op.product.id = :productID)
            AND (:quantity IS NULL OR op.quantity = :quantity)
            AND (:subtotal IS NULL OR op.subtotal = :subtotal)
    """)
    List<orderproduct> filterOrderProducts(
        @Param("orderID") Integer orderID,
        @Param("productID") Integer productID,
        @Param("quantity") Integer quantity,
        @Param("subtotal") Double subtotal
    );
}