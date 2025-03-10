package com.Cristofer.SoftComerce.controller;

import com.Cristofer.SoftComerce.DTO.paymentorderDTO;
import com.Cristofer.SoftComerce.service.paymentorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paymentorder")
public class paymentorderController {

    @Autowired
    private paymentorderService paymentorderService;

    @PostMapping("/")
    public ResponseEntity<Object> createPaymentOrder(@RequestBody paymentorderDTO paymentorderDTO) {
        paymentorderService.save(paymentorderDTO);
        return new ResponseEntity<>("paymentorder created successfully", HttpStatus.CREATED);
    }
}
