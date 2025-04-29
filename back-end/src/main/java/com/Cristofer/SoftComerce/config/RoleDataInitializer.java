package com.Cristofer.SoftComerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.repository.Irole;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final Irole roleRepository;

    public RoleDataInitializer(Irole roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el rol Admin ya existe
        if (!roleRepository.existsByName("Admin")) {
            role adminRole = new role();
            adminRole.setName("Admin");
            roleRepository.save(adminRole);
            System.out.println("Rol Admin creado exitosamente");
        }
    }
}