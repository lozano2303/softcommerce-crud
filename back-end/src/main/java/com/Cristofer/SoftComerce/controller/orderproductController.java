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

import com.Cristofer.SoftComerce.DTO.orderproductDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;
import com.Cristofer.SoftComerce.service.orderproductService;

@RestController
@RequestMapping("/api/v1/orderproduct")
public class orderproductController {

    @Autowired
    private orderproductService orderproductService;

    // Crear una nueva relación order-product
    @PostMapping("/")
    public ResponseEntity<Object> createOrderProduct(@RequestBody orderproductDTO orderproductDTO) {
        responseDTO response = orderproductService.save(orderproductDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las relaciones order-product
    @GetMapping("/")
    public ResponseEntity<Object> getAllOrderProducts() {
        return new ResponseEntity<>(orderproductService.findAll(), HttpStatus.OK);
    }

    // Obtener una relación order-product por ID compuesto
    @GetMapping("/{orderID}/{productID}")
    public ResponseEntity<Object> getOrderProductById(@PathVariable int orderID, @PathVariable int productID) {
        orderproductId id = new orderproductId();
        id.setOrderID(orderID);
        id.setProductID(productID);
        Optional<orderproduct> orderProduct = orderproductService.findById(id);

        if (!orderProduct.isPresent()) {
            return new ResponseEntity<>("Relación orden-producto no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderProduct.get(), HttpStatus.OK);
    }

    // Actualizar una relación order-product por ID compuesto
    @PutMapping("/{orderID}/{productID}")
    public ResponseEntity<Object> updateOrderProduct(
            @PathVariable int orderID,
            @PathVariable int productID,
            @RequestBody orderproductDTO orderproductDTO) {
        orderproductId id = new orderproductId();
        id.setOrderID(orderID);
        id.setProductID(productID);
        responseDTO response = orderproductService.update(id, orderproductDTO);

        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar una relación order-product por ID compuesto
    @DeleteMapping("/{orderID}/{productID}")
    public ResponseEntity<Object> deleteOrderProduct(@PathVariable int orderID, @PathVariable int productID) {
        orderproductId id = new orderproductId();
        id.setOrderID(orderID);
        id.setProductID(productID);
        responseDTO response = orderproductService.deleteById(id);

        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar relaciones order-product por parámetros opcionales
    @GetMapping("/filter")
    public ResponseEntity<Object> filterOrderProduct(
            @RequestParam(required = false, name = "orderID") Integer orderID,
            @RequestParam(required = false, name = "productID") Integer productID,
            @RequestParam(required = false, name = "quantity") Integer quantity,
            @RequestParam(required = false, name = "subtotal") Double subtotal) {

        var orderProductList = orderproductService.filterOrderProducts(orderID, productID, quantity, subtotal);
        return new ResponseEntity<>(orderProductList, HttpStatus.OK);
    }
}