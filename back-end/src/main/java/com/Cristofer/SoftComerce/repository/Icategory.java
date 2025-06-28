package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Category;

public interface ICategory extends JpaRepository<Category, Integer> {

    // 🔍 Búsqueda por nombre de categoría (similar a validateProduct)
    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE %:categoryName%")
    List<Category> findByCategoryName(@Param("categoryName") String categoryName);

    // 🔍 Filtro avanzado
    @Query("SELECT c FROM Category c WHERE " +
            "(:categoryName IS NULL OR c.categoryName LIKE %:categoryName%)")
    List<Category> filterCategories(
        @Param("categoryName") String categoryName
    );
}