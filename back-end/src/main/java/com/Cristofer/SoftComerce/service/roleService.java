package com.Cristofer.SoftComerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.roleDTO;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.repository.Irole;

@Service
public class roleService {

    @Autowired
    private Irole roleRepository;

    // Listar todos los roles
    public List<role> findAll() {
        return roleRepository.findAll();
    }

    // Buscar rol por ID
    public Optional<role> findById(int id) {
        return roleRepository.findById(id);
    }

    // Verificar si un rol existe por ID
    public boolean existsById(int id) {
        return roleRepository.existsById(id);
    }

    // Guardar rol con validaciones
    @Transactional
    public responseDTO save(roleDTO roleDTO) {
        // Validaciones
        if (roleDTO.getroleName() == null || roleDTO.getroleName().length() < 1 || roleDTO.getroleName().length() > 50) {
            return new responseDTO("El nombre del rol debe estar entre 1 y 50 caracteres", "error");
        }

        try {
            // Convertir DTO a entidad y guardar
            role roleEntity = convertToModel(roleDTO);
            roleRepository.save(roleEntity);
            return new responseDTO("Rol registrado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al guardar el rol", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al guardar el rol", "error");
        }
    }

    // Actualizar rol por ID
    @Transactional
    public responseDTO update(int id, roleDTO roleDTO) {
        Optional<role> existingRole = findById(id);
        if (!existingRole.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El rol no existe");
        }

        // Validaciones
        if (roleDTO.getroleName() == null || roleDTO.getroleName().length() < 1 || roleDTO.getroleName().length() > 50) {
            return new responseDTO("El nombre del rol debe estar entre 1 y 50 caracteres", "error");
        }

        try {
            // Actualizar datos del rol
            role roleToUpdate = existingRole.get();
            roleToUpdate.setName(roleDTO.getroleName());
            roleRepository.save(roleToUpdate);
            return new responseDTO("Rol actualizado exitosamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al actualizar el rol", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al actualizar el rol", "error");
        }
    }

    // Eliminar rol por ID
    @Transactional
    public responseDTO deleteById(int id) {
        Optional<role> roleEntity = findById(id);
        if (!roleEntity.isPresent()) {
            return new responseDTO("Rol no encontrado", "error");
        }

        try {
            roleRepository.deleteById(id);
            return new responseDTO("Rol eliminado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al eliminar el rol", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al eliminar el rol", "error");
        }
    }

    // Convertir entidad a DTO
    public roleDTO convertToDTO(role roleEntity) {
        return new roleDTO(roleEntity.getName());
    }

    // Convertir DTO a entidad
    public role convertToModel(roleDTO roleDTO) {
        return new role(0, roleDTO.getroleName());
    }
}