package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Cristofer.SoftComerce.DTO.paymentDTO;
import com.Cristofer.SoftComerce.service.paymentService;

@RestController
@RequestMapping("/api/v1/payment")
public class paymentController {

    @Autowired
    private paymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<Object> registerPayment(@RequestBody paymentDTO payment) {
        paymentService.save(payment);
        return new ResponseEntity<>("Payment registered successfully", HttpStatus.OK);
    }
}
