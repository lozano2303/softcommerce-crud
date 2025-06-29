package com.Cristofer.SoftComerce.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.RecoveryRequestDTO;
import com.Cristofer.SoftComerce.model.RecoveryRequest;
import com.Cristofer.SoftComerce.repository.IRecoveryRequest;
import com.Cristofer.SoftComerce.repository.IUser;
import com.Cristofer.SoftComerce.model.User;

@Service
public class RecoveryRequestService {

    @Autowired
    private IRecoveryRequest recoveryRequestRepository;

    @Autowired
    private IUser userRepository;

    // Listar todas las solicitudes de recuperación
    public List<RecoveryRequest> findAll() {
        return recoveryRequestRepository.findAll();
    }

    // Buscar por ID
    public Optional<RecoveryRequest> findById(int id) {
        return recoveryRequestRepository.findById(id);
    }

    // Buscar por token
    public Optional<RecoveryRequest> findByToken(String token) {
        return recoveryRequestRepository.findByToken(token);
    }

    // Buscar por usuario
    public List<RecoveryRequest> findByUserID(int userID) {
        return recoveryRequestRepository.findByUserID_UserID(userID);
    }

    // Registrar una nueva solicitud de recuperación
    @Transactional
    public String createRecoveryRequest(RecoveryRequestDTO dto) {
        if (dto.getUserID() == null || dto.getUserID().getUserID() == 0) {
            return "Usuario inválido para recuperación";
        }

        Optional<User> userOptional = userRepository.findById(dto.getUserID().getUserID());
        if (!userOptional.isPresent()) {
            return "Usuario no encontrado";
        }

        String token = generateSecureToken();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(1); // Token expira en 1 hora

        RecoveryRequest recoveryRequest = new RecoveryRequest(
                0,
                userOptional.get(),
                token,
                false,
                expiresAt,
                now
        );

        try {
            recoveryRequestRepository.save(recoveryRequest);
            return token;
        } catch (DataAccessException e) {
            return "Error de base de datos al guardar la solicitud";
        } catch (Exception e) {
            return "Error inesperado al guardar la solicitud";
        }
    }

    // Marcar como usada una solicitud de recuperación
    @Transactional
    public String markAsUsed(String token) {
        Optional<RecoveryRequest> recoveryRequestOptional = recoveryRequestRepository.findByToken(token);
        if (!recoveryRequestOptional.isPresent()) {
            return "Solicitud de recuperación no encontrada";
        }
        RecoveryRequest recoveryRequest = recoveryRequestOptional.get();
        recoveryRequest.setUsed(true);
        recoveryRequestRepository.save(recoveryRequest);
        return "Solicitud marcada como usada";
    }

    // Eliminar una solicitud por ID
    @Transactional
    public String deleteById(int id) {
        Optional<RecoveryRequest> recoveryRequest = recoveryRequestRepository.findById(id);
        if (!recoveryRequest.isPresent()) {
            return "Solicitud de recuperación no encontrada";
        }
        try {
            recoveryRequestRepository.deleteById(id);
            return "Solicitud de recuperación eliminada";
        } catch (DataAccessException e) {
            return "Error de base de datos al eliminar la solicitud";
        } catch (Exception e) {
            return "Error inesperado al eliminar la solicitud";
        }
    }

    // Utilidad para token seguro
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Convertir DTO a entidad
    public RecoveryRequest convertToModel(RecoveryRequestDTO dto, String token, LocalDateTime expiresAt, LocalDateTime createdAt) {
        return new RecoveryRequest(
                0,
                dto.getUserID(),
                token,
                false,
                expiresAt,
                createdAt
        );
    }

    // Convertir entidad a DTO
    public RecoveryRequestDTO convertToDTO(RecoveryRequest rr) {
        return new RecoveryRequestDTO(rr.getUserID());
    }
}