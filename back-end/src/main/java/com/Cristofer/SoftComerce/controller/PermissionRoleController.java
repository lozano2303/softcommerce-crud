package com.Cristofer.SoftComerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Cristofer.SoftComerce.model.PermissionRole;
import com.Cristofer.SoftComerce.DTO.PermissionRoleDTO;
import com.Cristofer.SoftComerce.service.PermissionRoleService;

@RestController
@RequestMapping("/api/permission-roles")
public class PermissionRoleController {

    @Autowired
    private PermissionRoleService permissionRoleService;

    // Obtener todos los permisos
    @GetMapping
    public ResponseEntity<List<PermissionRole>> getAll() {
        return ResponseEntity.ok(permissionRoleService.findAll());
    }

    // Obtener permiso por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<PermissionRole> pr = permissionRoleService.findById(id);
        return pr.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permiso no encontrado"));
    }

    // Obtener permisos por PageRoleID
    @GetMapping("/page-role/{pageRoleID}")
    public ResponseEntity<List<PermissionRole>> getByPageRoleID(@PathVariable int pageRoleID) {
        return ResponseEntity.ok(permissionRoleService.findByPageRoleID(pageRoleID));
    }

    // Obtener permisos por tipo
    @GetMapping("/permission-type/{permissionType}")
    public ResponseEntity<List<PermissionRole>> getByPermissionType(@PathVariable String permissionType) {
        return ResponseEntity.ok(permissionRoleService.findByPermissionType(permissionType));
    }

    // Registrar nuevo permiso
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PermissionRoleDTO dto) {
        String response = permissionRoleService.register(dto);
        if (response.equals("Permiso registrado correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Actualizar permiso
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody PermissionRoleDTO dto) {
        String response = permissionRoleService.update(id, dto);
        if (response.equals("Permiso actualizado correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar permiso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        String response = permissionRoleService.deleteById(id);
        if (response.equals("Permiso eliminado correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}