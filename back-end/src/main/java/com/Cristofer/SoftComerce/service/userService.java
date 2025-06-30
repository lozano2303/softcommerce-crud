package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.ResponseLogin;
import com.Cristofer.SoftComerce.DTO.UserDTO;
import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IRole;
import com.Cristofer.SoftComerce.repository.IUser;
import com.Cristofer.SoftComerce.service.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUser userRepository;
    private final IRole roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Listar todos los usuarios
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    // Verificar si un usuario existe por ID
    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }

    // Filtrar usuarios por nombre, correo y estado
    public List<User> filterUser(String name, String email, Boolean status) {
        return userRepository.filterUser(name, email, status);
    }

    // Registro de usuario
    @Transactional
    public ResponseDTO register(UserDTO userDTO) {
        // Siempre asigna el rol de Usuario (ID=1)
        Optional<Role> roleEntity = roleRepository.findById(1);
        if (!roleEntity.isPresent()) {
            return new ResponseDTO("error", "Rol por defecto (Usuario) no encontrado");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseDTO("error", "El correo electrónico ya está registrado");
        }
        if (userDTO.getName() == null || userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new ResponseDTO("error", "El nombre debe estar entre 1 y 50 caracteres");
        }
        if (userDTO.getEmail() == null || !userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseDTO("error", "El formato del correo electrónico no es válido");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8) {
            return new ResponseDTO("error", "La contraseña debe tener al menos 8 caracteres");
        }
        try {
            User userEntity = convertToModel(userDTO, roleEntity.get());
            userRepository.save(userEntity);
            return new ResponseDTO("success", "Usuario registrado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al guardar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al guardar el usuario");
        }
    }

    // LOGIN con ResponseLogin DTO (no expone la contraseña)
    public ResponseLogin login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return new ResponseLogin("error", "El correo electrónico no está registrado", null);
        }
        User userEntity = optionalUser.get();
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            return new ResponseLogin("error", "La contraseña es incorrecta", null);
        }
        if (!userEntity.isStatus()) {
            return new ResponseLogin("error", "El usuario está inactivo. Comuníquese con el administrador.", null);
        }
        // Generar el token JWT usando JwtService
        String token = jwtService.generateToken(userEntity);
        return new ResponseLogin("success", "Inicio de sesión exitoso", token);
    }

    // Borrado lógico de usuario (status = false)
    @Transactional
    public ResponseDTO deleteById(int id) {
        Optional<User> userEntity = findById(id);
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }
        try {
            User userToDelete = userEntity.get();
            userToDelete.setStatus(false);
            userRepository.save(userToDelete);
            return new ResponseDTO("success", "Usuario deshabilitado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al deshabilitar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al deshabilitar el usuario");
        }
    }

    // Reactivar usuario por ID (status = true)
    @Transactional
    public ResponseDTO reactivateUser(int id) {
        Optional<User> userEntity = userRepository.findById(id);
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }
        try {
            User userToReactivate = userEntity.get();
            userToReactivate.setStatus(true);
            userRepository.save(userToReactivate);
            return new ResponseDTO("success", "Usuario reactivado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al reactivar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al reactivar el usuario");
        }
    }

    @Transactional
    public ResponseDTO updateRole(int userId, int roleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            return new ResponseDTO("error", "Rol no encontrado");
        }
        try {
            User user = userOpt.get();
            user.setRoleID(roleOpt.get());
            userRepository.save(user);
            return new ResponseDTO("success", "Rol actualizado correctamente");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error al actualizar el rol del usuario");
        }
    }

    // Actualizar usuario por ID
    @Transactional
    public ResponseDTO update(int id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }
        try {
            User userEntity = userOptional.get();

            if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
                if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
                    return new ResponseDTO("error", "El nombre debe estar entre 1 y 50 caracteres");
                }
                userEntity.setName(userDTO.getName());
            }
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    return new ResponseDTO("error", "El formato del correo electrónico no es válido");
                }
                userEntity.setEmail(userDTO.getEmail());
            }
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                if (userDTO.getPassword().length() < 8) {
                    return new ResponseDTO("error", "La contraseña debe tener al menos 8 caracteres");
                }
                userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            // El rol no se actualiza desde el DTO
            userRepository.save(userEntity);
            return new ResponseDTO("success", "Usuario actualizado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al actualizar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al actualizar el usuario");
        }
    }

    // Convertir entidad a DTO (nunca expone la contraseña)
    public UserDTO convertToDTO(User userEntity) {
        return new UserDTO(
                userEntity.getName(),
                userEntity.getEmail(),
                null // Nunca expongas la contraseña
        );
    }

    // Convertir DTO a entidad, encriptando la contraseña
    public User convertToModel(UserDTO userDTO, Role role) {
        return new User(
                0, // ID autogenerado
                userDTO.getName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                true, // Estado inicial como true (activo)
                LocalDateTime.now(),
                role
        );
    }
}