package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.service.orderService;

@RestController
@RequestMapping("/api/v1/order")
public class orderController {

    @Autowired
    private orderService orderService;

    // Registrar una nueva orden
    @PostMapping("/")
    public ResponseEntity<Object> registerOrder(@RequestBody orderDTO orderDTO) {
        responseDTO response = orderService.save(orderDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las órdenes
    @GetMapping("/")
    public ResponseEntity<Object> getAllOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    // Obtener una orden por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable int id) {
        var order = orderService.findById(id);
        if (!order.isPresent()) {
            return new ResponseEntity<>("Orden no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    // Eliminar una orden por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable int id) {
        responseDTO response = orderService.deleteById(id);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar una orden por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable int id, @RequestBody orderDTO orderDTO) {
        responseDTO response = orderService.update(id, orderDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}