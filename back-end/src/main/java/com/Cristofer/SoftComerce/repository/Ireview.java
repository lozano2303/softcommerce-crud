package com.Cristofer.SoftComerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Cristofer.SoftComerce.model.Review;

public interface IReview extends JpaRepository<Review, Integer> {

    // Filtrar reseñas por parámetros opcionales
    @Query("""
        SELECT r
        FROM Review r
        WHERE (:rating IS NULL OR r.rating = :rating)
            AND (:comment IS NULL OR r.comment LIKE %:comment%)
            AND (:userID IS NULL OR r.user.userID = :userID)
            AND (:productID IS NULL OR r.product.productID = :productID)
    """)
    List<Review> filterReviews(
        @Param("rating") Integer rating,
        @Param("comment") String comment,
        @Param("userID") Integer userID,
        @Param("productID") Integer productID
    );
}