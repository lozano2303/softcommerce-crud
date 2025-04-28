package com.Cristofer.SoftComerce.controller;

import java.util.List;

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

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO; // Importar la clase order
import com.Cristofer.SoftComerce.model.order;
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
    try {
        orderService.deleteById(id); // Llamar al servicio para eliminar la orden
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Responder con 204 No Content
    } catch (Exception e) {
        return new ResponseEntity<>("Error al eliminar la orden: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    // Filtrar órdenes por nombre del usuario
    @GetMapping("filter")
    public ResponseEntity<Object> filterOrdersByUser(
            @RequestParam(name = "userName") String userName) {
        
        // Llamar al servicio para encontrar órdenes por nombre de usuario
        List<order> orders = orderService.findByUserName(userName);
    
        // Verificar si la lista está vacía
        if (orders.isEmpty()) {
            return new ResponseEntity<>("No se encontraron órdenes para el usuario especificado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}