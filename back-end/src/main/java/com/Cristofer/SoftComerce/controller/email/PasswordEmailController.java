package com.Cristofer.SoftComerce.controller.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.service.PasswordService;
import com.Cristofer.SoftComerce.service.email.PasswordEmailService;

/**
 * Controlador REST para acciones relacionadas con correos de contraseña.
 */
@RestController
@RequestMapping("/password-email")
public class PasswordEmailController {

    @Autowired
    private PasswordEmailService passwordEmailService;

    @Autowired
    private PasswordService passwordService;

    /**
     * Endpoint de prueba para enviar un correo genérico (puede usarse para pruebas).
     */
    @GetMapping("/sendTestEmail/{to}")
    public String sendTestEmail(@PathVariable String to) {
        passwordEmailService.sendEmail(
            to,
            "Correo de prueba - SoftComerce",
            "Este es un correo de prueba de SoftComerce.\n\nSaludos,\nEquipo de SoftComerce"
        );
        return "Mail sent successfully";
    }

    /**
     * Endpoint para iniciar el proceso de recuperación de contraseña.
     * Esto enviará un mail con el enlace de recuperación si el usuario existe.
     */
    @GetMapping("/forgotPassword/{email}")
    public String forgotPassword(@PathVariable String email) {
        passwordService.startForgotPassword(email);
        return "If this email is registered, a password reset mail has been sent.";
    }

    /**
     * Endpoint para enviar manualmente el correo de recuperación a un usuario (requiere el enlace).
     * Puede ser útil para pruebas.
     */
    @GetMapping("/sendPasswordResetMail/{email}/{resetLink}")
    public String sendPasswordResetMail(
            @PathVariable String email,
            @PathVariable String resetLink) {
        passwordEmailService.sendPasswordResetEmailToRequester(email, resetLink);
        return "Password reset mail sent successfully";
    }

    // Puedes agregar más endpoints relacionados con correos de contraseña aquí, por ejemplo:
    // - notificación de cambio de contraseña
    // - verificación de seguridad, etc.
}