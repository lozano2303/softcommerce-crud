package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.userDTO;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Irole;
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class userService {

    @Autowired
    private Iuser userRepository;

    @Autowired
    private Irole roleRepository;

    // Listar todos los usuarios
    public List<user> findAll() {
        return userRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<user> findById(int id) {
        return userRepository.findById(id);
    }

    // Verificar si un usuario existe por ID
    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }

    // Filtrar usuarios por nombre, correo y estado
    public List<user> filterUser(String name, String email, Boolean status) {
        return userRepository.filterUser(name, email, status);
    }

    // Guardar usuario con validaciones
    @Transactional
    public responseDTO save(userDTO userDTO) {
        // Validar que el rol exista
        Optional<role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            return new responseDTO("Rol no encontrado", "error");
        }

        // Validaciones adicionales
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new responseDTO("El nombre debe estar entre 1 y 50 caracteres", "error");
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new responseDTO("El formato del correo electrónico no es válido", "error");
        }

        if (userDTO.getPassword().length() < 8) {
            return new responseDTO("La contraseña debe tener al menos 8 caracteres", "error");
        }

        try {
            // Convertir DTO a entidad y guardar
            user userEntity = convertToModel(userDTO);
            userEntity.setRoleID(roleEntity.get());
            userRepository.save(userEntity);

            return new responseDTO("Usuario registrado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al guardar el usuario", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al guardar el usuario", "error");
        }
    }

    // Actualizar usuario por ID
    @Transactional
    public responseDTO update(int id, userDTO userDTO) {
        Optional<user> existingUser = findById(id);
        if (!existingUser.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El usuario no existe");
        }

        // Validar que el rol exista
        Optional<role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            return new responseDTO("Rol no encontrado", "error");
        }

        // Validaciones adicionales
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new responseDTO("El nombre debe estar entre 1 y 50 caracteres", "error");
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new responseDTO("El formato del correo electrónico no es válido", "error");
        }

        if (userDTO.getPassword().length() < 8) {
            return new responseDTO("La contraseña debe tener al menos 8 caracteres", "error");
        }

        try {
            // Actualizar datos del usuario
            user userToUpdate = existingUser.get();
            userToUpdate.setName(userDTO.getName());
            userToUpdate.setEmail(userDTO.getEmail());
            userToUpdate.setPassword(userDTO.getPassword());
            userToUpdate.setRoleID(roleEntity.get());
            userToUpdate.setCreatedAt(LocalDateTime.now());

            userRepository.save(userToUpdate);

            return new responseDTO("Usuario actualizado exitosamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al actualizar el usuario", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al actualizar el usuario", "error");
        }
    }

    // Eliminar usuario por ID
    @Transactional
    public responseDTO deleteById(int id) {
        Optional<user> userEntity = findById(id);
        if (!userEntity.isPresent()) {
            return new responseDTO("Usuario no encontrado", "error");
        }

        try {
            userRepository.deleteById(id);
            return new responseDTO("Usuario eliminado correctamente", "success");
        } catch (DataAccessException e) {
            return new responseDTO("Error de base de datos al eliminar el usuario", "error");
        } catch (Exception e) {
            return new responseDTO("Error inesperado al eliminar el usuario", "error");
        }
    }

    // Convertir entidad a DTO
    public userDTO convertToDTO(user userEntity) {
        return new userDTO(
            userEntity.getName(),
            userEntity.getEmail(),
            userEntity.getPassword(),
            userEntity.getRoleID().getRoleID() // Obteniendo el ID del rol
        );
    }

    // Convertir DTO a entidad
    public user convertToModel(userDTO userDTO) {
        Optional<role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            throw new IllegalArgumentException("Rol no encontrado");
        }

        return new user(
            0, // ID autogenerado
            userDTO.getName(),
            userDTO.getEmail(),
            userDTO.getPassword(),
            true, // Estado inicial como true
            LocalDateTime.now(),
            roleEntity.get()
        );
    }
}