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
        createRoleIfNotExists("ROLE_USUARIO");
        createRoleIfNotExists("ROLE_VENDEDOR");
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_SUPERVISOR");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Rol " + roleName + " creado exitosamente");
        }
    }
}