package com.Cristofer.SoftComerce.controller;

import com.Cristofer.SoftComerce.DTO.orderproductDTO;
import com.Cristofer.SoftComerce.service.orderproductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orderproduct")
public class orderproductController {

    @Autowired
    private orderproductService orderproductService;

    @PostMapping("/")
    public ResponseEntity<Object> createOrderProduct(@RequestBody orderproductDTO orderproductDTO) {
        orderproductService.save(orderproductDTO);
        return new ResponseEntity<>("orderproduct created successfully", HttpStatus.CREATED);
    }
}