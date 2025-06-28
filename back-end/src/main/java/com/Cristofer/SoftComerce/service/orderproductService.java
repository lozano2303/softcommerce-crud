package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.OrderProductDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Order;
import com.Cristofer.SoftComerce.model.OrderProduct;
import com.Cristofer.SoftComerce.model.OrderProductId;
import com.Cristofer.SoftComerce.model.Product;
import com.Cristofer.SoftComerce.repository.IOrder;
import com.Cristofer.SoftComerce.repository.IOrderProduct;
import com.Cristofer.SoftComerce.repository.IProduct;

@Service
public class OrderProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProductService.class);

    @Autowired
    private IOrderProduct orderProductRepository;

    @Autowired
    private IOrder orderRepository;

    @Autowired
    private IProduct productRepository;
    
    @Autowired
    private OrderService orderService;

    // Listar todas las relaciones order-product
    public List<OrderProduct> findAll() {
        return orderProductRepository.findAll();
    }

    // Buscar una relación por ID
    public Optional<OrderProduct> findById(OrderProductId id) {
        return orderProductRepository.findById(id);
    }

    // Método para filtrar relaciones order-product
    public List<OrderProduct> filterOrderProducts(Integer orderID, Integer productID, Integer quantity, Double subtotal) {
        return orderProductRepository.filterOrderProducts(orderID, productID, quantity, subtotal);
    }

    // Guardar una relación order-product con validaciones
    @Transactional
    public ResponseDTO save(OrderProductDTO orderProductDTO) {
        try {
            // Validar que la orden exista
            Optional<Order> orderEntity = orderRepository.findById(orderProductDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new ResponseDTO("error", "Orden no encontrada");
            }

            // Validar que el producto exista
            Optional<Product> productEntity = productRepository.findById(orderProductDTO.getProductID());
            if (!productEntity.isPresent()) {
                return new ResponseDTO("error", "Producto no encontrado");
            }

            // Convertir DTO a entidad y guardar
            OrderProductId id = new OrderProductId();
            id.setOrderID(orderProductDTO.getOrderID());
            id.setProductID(orderProductDTO.getProductID());

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(id);
            orderProduct.setOrder(orderEntity.get());
            orderProduct.setProduct(productEntity.get());
            orderProduct.setQuantity(orderProductDTO.getQuantity());
            
            // Calcular subtotal basado en precio del producto y cantidad
            double subtotal = productEntity.get().getPrice() * orderProductDTO.getQuantity();
            orderProduct.setSubtotal(subtotal);

            // Guardar la relación order-product
            orderProductRepository.save(orderProduct);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderProductDTO.getOrderID());

            logger.info("Nuevo producto agregado a la orden {} - Producto: {}, Cantidad: {}, Subtotal: {}", 
                orderProductDTO.getOrderID(), productEntity.get().getName(), 
                orderProductDTO.getQuantity(), subtotal);

            return new ResponseDTO("success", "Relación orden-producto registrada correctamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error de base de datos al guardar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error inesperado al guardar la relación");
        }
    }

    // Actualizar una relación por ID
    @Transactional
    public ResponseDTO update(OrderProductId id, OrderProductDTO orderProductDTO) {
        try {
            Optional<OrderProduct> existingOrderProduct = findById(id);
            if (!existingOrderProduct.isPresent()) {
                return new ResponseDTO("error", "La relación no existe");
            }

            // Validar que la orden exista
            Optional<Order> orderEntity = orderRepository.findById(orderProductDTO.getOrderID());
            if (!orderEntity.isPresent()) {
                return new ResponseDTO("error", "Orden no encontrada");
            }

            // Validar que el producto exista
            Optional<Product> productEntity = productRepository.findById(orderProductDTO.getProductID());
            if (!productEntity.isPresent()) {
                return new ResponseDTO("error", "Producto no encontrado");
            }

            // Actualizar datos de la relación
            OrderProduct orderProductToUpdate = existingOrderProduct.get();
            orderProductToUpdate.setOrder(orderEntity.get());
            orderProductToUpdate.setProduct(productEntity.get());
            orderProductToUpdate.setQuantity(orderProductDTO.getQuantity());
            
            // Recalcular subtotal
            double subtotal = productEntity.get().getPrice() * orderProductDTO.getQuantity();
            orderProductToUpdate.setSubtotal(subtotal);

            // Guardar los cambios
            orderProductRepository.save(orderProductToUpdate);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderProductDTO.getOrderID());

            logger.info("Producto actualizado en la orden {} - Producto: {}, Nueva cantidad: {}, Nuevo subtotal: {}", 
                orderProductDTO.getOrderID(), productEntity.get().getName(), 
                orderProductDTO.getQuantity(), subtotal);

            return new ResponseDTO("success", "Relación orden-producto actualizada exitosamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error de base de datos al actualizar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error inesperado al actualizar la relación");
        }
    }

    // Eliminar una relación por ID
    @Transactional
    public ResponseDTO deleteById(OrderProductId id) {
        try {
            Optional<OrderProduct> existingOrderProduct = findById(id);
            if (!existingOrderProduct.isPresent()) {
                return new ResponseDTO("error", "Relación orden-producto no encontrada");
            }

            // Obtener el orderID antes de eliminar
            int orderId = existingOrderProduct.get().getOrder().getOrderID();
            
            // Eliminar la relación
            orderProductRepository.deleteById(id);
            
            // Actualizar el totalPrice de la orden relacionada
            updateOrderTotalPrice(orderId);

            logger.info("Producto eliminado de la orden {}", orderId);

            return new ResponseDTO("success", "Relación orden-producto eliminada correctamente");

        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error de base de datos al eliminar la relación");
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar order-product: {}", e.getMessage());
            return new ResponseDTO("error", "Error inesperado al eliminar la relación");
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

    // Convertir de OrderProduct a OrderProductDTO
    public OrderProductDTO convertToDTO(OrderProduct orderProduct) {
        return new OrderProductDTO(
            orderProduct.getOrder().getOrderID(),
            orderProduct.getProduct().getProductID(),
            orderProduct.getProduct().getName(),
            orderProduct.getProduct().getPrice(),
            orderProduct.getQuantity(),
            orderProduct.getSubtotal()
        );
    }

    // Convertir de OrderProductDTO a OrderProduct
    public OrderProduct convertToModel(OrderProductDTO orderProductDTO) {
        OrderProductId id = new OrderProductId();
        id.setOrderID(orderProductDTO.getOrderID());
        id.setProductID(orderProductDTO.getProductID());

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(id);
        orderProduct.setQuantity(orderProductDTO.getQuantity());
        orderProduct.setSubtotal(orderProductDTO.getSubtotal());

        return orderProduct;
    }
}