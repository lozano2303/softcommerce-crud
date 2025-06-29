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

import com.Cristofer.SoftComerce.model.Page;
import com.Cristofer.SoftComerce.DTO.PageDTO;
import com.Cristofer.SoftComerce.service.PageService;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    @Autowired
    private PageService pageService;

    // Obtener todas las páginas
    @GetMapping
    public ResponseEntity<List<Page>> getAllPages() {
        return ResponseEntity.ok(pageService.findAll());
    }

    // Obtener página por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable int id) {
        Optional<Page> page = pageService.findById(id);
        return page.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Página no encontrada"));
    }

    // Registrar página
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PageDTO pageDTO) {
        String response = pageService.register(pageDTO);
        if (response.equals("Página registrada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Actualizar página
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePage(@PathVariable int id, @RequestBody PageDTO pageDTO) {
        String response = pageService.update(id, pageDTO);
        if (response.equals("Página actualizada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar página
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePage(@PathVariable int id) {
        String response = pageService.deleteById(id);
        if (response.equals("Página eliminada correctamente")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
