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

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.userDTO;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Irole;
import com.Cristofer.SoftComerce.repository.Iuser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class userService {

    @Autowired
    private Iuser userRepository;

    private static final String SECRET_KEY = "mi-clave-secreta-muy-segura-no-puedo-creer-por-que-es-tan-segura-guau-guau-oh-oh-oh-me-vengo-que-rico-12345678910-@-@-/"; // Cambia por una clave segura

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

    // **Registro de usuario**
    @Transactional
    public responseDTO register(userDTO userDTO) {
        // Validar que el rol exista
        Optional<role> roleEntity = roleRepository.findById(userDTO.getRoleID());
        if (!roleEntity.isPresent()) {
            return new responseDTO("error", "Rol no encontrado");
        }

        // Verificar si el correo ya está registrado
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return new responseDTO("error", "El correo electrónico ya está registrado");
        }

        // Validaciones adicionales
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new responseDTO("error", "El nombre debe estar entre 1 y 50 caracteres");
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new responseDTO("error", "El formato del correo electrónico no es válido");
        }

        if (userDTO.getPassword().length() < 8) {
            return new responseDTO("error", "La contraseña debe tener al menos 8 caracteres");
        }

        try {
            // Convertir DTO a entidad y guardar
            user userEntity = convertToModel(userDTO);
            userEntity.setRoleID(roleEntity.get());
            userRepository.save(userEntity);

            return new responseDTO("success", "Usuario registrado correctamente");
        } catch (DataAccessException e) {
            return new responseDTO("error", "Error de base de datos al guardar el usuario");
        } catch (Exception e) {
            return new responseDTO("error", "Error inesperado al guardar el usuario");
        }
    }

    // **Inicio de sesión**
    public responseDTO login(String email, String password) {
        // Buscar al usuario por correo
        Optional<user> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return new responseDTO("error", "El correo electrónico no está registrado");
        }

        user userEntity = optionalUser.get();

        // Verificar la contraseña hasheada (usa BCrypt)
        if (!BCrypt.checkpw(password, userEntity.getPassword())) {
            return new responseDTO("error", "La contraseña es incorrecta");
        }

        // Generar un token
        String token = generateToken(userEntity);

        // Si las credenciales son correctas, devolver un éxito con el token
        return new responseDTO("success", "Inicio de sesión exitoso", token);
    }

    private String generateToken(user userEntity) {
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
    public responseDTO deleteById(int id) {
        Optional<user> userEntity = findById(id);
        if (!userEntity.isPresent()) {
            return new responseDTO("error", "Usuario no encontrado");
        }

        try {
            userRepository.deleteById(id);
            return new responseDTO("success", "Usuario eliminado correctamente");
        } catch (DataAccessException e) {
            return new responseDTO("error", "Error de base de datos al eliminar el usuario");
        } catch (Exception e) {
            return new responseDTO("error", "Error inesperado al eliminar el usuario");
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
                BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()), // Contraseña cifrada
                true, // Estado inicial como true
                LocalDateTime.now(),
                roleEntity.get()
        );
    }

    public responseDTO update(int id, userDTO userDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}