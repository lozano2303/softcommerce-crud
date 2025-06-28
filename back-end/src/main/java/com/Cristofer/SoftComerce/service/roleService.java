package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.RoleDTO;
import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.repository.IRole;

@Service
public class RoleService {

    @Autowired
    private IRole roleRepository;

    // Guardar rol
    public ResponseDTO save(RoleDTO roleDTO) {
        if (!validateRole(roleDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del rol inválidos");
        }

        Role roleRegister = convertToModel(roleDTO);
        roleRepository.save(roleRegister);

        return new ResponseDTO(HttpStatus.OK.toString(), "Rol guardado exitosamente");
    }

    // Obtener todos los roles
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    // Buscar rol por ID
    public Optional<Role> findById(int id) {
        return roleRepository.findById(id);
    }

    // Actualizar rol
    public ResponseDTO update(int id, RoleDTO roleDTO) {
        Optional<Role> existingRole = findById(id);
        if (!existingRole.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "El rol no existe");
        }

        if (!validateRole(roleDTO)) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del rol inválidos");
        }

        Role roleToUpdate = existingRole.get();
        roleToUpdate.setName(roleDTO.getRoleName());

        roleRepository.save(roleToUpdate);

        return new ResponseDTO(HttpStatus.OK.toString(), "Rol actualizado correctamente");
    }

    // Eliminar rol
    public ResponseDTO deleteById(int id) {
        Optional<Role> role = findById(id);
        if (!role.isPresent()) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.toString(), "El rol no existe");
        }

        roleRepository.delete(role.get());

        return new ResponseDTO(HttpStatus.OK.toString(), "Rol eliminado correctamente");
    }

    // Filtrar roles por nombre
    public List<Role> filterRoles(String roleName) {
        List<Role> allRoles = roleRepository.findAll();

        if (roleName == null || roleName.isEmpty()) {
            return allRoles;
        }

        return allRoles.stream()
                .filter(r -> r.getName().toLowerCase().contains(roleName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Validaciones
    private boolean validateRole(RoleDTO dto) {
        return dto.getRoleName() != null && !dto.getRoleName().isBlank();
    }

    // Conversión de DTO a modelo
    public Role convertToModel(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getRoleName());
        return role;
    }

    // Conversión de modelo a DTO
    public RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO(role.getName());
        return dto;
    }
}