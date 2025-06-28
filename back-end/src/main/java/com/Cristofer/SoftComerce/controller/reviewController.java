package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.ReviewDTO;
import com.Cristofer.SoftComerce.service.ReviewService;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Crear una nueva reseña
    @PostMapping("/")
    public ResponseEntity<Object> createReview(@RequestBody ReviewDTO reviewDTO) {
        ResponseDTO response = reviewService.save(reviewDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las reseñas
    @GetMapping("/")
    public ResponseEntity<Object> getAllReviews() {
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    // Obtener una reseña por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getReviewById(@PathVariable int id) {
        var review = reviewService.findById(id);
        if (!review.isPresent()) {
            return new ResponseEntity<>("Reseña no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review.get(), HttpStatus.OK);
    }

    // Actualizar una reseña por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReview(@PathVariable int id, @RequestBody ReviewDTO reviewDTO) {
        ResponseDTO response = reviewService.update(id, reviewDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar una reseña por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable int id) {
        ResponseDTO response = reviewService.deleteById(id);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar reseñas por campos opcionales
    @GetMapping("/filter")
    public ResponseEntity<Object> filterReviews(
            @RequestParam(required = false, name = "rating") Integer rating,
            @RequestParam(required = false, name = "comment") String comment,
            @RequestParam(required = false, name = "userID") Integer userID,
            @RequestParam(required = false, name = "productID") Integer productID) {

        var reviewList = reviewService.filterReviews(rating, comment, userID, productID);
        return new ResponseEntity<>(reviewList, HttpStatus.OK);
    }
}