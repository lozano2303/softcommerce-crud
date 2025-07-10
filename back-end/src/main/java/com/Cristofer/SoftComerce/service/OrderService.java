package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.OrderDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Order;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IOrder;
import com.Cristofer.SoftComerce.repository.IOrderProduct;
import com.Cristofer.SoftComerce.repository.IUser;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private IOrder orderRepository;

    @Autowired
    private IUser userRepository;

    @Autowired
    private IOrderProduct orderProductRepository;

    // Listar todas las órdenes
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByUserName(String name) {
        return orderRepository.findByUserName(name);
    }

    // Buscar orden por ID
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    // Verificar si una orden existe por ID
    public boolean existsById(int id) {
        return orderRepository.existsById(id);
    }

    // Guardar orden con validaciones
    @Transactional
    public ResponseDTO save(OrderDTO orderDTO) {
        // Validar que el usuario exista
        Optional<User> userEntity = userRepository.findById(orderDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Crear y guardar la orden
            Order orderEntity = new Order();
            orderEntity.setUserID(userEntity.get());
            orderEntity.setStatus(false); // Estado inicial como false (ajustar según lógica de negocio)
            orderEntity.setCreatedAt(LocalDateTime.now());

            // Guardar la orden
            Order savedOrder = orderRepository.save(orderEntity);

            // Calcular el totalPrice dinámicamente
            double totalPrice = calculateTotalPrice(savedOrder.getOrderID());
            logger.info("Orden creada con totalPrice calculado: {}", totalPrice);

            return new ResponseDTO(HttpStatus.OK.toString(), "Orden registrada correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Actualizar orden por ID
    @Transactional
    public ResponseDTO update(int id, OrderDTO orderDTO) {
        Optional<Order> existingOrder = findById(id);
        if (!existingOrder.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Orden no encontrada");
        }

        Optional<User> userEntity = userRepository.findById(orderDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Actualizar datos de la orden
            Order orderToUpdate = existingOrder.get();
            orderToUpdate.setUserID(userEntity.get());
            orderToUpdate.setCreatedAt(LocalDateTime.now());

            orderRepository.save(orderToUpdate);

            // Calcular el totalPrice dinámicamente
            double totalPrice = calculateTotalPrice(orderToUpdate.getOrderID());
            logger.info("Orden actualizada con totalPrice calculado: {}", totalPrice);

            return new ResponseDTO(HttpStatus.OK.toString(), "Orden actualizada exitosamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Eliminar una orden por ID
    @Transactional
    public ResponseDTO deleteById(int id) {
        Optional<Order> orderEntity = findById(id);
        if (!orderEntity.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Orden no encontrada");
        }

        try {
            orderRepository.deleteById(id);
            return new ResponseDTO(HttpStatus.OK.toString(), "Orden eliminada correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la orden: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Método para actualizar solo el totalPrice de una orden
    @Transactional
    public void updateOrderTotalPrice(int orderId, double totalPrice) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order orderToUpdate = orderOpt.get();
            orderToUpdate.setTotalPrice(totalPrice);
            orderRepository.save(orderToUpdate);
            logger.info("TotalPrice actualizado para orden {}: {}", orderId, totalPrice);
        }
    }

    // Calcular el precio total de una orden (ya existente)
    public double calculateTotalPrice(int orderID) {
        List<Double> subtotals = orderProductRepository.findSubTotalsByOrderID(orderID);
        return subtotals.stream().mapToDouble(Double::doubleValue).sum();
    }
}