package com.Cristofer.SoftComerce.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IUser;
import com.Cristofer.SoftComerce.service.jwt.PasswordJwtService;
import com.Cristofer.SoftComerce.service.email.PasswordEmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final IUser userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordEmailService emailService; // Debes tener un servicio de correo implementado
    private final PasswordJwtService passwordJwtService; // Usando PasswordJwtService para recuperación

    /**
     * Cambia la contraseña del usuario autenticado (desde el perfil).
     * El userId siempre debe venir desde el token autenticado.
     */
    public void changePasswordFromProfile(Integer userIdFromToken, String currentPassword, String newPassword) {
        User user = userRepository.findById(userIdFromToken)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verifica la contraseña actual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Inicia flujo de recuperación ("olvidé mi contraseña").
     * Envía un email con un token de recuperación usando PasswordJwtService.
     */
    public void startForgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Por seguridad, no revelar si el email existe.
            return;
        }
        User user = userOpt.get();
        String token = passwordJwtService.generatePasswordResetToken(user);

        String resetLink = "https://tu-frontend/reset-password?token=" + token;
        String subject = "Recuperación de contraseña";
        String body = "Haz clic en el siguiente enlace para cambiar tu contraseña:\n" + resetLink;

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    /**
     * Cambia la contraseña usando un token JWT de recuperación (olvidó contraseña).
     * El userId se obtiene validando el token con PasswordJwtService.
     */
    public void resetPasswordWithToken(String resetToken, String newPassword) {
        // Valida el token y extrae el userId
        Integer userIdFromToken = passwordJwtService.extractUserId(resetToken);
        if (userIdFromToken == null || !passwordJwtService.isPasswordResetTokenValid(resetToken)) {
            throw new IllegalArgumentException("Token de recuperación inválido o expirado.");
        }
        User user = userRepository.findById(userIdFromToken)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // No es necesario invalidar el JWT si tiene expiración corta, pero podrías mantener una lista negra si lo deseas.
    }
}