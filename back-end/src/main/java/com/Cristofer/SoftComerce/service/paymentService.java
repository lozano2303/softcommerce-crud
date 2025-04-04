package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Cristofer.SoftComerce.DTO.paymentDTO;
import com.Cristofer.SoftComerce.model.payment;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Ipayment;
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class paymentService {
    private static final Logger logger = LoggerFactory.getLogger(paymentService.class);

    @Autowired
    private Ipayment data;

    @Autowired
    private Iuser userRepository;

    public void save(paymentDTO paymentDTO) {
        try {
            payment paymentRegister = convertToModel(paymentDTO);
            data.save(paymentRegister);
            logger.info("Pago guardado exitosamente.");
        } catch (Exception e) {
            logger.error("Error al guardar el pago: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el pago.");
        }
    }

    public paymentDTO convertToDTO(payment payment) {
        try {
            return new paymentDTO(
                payment.getUser().getUserID(),
                payment.getAmount(),
                payment.getMethod()
            );
        } catch (Exception e) {
            logger.error("Error al convertir a DTO: {}", e.getMessage());
            throw new RuntimeException("Error al convertir a DTO.");
        }
    }

    public payment convertToModel(paymentDTO paymentDTO) {
        try {
            user user = findUserById(paymentDTO.getUserID());
            return new payment(
                0,
                user,
                paymentDTO.getAmount(),
                paymentDTO.getMethod(),
                true,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            logger.error("Error al convertir a modelo: {}", e.getMessage());
            throw new RuntimeException("Error al convertir a modelo.");
        }
    }

    private user findUserById(int userID) {
        try {
            return userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userID));
        } catch (Exception e) {
            logger.error("Error al buscar usuario con ID {}: {}", userID, e.getMessage());
            throw new RuntimeException("Error al buscar usuario.");
        }
    }
}
