package com.Cristofer.SoftComerce.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordEmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo electrónico genérico.
     * @param to destinatario
     * @param subject asunto del correo
     * @param body cuerpo del mensaje
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    /**
     * Envía el correo de recuperación de contraseña al propio solicitante.
     * @param userEmail El correo del usuario que hizo la solicitud.
     * @param resetLink Enlace de recuperación (con token).
     */
    public void sendPasswordResetEmailToRequester(String userEmail, String resetLink) {
        String subject = "Recuperación de contraseña - SoftComerce";
        String body =
            "Hola,\n\n" +
            "Recibimos una solicitud para restablecer la contraseña de tu cuenta en SoftComerce.\n\n" +
            "Para cambiar tu contraseña, haz clic en el siguiente enlace o cópialo y pégalo en tu navegador:\n" +
            resetLink + "\n\n" +
            "Si tú no solicitaste este cambio, puedes ignorar este correo. Tu contraseña actual seguirá siendo válida.\n\n" +
            "Por cuestiones de seguridad, este enlace expirará en los próximos minutos.\n\n" +
            "¡Gracias por confiar en nosotros!\n" +
            "El equipo de SoftComerce";

        sendEmail(userEmail, subject, body);
    }

    // Puedes agregar más métodos para otros emails relacionados con contraseñas aquí
}