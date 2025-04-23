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

import com.Cristofer.SoftComerce.DTO.categoryDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.category;
import com.Cristofer.SoftComerce.service.categoryService;

@RestController
@RequestMapping("/api/v1/category")
public class categoryController {

    @Autowired
    private categoryService categoryService;

    // ✅ Crear categoría
    @PostMapping("/")
    public ResponseEntity<Object> registerCategory(@RequestBody categoryDTO categoryDTO) {
        responseDTO response = categoryService.save(categoryDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ Obtener todas las categorías
    @GetMapping("/")
    public ResponseEntity<Object> getAllCategories() {
        List<category> categories = categoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // ✅ Obtener una categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable int id) {
        var category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterCategory(
        @RequestParam(required = false, name = "categoryName") String categoryName) {
    
    var categoryList = categoryService.filterCategory(categoryName);
    return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    // ✅ Actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable int id, @RequestBody categoryDTO categoryDTO) {
        responseDTO response = categoryService.update(id, categoryDTO);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ✅ Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable int id) {
        responseDTO response = categoryService.deleteCategory(id);
        if (response.getStatus().equals(HttpStatus.OK.toString())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}