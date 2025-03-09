package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.categoryDTO;
import com.Cristofer.SoftComerce.service.categoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/category")
public class categoryController {

    @Autowired
    private categoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<Object> registerCategory(@RequestBody categoryDTO category) {
        categoryService.save(category);
        return new ResponseEntity<>("register category OK", HttpStatus.OK);
    }
}
