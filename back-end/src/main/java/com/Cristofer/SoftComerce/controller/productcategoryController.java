package com.Cristofer.SoftComerce.controller;

import com.Cristofer.SoftComerce.DTO.productcategoryDTO;
import com.Cristofer.SoftComerce.service.productcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productcategory")
public class productcategoryController {

    @Autowired
    private productcategoryService productcategoryService;

    @PostMapping("/")
    public ResponseEntity<Object> createProductCategory(@RequestBody productcategoryDTO productcategoryDTO) {
        productcategoryService.save(productcategoryDTO);
        return new ResponseEntity<>("productcategory created successfully", HttpStatus.CREATED);
    }
}
