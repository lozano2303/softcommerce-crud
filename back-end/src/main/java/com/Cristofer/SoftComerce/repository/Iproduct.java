package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.product;

public interface Iproduct extends JpaRepository<product, Integer> {

    @Query("SELECT p FROM product p WHERE p.name LIKE %:name%")
    List<product> validateProduct(@Param("name") String name);

    // 🔍 Filtro avanzado
    @Query("SELECT p FROM product p WHERE " +
           "(:name IS NULL OR p.name LIKE %:name%) AND " +
           "(:description IS NULL OR p.description LIKE %:description%) AND " +
           "(:price IS NULL OR p.price = :price) AND " +
           "(:status IS NULL OR p.status = :status)")
    List<product> filterProducts(
        @Param("name") String name,
        @Param("description") String description,
        @Param("price") Double price,
        @Param("status") Boolean status
    );
}
