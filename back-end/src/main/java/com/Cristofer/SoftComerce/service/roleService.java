package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.roleDTO;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.repository.Irole;

@Service
public class roleService {

    @Autowired
    private Irole roleRepository; // Cambiado de 'data' a 'roleRepository'

    // Guardar rol
    public responseDTO save(roleDTO roleDTO) {
        if (!validateRole(roleDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del rol inválidos");
        }
    
        role roleRegister = convertToModel(roleDTO);
        roleRepository.save(roleRegister);
    
        return new responseDTO(HttpStatus.OK.toString(), "Rol guardado exitosamente");
    }

    // Obtener todos los roles
    public List<role> findAll() {
        return roleRepository.findAll();
    }

    // Buscar rol por ID
    public Optional<role> findById(int id) {
        return roleRepository.findById(id);
    }

    // Actualizar rol
    public responseDTO update(int id, roleDTO roleDTO) {
        Optional<role> existingRole = findById(id);
        if (!existingRole.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El rol no existe");
        }

        if (!validateRole(roleDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del rol inválidos");
        }

        role roleToUpdate = existingRole.get();
        roleToUpdate.setName(roleDTO.getroleName()); // Asumiendo que el método se llama setName()

        roleRepository.save(roleToUpdate);

        return new responseDTO(HttpStatus.OK.toString(), "Rol actualizado correctamente");
    }

    // Eliminar rol
    public responseDTO deleteById(int id) {
        Optional<role> role = findById(id);
        if (!role.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El rol no existe");
        }

        roleRepository.delete(role.get());

        return new responseDTO(HttpStatus.OK.toString(), "Rol eliminado correctamente");
    }

    // Filtrar roles por nombre
    public List<role> filterRoles(String roleName) {
        List<role> allRoles = roleRepository.findAll();
        
        if (roleName == null || roleName.isEmpty()) {
            return allRoles;
        }
        
        // Filtrar roles que contengan el texto proporcionado (case insensitive)
        return allRoles.stream()
                .filter(r -> r.getName().toLowerCase().contains(roleName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Validaciones
    private boolean validateRole(roleDTO dto) {
        return dto.getroleName() != null && !dto.getroleName().isBlank();
    }

    // Conversión de DTO a modelo
    public role convertToModel(roleDTO roleDTO) {
        role role = new role();
        role.setName(roleDTO.getroleName());
        return role;
    }

    // Conversión de modelo a DTO
    public roleDTO convertToDTO(role role) {
        roleDTO dto = new roleDTO(null);
        dto.setroleName(role.getName());
        return dto;
    }
}