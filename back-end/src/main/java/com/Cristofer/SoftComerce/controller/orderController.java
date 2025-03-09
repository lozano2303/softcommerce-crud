package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.service.orderService;

@RestController
@RequestMapping("/api/v1/order")
public class orderController {

    @Autowired
    private orderService orderService;

    @PostMapping("/")
    public ResponseEntity<Object> registerorder(@RequestBody orderDTO orderDTO) {
        orderService.save(orderDTO);
        return new ResponseEntity<>("order registered successfully", HttpStatus.OK);
    }
}
