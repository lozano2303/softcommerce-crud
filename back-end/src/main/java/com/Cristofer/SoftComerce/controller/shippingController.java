package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.shippingDTO;
import com.Cristofer.SoftComerce.service.shippingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/shipping")
public class shippingController {

    @Autowired
    private shippingService shippingService;

    @PostMapping("/")
    public ResponseEntity<Object> registerShipping(@RequestBody shippingDTO shipping) {
        try {
            shippingService.save(shipping);
            return new ResponseEntity<>("Shipping registered successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
