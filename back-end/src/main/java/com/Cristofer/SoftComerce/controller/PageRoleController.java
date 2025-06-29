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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.model.PageRole;
import com.Cristofer.SoftComerce.DTO.PageRoleDTO;
import com.Cristofer.SoftComerce.service.PageRoleService;

@RestController
@RequestMapping("/api/page-roles")
public class PageRoleController {

    @Autowired
    private PageRoleService pageRoleService;

    // Obtener todas las relaciones Página-Rol
    @GetMapping
    public ResponseEntity<List<PageRole>> getAll() {
        return ResponseEntity.ok(pageRoleService.findAll());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<PageRole> pr = pageRoleService.findById(id);
        return pr.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Relación no encontrada"));
    }

    // Obtener todas las relaciones por PageID
    @GetMapping("/page/{pageID}")
    public ResponseEntity<List<PageRole>> getByPageID(@PathVariable int pageID) {
        return ResponseEntity.ok(pageRoleService.findByPageID(pageID));
    }

    // Obtener todas las relaciones por RoleID
    @GetMapping("/role/{roleID}")
    public ResponseEntity<List<PageRole>> getByRoleID(@PathVariable int roleID) {
        return ResponseEntity.ok(pageRoleService.findByRoleID(roleID));
    }

    // Registrar nueva relación
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PageRoleDTO dto) {
        String response = pageRoleService.register(dto);
        if (response.equals("Relación Página-Rol registrada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Actualizar relación
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody PageRoleDTO dto) {
        String response = pageRoleService.update(id, dto);
        if (response.equals("Relación Página-Rol actualizada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar relación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        String response = pageRoleService.deleteById(id);
        if (response.equals("Relación Página-Rol eliminada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}