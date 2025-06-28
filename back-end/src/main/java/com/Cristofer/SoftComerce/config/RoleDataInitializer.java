package com.Cristofer.SoftComerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.repository.IRole;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final IRole roleRepository;

    public RoleDataInitializer(IRole roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el rol Admin ya existe
        if (!roleRepository.existsByName("Admin")) {
            Role adminRole = new Role();
            adminRole.setName("Admin");
            roleRepository.save(adminRole);
            System.out.println("Rol Admin creado exitosamente");
        }
    }
}