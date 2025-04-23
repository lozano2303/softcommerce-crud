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

import com.Cristofer.SoftComerce.DTO.paymentDTO;
import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.model.payment;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Ipayment;
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class paymentService {
    private static final Logger logger = LoggerFactory.getLogger(paymentService.class);

    @Autowired
    private Ipayment paymentRepository;

    @Autowired
    private Iuser userRepository;

    // Listar todos los pagos
    public List<payment> findAll() {
        return paymentRepository.findAll();
    }

    // Buscar pago por ID
    public Optional<payment> findById(int id) {
        return paymentRepository.findById(id);
    }

    // Verificar si un pago existe por ID
    public boolean existsById(int id) {
        return paymentRepository.existsById(id);
    }

    // Guardar pago con validaciones
    @Transactional
    public responseDTO save(paymentDTO paymentDTO) {
        // Validar que el usuario exista
        Optional<user> userEntity = userRepository.findById(paymentDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Crear y guardar el pago
            payment paymentEntity = new payment();
            paymentEntity.setUser(userEntity.get());
            paymentEntity.setAmount(paymentDTO.getAmount());
            paymentEntity.setMethod(paymentDTO.getMethod());
            paymentEntity.setStatus(true); // Estado inicial como true
            paymentEntity.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(paymentEntity);
            return new responseDTO(HttpStatus.OK.toString(), "Pago registrado correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Actualizar pago por ID
    @Transactional
    public responseDTO update(int id, paymentDTO paymentDTO) {
        Optional<payment> existingPayment = findById(id);
        if (!existingPayment.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Pago no encontrado");
        }

        Optional<user> userEntity = userRepository.findById(paymentDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Actualizar datos del pago
            payment paymentToUpdate = existingPayment.get();
            paymentToUpdate.setUser(userEntity.get());
            paymentToUpdate.setAmount(paymentDTO.getAmount());
            paymentToUpdate.setMethod(paymentDTO.getMethod());
            paymentToUpdate.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(paymentToUpdate);
            return new responseDTO(HttpStatus.OK.toString(), "Pago actualizado exitosamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Eliminar pago por ID
    @Transactional
    public responseDTO deleteById(int id) {
        Optional<payment> paymentEntity = findById(id);
        if (!paymentEntity.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Pago no encontrado");
        }

        try {
            paymentRepository.deleteById(id);
            return new responseDTO(HttpStatus.OK.toString(), "Pago eliminado correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al eliminar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar el pago: {}", e.getMessage());
            return new responseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

// Convertir entidad a DTO
public paymentDTO convertToDTO(payment paymentEntity) {
    return new paymentDTO(
        paymentEntity.getUser().getUserID(), // Obteniendo el ID del usuario
        paymentEntity.getAmount(),
        paymentEntity.getMethod()
    );
}

// Convertir DTO a entidad
public payment convertToModel(paymentDTO paymentDTO) {
    Optional<user> userEntity = userRepository.findById(paymentDTO.getUserID());
    if (!userEntity.isPresent()) {
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    return new payment(
        0, // ID autogenerado
        userEntity.get(),
        paymentDTO.getAmount(),
        paymentDTO.getMethod(),
        true, // Estado inicial como true
        LocalDateTime.now()
    );
}
}