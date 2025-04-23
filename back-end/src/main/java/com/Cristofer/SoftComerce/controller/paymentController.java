package com.Cristofer.SoftComerce.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.paymentDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.service.paymentService;

@RestController
@RequestMapping("/api/v1/payment")
public class paymentController {

    @Autowired
    private paymentService paymentService;

    // Registrar un nuevo pago
    @PostMapping("/")
    public ResponseEntity<Object> registerPayment(@RequestBody paymentDTO paymentDTO) {
        responseDTO response = paymentService.save(paymentDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los pagos
    @GetMapping("/")
    public ResponseEntity<Object> getAllPayments() {
        return new ResponseEntity<>(paymentService.findAll(), HttpStatus.OK);
    }

    // Obtener un pago por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPaymentById(@PathVariable int id) {
        Optional<paymentDTO> payment = paymentService.findById(id).map(paymentService::convertToDTO);
        if (!payment.isPresent()) {
            return new ResponseEntity<>("Pago no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(payment.get(), HttpStatus.OK);
    }

    // Eliminar un pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePayment(@PathVariable int id) {
        responseDTO response = paymentService.deleteById(id);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar un pago por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePayment(@PathVariable int id, @RequestBody paymentDTO paymentDTO) {
        responseDTO response = paymentService.update(id, paymentDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar pagos por método
    @GetMapping("/filter")
    public ResponseEntity<Object> filterPaymentsByMethod(
            @RequestParam(required = false, name = "method") String method) {
        
        var paymentList = paymentService.filterPaymentsByMethod(method);
        return new ResponseEntity<>(paymentList, HttpStatus.OK);
    }
}