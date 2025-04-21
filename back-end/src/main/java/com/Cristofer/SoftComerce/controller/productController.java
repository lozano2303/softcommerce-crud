package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.productDTO;
import com.Cristofer.SoftComerce.service.productService;



@RestController
@RequestMapping("/api/v1/products")
public class productController {

    @Autowired
    private productService productService;

    @PostMapping("/")
    public ResponseEntity<Object> registerProduct (@RequestBody productDTO product){
        productService.save(product);
        return new ResponseEntity<>("product OK", HttpStatus.OK);
    }

}