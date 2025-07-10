package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.PaymentDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.model.Payment;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IPayment;
import com.Cristofer.SoftComerce.repository.IUser;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private IPayment paymentRepository;

    @Autowired
    private IUser userRepository;

    // Listar todos los pagos
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    // Buscar pago por ID
    public Optional<Payment> findById(int id) {
        return paymentRepository.findById(id);
    }

    // Verificar si un pago existe por ID
    public boolean existsById(int id) {
        return paymentRepository.existsById(id);
    }
    
    public List<PaymentDTO> filterPaymentsByMethod(String method) {
        List<Payment> payments = paymentRepository.findByMethod(method);
        return payments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Guardar pago con validaciones
    @Transactional
    public ResponseDTO save(PaymentDTO paymentDTO) {
        // Validar que el usuario exista
        Optional<User> userEntity = userRepository.findById(paymentDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Crear y guardar el pago
            Payment paymentEntity = new Payment();
            paymentEntity.setUser(userEntity.get());
            paymentEntity.setAmount(paymentDTO.getAmount());
            paymentEntity.setMethod(paymentDTO.getMethod());
            paymentEntity.setStatus(true); // Estado inicial como true
            paymentEntity.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(paymentEntity);
            return new ResponseDTO(HttpStatus.OK.toString(), "Pago registrado correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al guardar el pago: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al guardar el pago: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Actualizar pago por ID
    @Transactional
    public ResponseDTO update(int id, PaymentDTO paymentDTO) {
        Optional<Payment> existingPayment = findById(id);
        if (!existingPayment.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Pago no encontrado");
        }

        Optional<User> userEntity = userRepository.findById(paymentDTO.getUserID());
        if (!userEntity.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Usuario no encontrado");
        }

        try {
            // Actualizar datos del pago
            Payment paymentToUpdate = existingPayment.get();
            paymentToUpdate.setUser(userEntity.get());
            paymentToUpdate.setAmount(paymentDTO.getAmount());
            paymentToUpdate.setMethod(paymentDTO.getMethod());
            paymentToUpdate.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(paymentToUpdate);
            return new ResponseDTO(HttpStatus.OK.toString(), "Pago actualizado exitosamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al actualizar el pago: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error de base de datos");
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el pago: {}", e.getMessage());
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error inesperado");
        }
    }

    // Eliminar pago por ID (deshabilitar)
    @Transactional
    public ResponseDTO disablePayment(int id) {
        // Buscar el pago por ID
        Optional<Payment> paymentEntity = findById(id);
        if (!paymentEntity.isPresent()) {
            return new ResponseDTO("error", "Pago no encontrado");
        }

        try {
            Payment paymentToDisable = paymentEntity.get();

            // Cambiar el estatus del pago a false (deshabilitar)
            paymentToDisable.setStatus(false);

            // Guardar el cambio en la base de datos
            paymentRepository.save(paymentToDisable);

            return new ResponseDTO("success", "Pago deshabilitado correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al deshabilitar el pago: {}", e.getMessage());
            return new ResponseDTO("error", "Error de base de datos al deshabilitar el pago");
        } catch (Exception e) {
            logger.error("Error inesperado al deshabilitar el pago: {}", e.getMessage());
            return new ResponseDTO("error", "Error inesperado al deshabilitar el pago");
        }
    }

    // Reactivar pago por ID
    @Transactional
    public ResponseDTO reactivatePayment(int id) {
        // Buscar el pago por ID
        Optional<Payment> paymentEntity = findById(id);
        if (!paymentEntity.isPresent()) {
            return new ResponseDTO("error", "Pago no encontrado");
        }

        try {
            Payment paymentToReactivate = paymentEntity.get();

            // Cambiar el estatus del pago a true (reactivar)
            paymentToReactivate.setStatus(true);

            // Guardar el cambio en la base de datos
            paymentRepository.save(paymentToReactivate);

            return new ResponseDTO("success", "Pago reactivado correctamente");
        } catch (DataAccessException e) {
            logger.error("Error de base de datos al reactivar el pago: {}", e.getMessage());
            return new ResponseDTO("error", "Error de base de datos al reactivar el pago");
        } catch (Exception e) {
            logger.error("Error inesperado al reactivar el pago: {}", e.getMessage());
            return new ResponseDTO("error", "Error inesperado al reactivar el pago");
        }
    }

    // Convertir entidad a DTO
    public PaymentDTO convertToDTO(Payment paymentEntity) {
        return new PaymentDTO(
            paymentEntity.getUser().getUserID(), // Obteniendo el ID del usuario
            paymentEntity.getAmount(),
            paymentEntity.getMethod()
        );
    }

    // Convertir DTO a entidad
    public Payment convertToModel(PaymentDTO paymentDTO) {
        Optional<User> userEntity = userRepository.findById(paymentDTO.getUserID());
        if (!userEntity.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        return new Payment(
            0, // ID autogenerado
            userEntity.get(),
            paymentDTO.getAmount(),
            paymentDTO.getMethod(),
            true, // Estado inicial como true
            LocalDateTime.now()
        );
    }
}