package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.category;

public interface Icategory extends JpaRepository<category, Integer> {

    // 🔍 Búsqueda por nombre de categoría (similar a validateProduct)
    @Query("SELECT c FROM category c WHERE c.categoryName LIKE %:categoryName%")
    List<category> findByCategoryName(@Param("categoryName") String categoryName);

    // 🔍 Filtro avanzado
    @Query("SELECT c FROM category c WHERE " +
            "(:categoryName IS NULL OR c.categoryName LIKE %:categoryName%)")
    List<category> filterCategories(
        @Param("categoryName") String categoryName
    );
}