package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Iuser;

import java.time.LocalDateTime;

@Service
public class orderService {
    private static final Logger logger = LoggerFactory.getLogger(orderService.class);

    @Autowired
    private Iorder data;

    @Autowired
    private Iuser userRepository;

public void save(orderDTO orderDTO) {
    try {
        // Validar que el usuario exista
        if (!userRepository.existsById(orderDTO.getUserID())) {
            throw new IllegalArgumentException("Error: User does not exist");
        }

        // Convertir DTO a entidad y guardar
        order orderRegister = convertToModel(orderDTO);
        data.save(orderRegister);
        logger.info("Orden guardada exitosamente.");
    } catch (IllegalArgumentException e) {
        logger.warn("Validación fallida: {}", e.getMessage());
        throw e; // Re-lanzar para que se maneje a nivel superior si es necesario
    } catch (DataAccessException e) {
        logger.error("Error de base de datos al guardar la orden: {}", e.getMessage());
        throw new RuntimeException("Error de base de datos al guardar la orden.");
    } catch (Exception e) {
        logger.error("Error inesperado al guardar la orden: {}", e.getMessage());
        throw new RuntimeException("Error inesperado al guardar la orden.");
    }
}


    public orderDTO convertToDTO(order order) {
        try {
            return new orderDTO(
                order.getUserID(),
                order.getTotalPrice(),
                order.getCreatedAt()
            );
        } catch (Exception e) {
            logger.error("Error al convertir a DTO: {}", e.getMessage());
            throw new RuntimeException("Error al convertir a DTO.");
        }
    }

    public order convertToModel(orderDTO orderDTO) {
        try {
            return new order(
                0, // orderID autogenerado
                orderDTO.getUserID(),
                orderDTO.getTotalPrice(),
                true, // Convertir booleano
                LocalDateTime.now()
            );
        } catch (Exception e) {
            logger.error("Error al convertir a modelo: {}", e.getMessage());
            throw new RuntimeException("Error al convertir a modelo.");
        }
    }
}
