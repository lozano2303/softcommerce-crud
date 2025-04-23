package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.review;

public interface Ireview extends JpaRepository<review, Integer> {

    // Obtener todas las reseñas activas (si existe un campo de estado, opcional)
    @Query("SELECT r FROM review r WHERE r.status != false")
    List<review> getActiveReviews();

    // Filtrar reseñas por calificación, usuario y producto
    @Query("""
        SELECT r
        FROM review r
        WHERE (:rating IS NULL OR r.rating = :rating)
            AND (:userId IS NULL OR r.user.userID = :userId)
            AND (:productId IS NULL OR r.product.productID = :productId)
    """)
    List<review> filterReviews(
        @Param("rating") Integer rating,
        @Param("userId") Integer userId,
        @Param("productId") Integer productId
    );
}