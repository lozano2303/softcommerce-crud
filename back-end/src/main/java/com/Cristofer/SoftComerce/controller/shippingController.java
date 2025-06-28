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

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.ShippingDTO;
import com.Cristofer.SoftComerce.service.ShippingService;

@RestController
@RequestMapping("/api/v1/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    // Registrar un nuevo envío
    @PostMapping("/")
    public ResponseEntity<Object> registerShipping(@RequestBody ShippingDTO shippingDTO) {
        ResponseDTO response = shippingService.save(shippingDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los envíos
    @GetMapping("/")
    public ResponseEntity<Object> getAllShippings() {
        return new ResponseEntity<>(shippingService.findAll(), HttpStatus.OK);
    }

    // Obtener un envío por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getShippingById(@PathVariable int id) {
        Optional<ShippingDTO> shipping = shippingService.findById(id).map(shippingService::convertToDTO);
        if (!shipping.isPresent()) {
            return new ResponseEntity<>("Envío no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shipping.get(), HttpStatus.OK);
    }

    // Desactivar un envío por su ID
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ResponseDTO> deactivateShipping(@PathVariable int id) {
        ResponseDTO response = shippingService.deactivateShipping(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Reactivar un envío por su ID
    @PutMapping("/reactivate/{id}")
    public ResponseEntity<ResponseDTO> reactivateShipping(@PathVariable int id) {
        ResponseDTO response = shippingService.reactivateShipping(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Actualizar un envío por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShipping(@PathVariable int id, @RequestBody ShippingDTO shippingDTO) {
        ResponseDTO response = shippingService.update(id, shippingDTO);
        if (response.getStatus().equals("success")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar envíos por campos opcionales
    @GetMapping("/filter")
    public ResponseEntity<Object> filterShippings(
            @RequestParam(required = false, name = "orderID") Integer orderID,
            @RequestParam(required = false, name = "address") String address,
            @RequestParam(required = false, name = "city") String city,
            @RequestParam(required = false, name = "country") String country,
            @RequestParam(required = false, name = "postal_code") String postalCode) {

        var shippingList = shippingService.filterShippings(orderID, address, city, country, postalCode);
        return new ResponseEntity<>(shippingList, HttpStatus.OK);
    }
}