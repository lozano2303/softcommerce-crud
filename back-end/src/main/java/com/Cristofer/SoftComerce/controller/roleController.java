package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.roleDTO;
import com.Cristofer.SoftComerce.service.roleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class roleController {

    @Autowired
    private roleService roleService;

    // Registrar un nuevo rol
    @PostMapping("/")
    public ResponseEntity<Object> registerRole(@RequestBody roleDTO roleDTO) {
        responseDTO response = roleService.save(roleDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los roles
    @GetMapping("/")
    public ResponseEntity<Object> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    // Obtener un rol por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleById(@PathVariable int id) {
        var role = roleService.findById(id);
        if (!role.isPresent()) {
            return new ResponseEntity<>("Rol no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(role.get(), HttpStatus.OK);
    }

    // Eliminar un rol por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable int id) {
        responseDTO response = roleService.deleteById(id);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar un rol por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable int id, @RequestBody roleDTO roleDTO) {
        responseDTO response = roleService.update(id, roleDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}