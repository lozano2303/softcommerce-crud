package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.reviewDTO;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.model.review;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Iproduct;
import com.Cristofer.SoftComerce.repository.Ireview;
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class reviewService {

    @Autowired
    private Ireview reviewRepository;

    @Autowired
    private Iuser userRepository;

    @Autowired
    private Iproduct productRepository;

    // Guardar una reseña
    public void save(reviewDTO reviewDTO) {
        // Obtener el usuario y el producto por sus IDs
        user user = userRepository.findById(reviewDTO.getUserID()).orElse(null);
        product product = productRepository.findById(reviewDTO.getProductID()).orElse(null);

        if (user != null && product != null) {
            review review = new review();
            review.setRating(reviewDTO.getRating());
            review.setComment(reviewDTO.getComment());
            review.setCreatedAt(LocalDateTime.now());
            review.setUser(user);
            review.setProduct(product);

            reviewRepository.save(review);
        }
    }

    // Convertir de review a reviewDTO
    public reviewDTO convertToDTO(review review) {
        return new reviewDTO(
            review.getRating(),
            review.getComment(),
            review.getUser().getUserID(),
            review.getProduct().getProductID()
        );
    }

    // Convertir de reviewDTO a review
    public review convertToModel(reviewDTO reviewDTO) {
        review review = new review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());

        // Obtener el usuario y el producto por sus IDs
        user user = userRepository.findById(reviewDTO.getUserID()).orElse(null);
        product product = productRepository.findById(reviewDTO.getProductID()).orElse(null);

        review.setUser(user);
        review.setProduct(product);

        return review;
    }
}