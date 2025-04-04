package com.Cristofer.SoftComerce.service;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.responseDTO;
import com.Cristofer.SoftComerce.DTO.userDTO;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Iuser;

@Service
public class userService {

    @Autowired
    private Iuser data;
    
    public List<user> findAll() {
        return data.findAll();
    }

    public Optional<user> findById(int id) {
        return data.findById(id);
    }

    public responseDTO deleteUser(int id) {
        Optional<user> user = findById(id);
        if (!user.isPresent()) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El usuario no existe"
            );
        }
        
        data.deleteById(id);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "Usuario eliminado correctamente"
        );
    }

    public List<user> filterUser(String name, String email, Boolean status) {
        return data.filterUser(name, email, status); // No necesitas conversión
    }
    

    public responseDTO save(userDTO userDTO) {
        // Validaciones
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El nombre debe estar entre 1 y 50 caracteres"
            );
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El formato del correo electrónico no es válido"
            );
        }

        if (userDTO.getPassword().length() < 8) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "La contraseña debe tener al menos 8 caracteres"
            );
        }
        
        // Convertir y guardar
        user userRegister = convertToModel(userDTO);
        data.save(userRegister);
        
        return new responseDTO(
            HttpStatus.OK.toString(),
            "Usuario guardado correctamente"
        );
    }

    public responseDTO update(int id, userDTO userDTO) {
        Optional<user> existingUser = findById(id);
        if (!existingUser.isPresent()) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El usuario no existe"
            );
        }

        // Validaciones similares a save
        if (userDTO.getName().length() < 1 || userDTO.getName().length() > 50) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El nombre debe estar entre 1 y 50 caracteres"
            );
        }

        if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El formato del correo electrónico no es válido"
            );
        }

        if (userDTO.getPassword().length() < 8) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "La contraseña debe tener al menos 8 caracteres"
            );
        }
        
        // Actualizar datos
        user userToUpdate = existingUser.get();
        userToUpdate.setName(userDTO.getName());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setPassword(userDTO.getPassword());
        userToUpdate.setCreatedAt(LocalDateTime.now());
        
        data.save(userToUpdate);
        
        return new responseDTO(
            HttpStatus.OK.toString(),
            "Usuario actualizado exitosamente"
        );
    }

    public userDTO convertToDTO(user user) {
        return new userDTO(
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );
    }

    public user convertToModel(userDTO userDTO) {
        return new user(
            0, // userID
            userDTO.getName(), // name
            userDTO.getEmail(), // email
            userDTO.getPassword(), // password
            true, // status (puedes ajustar según la lógica requerida)
            LocalDateTime.now() // createdAt
        );
    }
}
