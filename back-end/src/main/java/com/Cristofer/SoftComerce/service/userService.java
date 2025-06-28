package com.Cristofer.SoftComerce.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.UserDTO;
import com.Cristofer.SoftComerce.model.Role;
import com.Cristofer.SoftComerce.model.User;
import com.Cristofer.SoftComerce.repository.IRole;
import com.Cristofer.SoftComerce.repository.IUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

    @Autowired
    private IUser userRepository;

    private static final String SECRET_KEY = "mi-clave-secreta-muy-segura-no-puedo-creer-por-que-es-tan-segura-guau-guau-oh-oh-oh-me-vengo-que-rico-12345678910-@-@-/"; // Cambia por una clave segura

    @Autowired
    private IRole roleRepository;

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

    // **Registro de usuario**
    @Transactional
    public ResponseDTO register(UserDTO userDTO) {
        // Validar que el rol exista
        Optional<Role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            return new ResponseDTO("error", "Rol no encontrado");
        }

        // Verificar si el correo ya está registrado
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new ResponseDTO("error", "El correo electrónico ya está registrado");
        }

        // Validaciones adicionales
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new ResponseDTO("error", "El nombre debe estar entre 1 y 50 caracteres");
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseDTO("error", "El formato del correo electrónico no es válido");
        }

        if (userDTO.getPassword().length() < 8) {
            return new ResponseDTO("error", "La contraseña debe tener al menos 8 caracteres");
        }

        try {
            // Convertir DTO a entidad y guardar
            User userEntity = convertToModel(userDTO);
            userEntity.setRoleID(roleEntity.get());
            userRepository.save(userEntity);

            return new ResponseDTO("success", "Usuario registrado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al guardar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al guardar el usuario");
        }
    }

    // **Inicio de sesión**
    public ResponseDTO login(String email, String password) {
        // Buscar al usuario por correo
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return new ResponseDTO("error", "El correo electrónico no está registrado");
        }

        User userEntity = optionalUser.get();

        // Verificar la contraseña hasheada (usa BCrypt)
        if (!BCrypt.checkpw(password, userEntity.getPassword())) {
            return new ResponseDTO("error", "La contraseña es incorrecta");
        }

        // Verificar si el usuario está activo
        if (!userEntity.isStatus()) {
            return new ResponseDTO("error", "El usuario está inactivo. Comuníquese con el administrador.");
        }

        // Generar un token
        String token = generateToken(userEntity);

        // Si las credenciales son correctas, devolver un éxito con el token y el usuario
        return new ResponseDTO("success", "Inicio de sesión exitoso", token, userEntity);
    }

    private String generateToken(User userEntity) {
        // Crear la clave secreta
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        // Generar el token JWT
        return Jwts.builder()
                .setSubject(userEntity.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de validez
                .signWith(key, SignatureAlgorithm.HS512) // Usar la clave y el algoritmo
                .compact();
    }

    // Eliminar usuario por ID
    @Transactional
    public ResponseDTO deleteById(int id) {
        // Buscar el usuario por ID
        Optional<User> userEntity = findById(id);
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }

        try {
            User userToDelete = userEntity.get();

            // Cambiar el estatus del usuario a false (borrado lógico)
            userToDelete.setStatus(false);

            // Guardar el cambio en la base de datos
            userRepository.save(userToDelete);

            return new ResponseDTO("success", "Usuario deshabilitado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al deshabilitar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al deshabilitar el usuario");
        }
    }

    // **Reactivar usuario por ID**
    @Transactional
    public ResponseDTO reactivateUser(int id) {
        // Buscar el usuario por ID
        Optional<User> userEntity = userRepository.findById(id);
        if (!userEntity.isPresent()) {
            return new ResponseDTO("error", "Usuario no encontrado");
        }

        try {
            User userToReactivate = userEntity.get();

            // Cambiar el estatus del usuario a true (reactivar)
            userToReactivate.setStatus(true);

            // Guardar el cambio en la base de datos
            userRepository.save(userToReactivate);

            return new ResponseDTO("success", "Usuario reactivado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al reactivar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al reactivar el usuario");
        }
    }

    // Actualizar usuario por ID
    @Transactional
    public ResponseDTO update(int id, UserDTO userDTO) {
        // Buscar el usuario por ID en la base de datos
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            // Si el usuario no existe, devolver un mensaje de error
            return new ResponseDTO("error", "Usuario no encontrado");
        }

        try {
            User userEntity = userOptional.get();

            // Validar que el rol exista
            Optional<Role> roleEntity = roleRepository.findById(userDTO.getRoleID());
            if (!roleEntity.isPresent()) {
                return new ResponseDTO("error", "Rol no encontrado");
            }

            // Actualizar los campos del usuario con los valores del DTO
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
                userEntity.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()));
            }

            // Actualizar el rol
            userEntity.setRoleID(roleEntity.get());

            // Guardar los cambios en la base de datos
            userRepository.save(userEntity);

            // Devolver una respuesta indicando éxito
            return new ResponseDTO("success", "Usuario actualizado correctamente");
        } catch (DataAccessException e) {
            return new ResponseDTO("error", "Error de base de datos al actualizar el usuario");
        } catch (Exception e) {
            return new ResponseDTO("error", "Error inesperado al actualizar el usuario");
        }
    }

    // Convertir entidad a DTO
    public UserDTO convertToDTO(User userEntity) {
        return new UserDTO(
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoleID().getRoleID() // Obteniendo el ID del rol
        );
    }

    // Convertir DTO a entidad
    public User convertToModel(UserDTO userDTO) {
        Optional<Role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            throw new IllegalArgumentException("Rol no encontrado");
        }

        return new User(
                0, // ID autogenerado
                userDTO.getName(),
                userDTO.getEmail(),
                BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()), // Contraseña cifrada
                true, // Estado inicial como true
                LocalDateTime.now(),
                roleEntity.get()
        );
    }
}