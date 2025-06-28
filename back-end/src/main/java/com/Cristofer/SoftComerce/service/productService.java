package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.ProductDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Category;
import com.Cristofer.SoftComerce.model.Product;
import com.Cristofer.SoftComerce.repository.ICategory;
import com.Cristofer.SoftComerce.repository.IProduct;

@Service
public class ProductService {

    @Autowired
    private IProduct productRepository;

    @Autowired
    private ICategory categoryRepository;

    // ✅ Guardar producto
    public ResponseDTO save(ProductDTO productDTO) {
        if (!validateProduct(productDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del producto inválidos");
        }

        // Obtener la categoría desde el repositorio
        Optional<Category> categoryOptional = categoryRepository.findById(productDTO.getCategoryID());
        if (!categoryOptional.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Categoría no encontrada");
        }

        Product product = convertToModel(productDTO, categoryOptional.get());
        productRepository.save(product);

        return new ResponseDTO(HttpStatus.OK.toString(), "Producto guardado exitosamente");
    }

    // ✅ Obtener todos los productos
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> filterProducts(String name, String category, Double price, Boolean status, Integer stock) {
        return productRepository.filterProducts(name, category, price, status, stock);
    }

    // ✅ Buscar producto por ID
    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    // ✅ Eliminar (desactivar) producto
    public ResponseDTO deleteProduct(int id) {
        Optional<Product> product = findById(id);
        if (!product.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "El producto no existe");
        }

        Product productToDelete = product.get();
        productToDelete.setStatus(false);
        productRepository.save(productToDelete);

        return new ResponseDTO(HttpStatus.OK.toString(), "Producto eliminado correctamente");
    }

    // ✅ Reactivar producto
    public ResponseDTO updateProductStatus(int id, boolean status) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseDTO("400 BAD_REQUEST", "El producto no existe");
        }

        Product productToUpdate = optionalProduct.get();
        productToUpdate.setStatus(status);
        productRepository.save(productToUpdate);

        String message = status ? "Producto activado correctamente" : "Producto desactivado correctamente";
        return new ResponseDTO("200 OK", message);
    }

    // ✅ Actualizar producto
    public ResponseDTO update(int id, ProductDTO productDTO) {
        // Depuración: imprimir el objeto recibido
        System.out.println("Datos recibidos en la solicitud de actualización: " + productDTO);

        Optional<Product> existingProduct = findById(id);
        if (!existingProduct.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "El producto no existe");
        }

        if (!validateProduct(productDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del producto inválidos");
        }

        // Obtener la categoría desde el repositorio
        Optional<Category> categoryOptional = categoryRepository.findById(productDTO.getCategoryID());
        if (!categoryOptional.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Categoría no encontrada");
        }

        Product productToUpdate = existingProduct.get();
        productToUpdate.setName(productDTO.getName());
        productToUpdate.setDescription(productDTO.getDescription());
        productToUpdate.setPrice(productDTO.getPrice());
        productToUpdate.setStock(productDTO.getStock());
        productToUpdate.setImageUrl(productDTO.getImageUrl()); // Asegúrate de que este campo se esté mapeando correctamente
        productToUpdate.setCategory(categoryOptional.get());

        productRepository.save(productToUpdate);

        return new ResponseDTO(HttpStatus.OK.toString(), "✅ Producto actualizado correctamente");
    }

    // ✅ Validaciones
    private boolean validateProduct(ProductDTO dto) {
        return dto.getName() != null && !dto.getName().isBlank() &&
                dto.getDescription() != null && !dto.getDescription().isBlank() &&
                dto.getPrice() >= 0 &&
                dto.getStock() >= 0 &&
                dto.getCategoryID() > 0;
    }

    // ✅ Conversión de DTO a modelo
    public Product convertToModel(ProductDTO productDTO, Category category) {
        return new Product(
            0,
            productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            productDTO.getStock(),
            LocalDateTime.now(),
            productDTO.getImageUrl(),
            true,
            category
        );
    }

    // ✅ Conversión de modelo a DTO
    public ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            product.getCategory().getCategoryID()
        );
    }
}