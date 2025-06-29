package com.Cristofer.SoftComerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.RecoveryRequestDTO;
import com.Cristofer.SoftComerce.model.RecoveryRequest;
import com.Cristofer.SoftComerce.service.RecoveryRequestService;


@RestController
@RequestMapping("/api/recovery-requests")
public class RecoveryRequestController {

    @Autowired
    private RecoveryRequestService recoveryRequestService;

    // Listar todas las solicitudes de recuperación
    @GetMapping
    public ResponseEntity<List<RecoveryRequest>> getAll() {
        return ResponseEntity.ok(recoveryRequestService.findAll());
    }

    // Buscar solicitud por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<RecoveryRequest> rr = recoveryRequestService.findById(id);
        return rr.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada"));
    }

    // Buscar por token
    @GetMapping("/by-token/{token}")
    public ResponseEntity<?> getByToken(@PathVariable String token) {
        Optional<RecoveryRequest> rr = recoveryRequestService.findByToken(token);
        return rr.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada"));
    }

    // Buscar todas las solicitudes de un usuario
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<RecoveryRequest>> getByUser(@PathVariable int userID) {
        return ResponseEntity.ok(recoveryRequestService.findByUserID(userID));
    }

    // Crear una nueva solicitud de recuperación (devuelve el token generado)
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RecoveryRequestDTO dto) {
        String response = recoveryRequestService.createRecoveryRequest(dto);
        // Si el token tiene formato largo (token) es éxito, si es mensaje corto es error
        if (response.length() > 30) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Marcar una solicitud como usada
    @PostMapping("/use/{token}")
    public ResponseEntity<String> markAsUsed(@PathVariable String token) {
        String response = recoveryRequestService.markAsUsed(token);
        if (response.equals("Solicitud marcada como usada")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar una solicitud de recuperación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        String response = recoveryRequestService.deleteById(id);
        if (response.equals("Solicitud de recuperación eliminada")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
