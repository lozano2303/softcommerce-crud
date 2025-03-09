package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.reviewDTO;
import com.Cristofer.SoftComerce.service.reviewService;

@RestController
@RequestMapping("/api/v1/review")
public class reviewController {

    @Autowired
    private reviewService reviewService;

    @PostMapping("/")
    public ResponseEntity<Object> createReview(@RequestBody reviewDTO reviewDTO) {
        reviewService.save(reviewDTO);
        return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
    }
}