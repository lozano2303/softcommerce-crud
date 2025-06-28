package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Shipping;

public interface IShipping extends JpaRepository<Shipping, Integer> {

    // Filtrar envíos por parámetros opcionales
    @Query("""
        SELECT s
        FROM Shipping s
        WHERE (:orderID IS NULL OR s.order.orderID = :orderID)
            AND (:address IS NULL OR s.address LIKE %:address%)
            AND (:city IS NULL OR s.city LIKE %:city%)
            AND (:country IS NULL OR s.country LIKE %:country%)
            AND (:postalCode IS NULL OR s.postalCode LIKE %:postalCode%)
    """)
    List<Shipping> filterShippings(
        @Param("orderID") Integer orderID,
        @Param("address") String address,
        @Param("city") String city,
        @Param("country") String country,
        @Param("postalCode") String postalCode
    );
}