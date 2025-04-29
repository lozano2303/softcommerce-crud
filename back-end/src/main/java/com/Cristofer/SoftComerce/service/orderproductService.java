package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    
    private static final Logger logger = LoggerFactory.getLogger(orderproductService.class);

    @Autowired
    private Iorderproduct orderproductRepository;

    @Autowired
    private Iorder orderRepository;

    @Autowired
    private Iproduct productRepository;
    
    @Autowired
    private orderService orderService;

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
        try {
            // Validar que la orden exista
            Optional<order> orderEntity = orderRepository.findById(orderproductDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new responseDTO("error", "Orden no encontrada");
            }

            // Validar que el producto exista
            Optional<product> productEntity = productRepository.findById(orderproductDTO.getProductID());
            if (!productEntity.isPresent()) {
                return new responseDTO("error", "Producto no encontrado");
            }

            // Convertir DTO a entidad y guardar
            orderproductId id = new orderproductId();
            id.setOrderID(orderproductDTO.getOrderID());
            id.setProductID(orderproductDTO.getProductID());

            orderproduct orderproduct = new orderproduct();
            orderproduct.setId(id);
            orderproduct.setOrder(orderEntity.get());
            orderproduct.setProduct(productEntity.get());
            orderproduct.setQuantity(orderproductDTO.getQuantity());
            
            // Calcular subtotal basado en precio del producto y cantidad
            double subtotal = productEntity.get().getPrice() * orderproductDTO.getQuantity();
            orderproduct.setSubtotal(subtotal);

            // Guardar la relación order-product
            orderproductRepository.save(orderproduct);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderproductDTO.getOrderID());

            logger.info("Nuevo producto agregado a la orden {} - Producto: {}, Cantidad: {}, Subtotal: {}", 
                orderproductDTO.getOrderID(), productEntity.get().getName(), 
                orderproductDTO.getQuantity(), subtotal);

            return new responseDTO("success", "Relación orden-producto registrada correctamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error de base de datos al guardar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error inesperado al guardar la relación");
        }
    }

    // Actualizar una relación por ID
    @Transactional
    public responseDTO update(orderproductId id, orderproductDTO orderproductDTO) {
        try {
            Optional<orderproduct> existingOrderProduct = findById(id);
            if (!existingOrderProduct.isPresent()) {
                return new responseDTO("error", "La relación no existe");
            }

            // Validar que la orden exista
            Optional<order> orderEntity = orderRepository.findById(orderproductDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new responseDTO("error", "Orden no encontrada");
            }

            // Validar que el producto exista
            Optional<product> productEntity = productRepository.findById(orderproductDTO.getProductID());
            if (!productEntity.isPresent()) {
                return new responseDTO("error", "Producto no encontrado");
            }

            // Actualizar datos de la relación
            orderproduct orderproductToUpdate = existingOrderProduct.get();
            orderproductToUpdate.setOrder(orderEntity.get());
            orderproductToUpdate.setProduct(productEntity.get());
            orderproductToUpdate.setQuantity(orderproductDTO.getQuantity());
            
            // Recalcular subtotal
            double subtotal = productEntity.get().getPrice() * orderproductDTO.getQuantity();
            orderproductToUpdate.setSubtotal(subtotal);

            // Guardar los cambios
            orderproductRepository.save(orderproductToUpdate);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderproductDTO.getOrderID());

            logger.info("Producto actualizado en la orden {} - Producto: {}, Nueva cantidad: {}, Nuevo subtotal: {}", 
                orderproductDTO.getOrderID(), productEntity.get().getName(), 
                orderproductDTO.getQuantity(), subtotal);

            return new responseDTO("success", "Relación orden-producto actualizada exitosamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error de base de datos al actualizar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error inesperado al actualizar la relación");
        }
    }

    // Eliminar una relación por ID
    @Transactional
    public responseDTO deleteById(orderproductId id) {
        try {
            Optional<orderproduct> existingOrderProduct = findById(id);
            if (!existingOrderProduct.isPresent()) {
                return new responseDTO("error", "Relación orden-producto no encontrada");
            }

            // Obtener el orderID antes de eliminar
            int orderId = existingOrderProduct.get().getOrder().getOrderID();
            
            // Eliminar la relación
            orderproductRepository.deleteById(id);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderId);

            logger.info("Producto eliminado de la orden {}", orderId);

            return new responseDTO("success", "Relación orden-producto eliminada correctamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error de base de datos al eliminar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar order-product: {}", e.getMessage());
            return new responseDTO("error", "Error inesperado al eliminar la relación");
        }
    }

    // Método para actualizar el totalPrice de una orden
    private void updateOrderTotalPrice(int orderId) {
        try {
            double totalPrice = orderService.calculateTotalPrice(orderId);
            orderService.updateOrderTotalPrice(orderId, totalPrice);
            logger.debug("TotalPrice actualizado para la orden {}: {}", orderId, totalPrice);
        } catch (Exception e) {
            logger.error("Error al actualizar totalPrice para la orden {}: {}", orderId, e.getMessage());
        }
    }

    // Convertir de orderproduct a orderproductDTO
    public orderproductDTO convertToDTO(orderproduct orderproduct) {
        return new orderproductDTO(
            orderproduct.getOrder().getOrderID(),
            orderproduct.getProduct().getProductID(),
            orderproduct.getProduct().getName(),
            orderproduct.getProduct().getPrice(),
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