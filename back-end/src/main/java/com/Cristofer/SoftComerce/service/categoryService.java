package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.CategoryDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Category;
import com.Cristofer.SoftComerce.repository.ICategory;

@Service
public class CategoryService {

    @Autowired
    private ICategory data;

    // ✅ Guardar categoría
    public ResponseDTO save(CategoryDTO categoryDTO) {
        if (!validateCategory(categoryDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la categoría inválidos");
        }

        Category categoryRegister = convertToModel(categoryDTO);
        data.save(categoryRegister);

        return new ResponseDTO(HttpStatus.OK.toString(), "Categoría guardada exitosamente");
    }

    // ✅ Obtener todas las categorías
    public List<Category> findAll() {
        return data.findAll(); // Devuelve todas las categorías
    }

    // ✅ Buscar categoría por ID
    public Optional<Category> findById(int id) {
        return data.findById(id);
    }

    // ✅ Actualizar categoría
    public ResponseDTO update(int id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = findById(id);
        if (!existingCategory.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "La categoría no existe");
        }

        if (!validateCategory(categoryDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la categoría inválidos");
        }

        Category categoryToUpdate = existingCategory.get();
        categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());

        data.save(categoryToUpdate);

        return new ResponseDTO(HttpStatus.OK.toString(), "✅ Categoría actualizada correctamente");
    }

    // ✅ Eliminar categoría
    public ResponseDTO deleteCategory(int id) {
        Optional<Category> category = findById(id);
        if (!category.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "La categoría no existe");
        }

        data.delete(category.get()); // Eliminación física de la categoría

        return new ResponseDTO(HttpStatus.OK.toString(), "Categoría eliminada correctamente");
    }

    // ✅ Filtrar categorías por nombre
    public List<Category> filterCategory(String categoryName) {
        List<Category> allCategories = data.findAll();

        if (categoryName == null || categoryName.isEmpty()) {
            return allCategories;
        }

        // Filtrar categorías que contengan el texto proporcionado (case insensitive)
        return allCategories.stream()
                .filter(c -> c.getCategoryName().toLowerCase().contains(categoryName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ✅ Validaciones
    private boolean validateCategory(CategoryDTO dto) {
        return dto.getCategoryName() != null && !dto.getCategoryName().isBlank();
    }

    // ✅ Conversión de DTO a modelo
    public Category convertToModel(CategoryDTO categoryDTO) {
        return new Category(
            0, // ID por defecto (autogenerado)
            categoryDTO.getCategoryName()
        );
    }

    // ✅ Conversión de modelo a DTO
    public CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
            category.getCategoryName()
        );
    }
}