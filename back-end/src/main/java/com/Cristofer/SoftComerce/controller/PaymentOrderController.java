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

import com.Cristofer.SoftComerce.DTO.PaymentOrderDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.PaymentOrder;
import com.Cristofer.SoftComerce.model.PaymentOrderId;
import com.Cristofer.SoftComerce.service.PaymentOrderService;

@RestController
@RequestMapping("/api/v1/paymentorder")
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    // Crear una nueva relación payment-order
    @PostMapping("/")
    public ResponseEntity<Object> createPaymentOrder(@RequestBody PaymentOrderDTO paymentOrderDTO) {
        System.out.println("Datos recibidos - paymentID: " + paymentOrderDTO.getPaymentID());
        System.out.println("Datos recibidos - orderID: " + paymentOrderDTO.getOrderID());

        if (paymentOrderDTO.getPaymentID() == 0 || paymentOrderDTO.getOrderID() == 0) {
            System.out.println("Datos inválidos recibidos");
        }

        ResponseDTO response = paymentOrderService.save(paymentOrderDTO);
        return new ResponseEntity<>(response, response.getStatus().equals("success") ?
                HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    // Obtener todas las relaciones payment-order
    @GetMapping("/")
    public ResponseEntity<Object> getAllPaymentOrders() {
        return new ResponseEntity<>(paymentOrderService.findAll(), HttpStatus.OK);
    }

    // Obtener una relación payment-order por ID compuesto
    @GetMapping("/{paymentID}/{orderID}")
    public ResponseEntity<Object> getPaymentOrderById(@PathVariable int paymentID, @PathVariable int orderID) {
        PaymentOrderId id = new PaymentOrderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        Optional<PaymentOrder> paymentOrder = paymentOrderService.findById(id);

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
            @RequestBody PaymentOrderDTO paymentOrderDTO) {
        PaymentOrderId id = new PaymentOrderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        ResponseDTO response = paymentOrderService.update(id, paymentOrderDTO);

        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar una relación payment-order por ID compuesto
    @DeleteMapping("/{paymentID}/{orderID}")
    public ResponseEntity<Object> deletePaymentOrder(@PathVariable int paymentID, @PathVariable int orderID) {
        PaymentOrderId id = new PaymentOrderId();
        id.setPaymentID(paymentID);
        id.setOrderID(orderID);
        ResponseDTO response = paymentOrderService.deleteById(id);

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

        var paymentOrderList = paymentOrderService.filterPaymentOrders(paymentID, orderID);
        return new ResponseEntity<>(paymentOrderList, HttpStatus.OK);
    }
}