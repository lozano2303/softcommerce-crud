package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.productDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.repository.Iproduct;

@Service
public class productService {

    @Autowired
    private Iproduct data;

    // ✅ Guardar producto
    public responseDTO save(productDTO productDTO) {
        if (!validateProduct(productDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del producto inválidos");
        }

        product product = convertToModel(productDTO);
        data.save(product);

        return new responseDTO(HttpStatus.OK.toString(), "Producto guardado exitosamente");
    }

    // ✅ Obtener todos los productos
    public List<product> findAll() {
        return data.findAll(); // O reemplaza por un método que devuelva solo productos activos si lo tienes
    }

    // ✅ Buscar producto por ID
    public Optional<product> findById(int id) {
        return data.findById(id);
    }

    // ✅ Eliminar (desactivar) producto
    public responseDTO deleteProduct(int id) {
        Optional<product> product = findById(id);
        if (!product.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El producto no existe");
        }

        return new responseDTO(HttpStatus.OK.toString(), "Producto eliminado correctamente");
    }

    // ✅ Actualizar producto
    public responseDTO update(int id, productDTO productDTO) {
        Optional<product> existingProduct = findById(id);
        if (!existingProduct.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El producto no existe");
        }

        if (!validateProduct(productDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del producto inválidos");
        }

        product productToUpdate = existingProduct.get();
        productToUpdate.setName(productDTO.getName());
        productToUpdate.setDescription(productDTO.getDescription());
        productToUpdate.setPrice(productDTO.getPrice());
        productToUpdate.setStock(productDTO.getStock());
        productToUpdate.setImageUrl(productDTO.getImageUrl());

        data.save(productToUpdate);

        return new responseDTO(HttpStatus.OK.toString(), "✅ Producto actualizado correctamente");
    }

    // ✅ Validaciones
    private boolean validateProduct(productDTO dto) {
        return dto.getName() != null && !dto.getName().isBlank() &&
                dto.getDescription() != null && !dto.getDescription().isBlank() &&
                dto.getPrice() >= 0 &&
                dto.getStock() >= 0;
    }

    // ✅ Conversión de DTO a modelo
    public product convertToModel(productDTO productDTO) {
        return new product(
            0,
            productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            productDTO.getStock(),
            LocalDateTime.now(),
            productDTO.getImageUrl(),
            true
        );
    }

    // ✅ Conversión de modelo a DTO
    public productDTO convertToDTO(product product) {
        return new productDTO(
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl()
        );
    }
}
