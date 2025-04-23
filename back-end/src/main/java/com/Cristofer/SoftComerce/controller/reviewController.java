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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.reviewDTO;
import com.Cristofer.SoftComerce.service.reviewService;

@RestController
@RequestMapping("/api/v1/review")
public class reviewController {

    @Autowired
    private reviewService reviewService;

    // Crear una nueva reseña
    @PostMapping("/")
    public ResponseEntity<Object> createReview(@RequestBody reviewDTO reviewDTO) {
        responseDTO response = reviewService.save(reviewDTO);
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
    public ResponseEntity<Object> updateReview(@PathVariable int id, @RequestBody reviewDTO reviewDTO) {
        responseDTO response = reviewService.update(id, reviewDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar una reseña por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable int id) {
        responseDTO response = reviewService.deleteById(id);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}