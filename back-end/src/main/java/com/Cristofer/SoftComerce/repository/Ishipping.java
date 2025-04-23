package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.shipping;

public interface Ishipping extends JpaRepository<shipping, Integer> {

    // Obtener todos los envíos activos
    @Query("SELECT s FROM shipping s WHERE s.status = true")
    List<shipping> getListShippingActive();

    // Filtrar envíos por parámetros opcionales
    @Query("""
        SELECT s
        FROM shipping s
        WHERE (:orderID IS NULL OR s.order.orderID = :orderID)
            AND (:address IS NULL OR s.address LIKE %:address%)
            AND (:city IS NULL OR s.city LIKE %:city%)
            AND (:country IS NULL OR s.country LIKE %:country%)
            AND (:status IS NULL OR s.status = :status)
    """)
    List<shipping> filterShipping(
        @Param("orderID") Integer orderID,
        @Param("address") String address,
        @Param("city") String city,
        @Param("country") String country,
        @Param("status") Boolean status
    );
}