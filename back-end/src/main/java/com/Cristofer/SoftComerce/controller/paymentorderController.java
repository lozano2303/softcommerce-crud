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

import com.Cristofer.SoftComerce.DTO.paymentorderDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.paymentorder;
import com.Cristofer.SoftComerce.model.paymentorderId;
import com.Cristofer.SoftComerce.service.paymentorderService;

@RestController
@RequestMapping("/api/v1/paymentorder")
public class paymentorderController {

    @Autowired
    private paymentorderService paymentorderService;

    // Crear una nueva relación payment-order
    @PostMapping("/")
public ResponseEntity<Object> createPaymentOrder(@RequestBody paymentorderDTO paymentorderDTO) {
    System.out.println("Datos recibidos - paymentID: " + paymentorderDTO.getPaymentID());
    System.out.println("Datos recibidos - orderID: " + paymentorderDTO.getOrderID());
    
    if (paymentorderDTO.getPaymentID() == 0 || paymentorderDTO.getOrderID() == 0) {
        System.out.println("Datos inválidos recibidos");
    }
    
    responseDTO response = paymentorderService.save(paymentorderDTO);
    return new ResponseEntity<>(response, response.getStatus().equals("success") ? 
        HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
}

    // Obtener todas las relaciones payment-order
    @GetMapping("/")
    public ResponseEntity<Object> getAllPaymentOrders() {
        return new ResponseEntity<>(paymentorderService.findAll(), HttpStatus.OK);
    }

    // Obtener una relación payment-order por ID compuesto
    @GetMapping("/{paymentID}/{orderID}")
    public ResponseEntity<Object> getPaymentOrderById(@PathVariable int paymentID, @PathVariable int orderID) {
        paymentorderId id = new paymentorderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        Optional<paymentorder> paymentOrder = paymentorderService.findById(id);

        if (!paymentOrder.isPresent()) {
            return new ResponseEntity<>("Relación método de pago-orden no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentOrder.get(), HttpStatus.OK);
    }

    // Actualizar una relación payment-order por ID compuesto
    @PutMapping("/{paymentID}/{orderID}")
    public ResponseEntity<Object> updatePaymentOrder(
            @PathVariable int paymentID,
            @PathVariable int orderID,
            @RequestBody paymentorderDTO paymentorderDTO) {
        paymentorderId id = new paymentorderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        responseDTO response = paymentorderService.update(id, paymentorderDTO);

        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar una relación payment-order por ID compuesto
    @DeleteMapping("/{paymentID}/{orderID}")
    public ResponseEntity<Object> deletePaymentOrder(@PathVariable int paymentID, @PathVariable int orderID) {
        paymentorderId id = new paymentorderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        responseDTO response = paymentorderService.deleteById(id);

        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar relaciones payment-order por parámetros opcionales
    @GetMapping("/filter")
    public ResponseEntity<Object> filterPaymentOrder(
            @RequestParam(required = false, name = "paymentID") Integer paymentID,
            @RequestParam(required = false, name = "orderID") Integer orderID) {

        var paymentOrderList = paymentorderService.filterPaymentOrders(paymentID, orderID);
        return new ResponseEntity<>(paymentOrderList, HttpStatus.OK);
    }
}