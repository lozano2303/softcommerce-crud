package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.categoryDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.category;
import com.Cristofer.SoftComerce.repository.Icategory;

@Service
public class categoryService {

    @Autowired
    private Icategory data;

    // ✅ Guardar categoría
    public responseDTO save(categoryDTO categoryDTO) {
        if (!validateCategory(categoryDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la categoría inválidos");
        }
    
        category categoryRegister = convertToModel(categoryDTO);
        data.save(categoryRegister);
    
        return new responseDTO(HttpStatus.OK.toString(), "Categoría guardada exitosamente");
    }

    // ✅ Obtener todas las categorías
    public List<category> findAll() {
        return data.findAll(); // Devuelve todas las categorías
    }

    // ✅ Buscar categoría por ID
    public Optional<category> findById(int id) {
        return data.findById(id);
    }

    // ✅ Actualizar categoría
    public responseDTO update(int id, categoryDTO categoryDTO) {
        Optional<category> existingCategory = findById(id);
        if (!existingCategory.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La categoría no existe");
        }

        if (!validateCategory(categoryDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la categoría inválidos");
        }

        category categoryToUpdate = existingCategory.get();
        categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());

        data.save(categoryToUpdate);

        return new responseDTO(HttpStatus.OK.toString(), "✅ Categoría actualizada correctamente");
    }

    // ✅ Eliminar categoría
    public responseDTO deleteCategory(int id) {
        Optional<category> category = findById(id);
        if (!category.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La categoría no existe");
        }

        data.delete(category.get()); // Eliminación física de la categoría

        return new responseDTO(HttpStatus.OK.toString(), "Categoría eliminada correctamente");
    }

    // ✅ Filtrar categorías por nombre
    public List<category> filterCategory(String categoryName) {
        List<category> allCategories = data.findAll();
        
        if (categoryName == null || categoryName.isEmpty()) {
            return allCategories;
        }
        
        // Filtrar categorías que contengan el texto proporcionado (case insensitive)
        return allCategories.stream()
                .filter(c -> c.getCategoryName().toLowerCase().contains(categoryName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ✅ Validaciones
    private boolean validateCategory(categoryDTO dto) {
        return dto.getCategoryName() != null && !dto.getCategoryName().isBlank();
    }

    // ✅ Conversión de DTO a modelo
    public category convertToModel(categoryDTO categoryDTO) {
        return new category(
            0, // ID por defecto (autogenerado)
            categoryDTO.getCategoryName()
        );
    }

    // ✅ Conversión de modelo a DTO
    public categoryDTO convertToDTO(category category) {
        return new categoryDTO(
            category.getCategoryName()
        );
    }
}