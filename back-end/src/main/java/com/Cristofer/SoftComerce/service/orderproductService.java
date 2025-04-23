package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.orderproductDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.orderproduct;
import com.Cristofer.SoftComerce.model.orderproductId;
import com.Cristofer.SoftComerce.model.product;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Iorderproduct;
import com.Cristofer.SoftComerce.repository.Iproduct;

@Service
public class orderproductService {

    @Autowired
    private Iorderproduct orderproductRepository;

    @Autowired
    private Iorder orderRepository;

    @Autowired
    private Iproduct productRepository;

    // Listar todas las relaciones order-product
    public List<orderproduct> findAll() {
        return orderproductRepository.findAll();
    }

    // Buscar una relación por ID
    public Optional<orderproduct> findById(orderproductId id) {
        return orderproductRepository.findById(id);
    }

        // Método para filtrar relaciones order-product
        public List<orderproduct> filterOrderProducts(Integer orderID, Integer productID, Integer quantity, Double subtotal) {
            return orderproductRepository.filterOrderProducts(orderID, productID, quantity, subtotal);
        }

    // Guardar una relación order-product con validaciones
    @Transactional
    public responseDTO save(orderproductDTO orderproductDTO) {
        // Validar que la orden exista
        Optional<order> orderEntity = orderRepository.findById(orderproductDTO.getOrderID());
        if (!orderEntity.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        // Validar que el producto exista
        Optional<product> productEntity = productRepository.findById(orderproductDTO.getProductID());
        if (!productEntity.isPresent()) {
            return new responseDTO("Producto no encontrado", "error");
        }

        try {
            // Convertir DTO a entidad y guardar
            orderproductId id = new orderproductId();
            id.setOrderID(orderproductDTO.getOrderID());
            id.setProductID(orderproductDTO.getProductID());

            orderproduct orderproduct = new orderproduct();
            orderproduct.setId(id);
            orderproduct.setOrder(orderEntity.get());
            orderproduct.setProduct(productEntity.get());
            orderproduct.setQuantity(orderproductDTO.getQuantity());
            orderproduct.setSubtotal(orderproductDTO.getSubtotal());

            orderproductRepository.save(orderproduct);

            return new responseDTO("Relación orden-producto registrada correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al guardar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al guardar la relación", "error");
        }
    }

    // Actualizar una relación por ID
    @Transactional
    public responseDTO update(orderproductId id, orderproductDTO orderproductDTO) {
        Optional<orderproduct> existingOrderProduct = findById(id);
        if (!existingOrderProduct.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La relación no existe");
        }

        // Validar que la orden exista
        Optional<order> orderEntity = orderRepository.findById(orderproductDTO.getOrderID());
        if (!orderEntity.isPresent()) {
            return new responseDTO("Orden no encontrada", "error");
        }

        // Validar que el producto exista
        Optional<product> productEntity = productRepository.findById(orderproductDTO.getProductID());
        if (!productEntity.isPresent()) {
            return new responseDTO("Producto no encontrado", "error");
        }

        try {
            // Actualizar datos de la relación
            orderproduct orderproductToUpdate = existingOrderProduct.get();
            orderproductToUpdate.setOrder(orderEntity.get());
            orderproductToUpdate.setProduct(productEntity.get());
            orderproductToUpdate.setQuantity(orderproductDTO.getQuantity());
            orderproductToUpdate.setSubtotal(orderproductDTO.getSubtotal());

            orderproductRepository.save(orderproductToUpdate);

            return new responseDTO("Relación orden-producto actualizada exitosamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al actualizar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al actualizar la relación", "error");
        }
    }

    // Eliminar una relación por ID
    @Transactional
    public responseDTO deleteById(orderproductId id) {
        Optional<orderproduct> existingOrderProduct = findById(id);
        if (!existingOrderProduct.isPresent()) {
            return new responseDTO("Relación orden-producto no encontrada", "error");
        }

        try {
            orderproductRepository.deleteById(id);
            return new responseDTO("Relación orden-producto eliminada correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al eliminar la relación", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al eliminar la relación", "error");
        }
    }

    // Convertir de orderproduct a orderproductDTO
    public orderproductDTO convertToDTO(orderproduct orderproduct) {
        return new orderproductDTO(
            orderproduct.getOrder().getOrderID(),
            orderproduct.getProduct().getProductID(),
            orderproduct.getQuantity(),
            orderproduct.getSubtotal()
        );
    }

    // Convertir de orderproductDTO a orderproduct
    public orderproduct convertToModel(orderproductDTO orderproductDTO) {
        orderproductId id = new orderproductId();
        id.setOrderID(orderproductDTO.getOrderID());
        id.setProductID(orderproductDTO.getProductID());

        orderproduct orderproduct = new orderproduct();
        orderproduct.setId(id);
        orderproduct.setQuantity(orderproductDTO.getQuantity());
        orderproduct.setSubtotal(orderproductDTO.getSubtotal());

        return orderproduct;
    }
}