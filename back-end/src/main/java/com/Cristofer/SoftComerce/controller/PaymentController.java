package com.Cristofer.SoftComerce.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.PaymentDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Registrar un nuevo pago
    @PostMapping("/")
    public ResponseEntity<Object> registerPayment(@RequestBody PaymentDTO paymentDTO) {
        ResponseDTO response = paymentService.save(paymentDTO);
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
        Optional<PaymentDTO> payment = paymentService.findById(id).map(paymentService::convertToDTO);
        if (!payment.isPresent()) {
            return new ResponseEntity<>("Pago no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(payment.get(), HttpStatus.OK);
    }

    // Eliminar (deshabilitar) un pago por su ID
    @PutMapping("/disable/{id}")
    public ResponseEntity<ResponseDTO> disablePayment(@PathVariable int id) {
        ResponseDTO response = paymentService.disablePayment(id);
        if (response.getStatus().equals("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Reactivar un pago por su ID
    @PutMapping("/reactivate/{id}")
    public ResponseEntity<ResponseDTO> reactivatePayment(@PathVariable int id) {
        ResponseDTO response = paymentService.reactivatePayment(id);
        if (response.getStatus().equals("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Actualizar un pago por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePayment(@PathVariable int id, @RequestBody PaymentDTO paymentDTO) {
        ResponseDTO response = paymentService.update(id, paymentDTO);
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