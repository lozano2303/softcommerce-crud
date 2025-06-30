package com.Cristofer.SoftComerce.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IRole;
import com.Cristofer.SoftComerce.repository.IUser;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final IUser userRepository;
    private final IRole roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(IUser userRepository, IRole roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Busca el rol de Administrador
        Optional<Role> adminRoleOpt = roleRepository.findByName("ROLE_ADMIN");
        if (adminRoleOpt.isEmpty()) {
            System.out.println("No existe el rol Administrador. No se creará usuario admin por ahora.");
            return;
        }
        Role adminRole = adminRoleOpt.get();

        // Verifica si ya existe un admin por correo (puedes cambiar el correo si prefieres otro)
        String adminEmail = "admin@system.local";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin1234")); // Cambia la contraseña por algo seguro
            admin.setStatus(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRoleID(adminRole);

            userRepository.save(admin);
            System.out.println("Usuario administrador creado: " + adminEmail + " (contraseña: admin1234)");
        }
    }
}