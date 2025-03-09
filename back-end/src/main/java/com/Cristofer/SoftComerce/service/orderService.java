package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.orderDTO;
import com.Cristofer.SoftComerce.model.order;
import com.Cristofer.SoftComerce.repository.Iorder;
import com.Cristofer.SoftComerce.repository.Iuser;

import java.time.LocalDateTime;

@Service
public class orderService {
    
    @Autowired
    private Iorder data;

    @Autowired
    private Iuser userRepository; // Repositorio de usuarios

    public void save(orderDTO orderDTO) {
        // Validar que el usuario exista
        boolean userExists = userRepository.existsById(orderDTO.getUserID());
        if (!userExists) {
            throw new IllegalArgumentException("Error: User does not exist");
        }

        // Convertir DTO a entidad y guardar
        order orderRegister = convertToModel(orderDTO);
        data.save(orderRegister);
    }

    public orderDTO convertToDTO(order order) {
        return new orderDTO(
            order.getUserID(),
            order.getTotalPrice(),
            order.getStatus(),
            order.getCreatedAt() // Asegúrate de que este método existe en order
        );
    }
    

    public order convertToModel(orderDTO orderDTO) {
        return new order(
            0, // orderID autogenerado
            orderDTO.getUserID(),
            orderDTO.getTotalPrice(),
            orderDTO.getStatus(),
            LocalDateTime.now() // Fecha de creación automática
        );
    }
}
