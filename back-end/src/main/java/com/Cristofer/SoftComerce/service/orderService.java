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

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Iorderproduct; // Cambiado a Iorderproduct
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class orderService {
    private static final Logger logger = LoggerFactory.getLogger(orderService.class);

    @Autowired
    private Iorder orderRepository;

    @Autowired
    private Iuser userRepository;

    @Autowired
    private Iorderproduct orderProductRepository; // Cambiado a Iorderproduct

    // Listar todas las órdenes
    public List<order> findAll() {
        return orderRepository.findAll();
    }

    
    public List<order> findByUserName(String name) {
        return orderRepository.findByUserName(name);
    }

    // Buscar orden por ID
    public Optional<order> findById(int id) {
        return orderRepository.findById(id);
    }

    // Verificar si una orden existe por ID
    public boolean existsById(int id) {
        return orderRepository.existsById(id);
    }

    // Guardar orden con validaciones
    @Transactional
    public responseDTO save(orderDTO orderDTO) {
        // Validar que el usuario exista
        Optional<user> userEntity = userRepository.findById(orderDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Crear y guardar la orden
            order orderEntity = new order();
            orderEntity.setUserID(userEntity.get());
            orderEntity.setStatus(false); // Estado inicial como true
            orderEntity.setCreatedAt(LocalDateTime.now());

            // Guardar la orden
            order savedOrder = orderRepository.save(orderEntity);

            // Calcular el totalPrice dinámicamente
            double totalPrice = calculateTotalPrice(savedOrder.getOrderID());
            logger.info("Orden creada con totalPrice calculado: {}", totalPrice);

            return new responseDTO(HttpStatus.OK.toString(), "Orden registrada correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Actualizar orden por ID
    @Transactional
    public responseDTO update(int id, orderDTO orderDTO) {
        Optional<order> existingOrder = findById(id);
        if (!existingOrder.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Orden no encontrada");
        }

        Optional<user> userEntity = userRepository.findById(orderDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Actualizar datos de la orden
            order orderToUpdate = existingOrder.get();
            orderToUpdate.setUserID(userEntity.get());
            orderToUpdate.setCreatedAt(LocalDateTime.now());

            orderRepository.save(orderToUpdate);

            // Calcular el totalPrice dinámicamente
            double totalPrice = calculateTotalPrice(orderToUpdate.getOrderID());
            logger.info("Orden actualizada con totalPrice calculado: {}", totalPrice);

            return new responseDTO(HttpStatus.OK.toString(), "Orden actualizada exitosamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Eliminar una orden por ID
    @Transactional
    public responseDTO deleteById(int id) {
        Optional<order> orderEntity = findById(id);
        if (!orderEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Orden no encontrada");
        }

        try {
            orderRepository.deleteById(id);
            return new responseDTO(HttpStatus.OK.toString(), "Orden eliminada correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la orden: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Calcular el precio total de una orden
    public double calculateTotalPrice(int orderID) {
        // Obtener subtotales de los productos relacionados con la orden
        List<Double> subtotals = orderProductRepository.findSubTotalsByOrderID(orderID);

        // Sumar los subtotales para obtener el totalPrice
        return subtotals.stream().mapToDouble(Double::doubleValue).sum();
    }
}