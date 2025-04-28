package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;

public interface Iorderproduct extends JpaRepository<orderproduct, orderproductId> {

    // Obtener los subtotales de los productos relacionados con una orden específica
    @Query("SELECT op.subtotal FROM orderproduct op WHERE op.order.orderID = :orderID")
    List<Double> findSubTotalsByOrderID(@Param("orderID") int orderID);

    // Filtrar relaciones por ID de orden, ID de producto, cantidad o subtotal (Opcional)
    @Query("""
        SELECT op
        FROM orderproduct op
        WHERE (:orderID IS NULL OR op.order.orderID = :orderID)
            AND (:productID IS NULL OR op.product.productID = :productID)
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