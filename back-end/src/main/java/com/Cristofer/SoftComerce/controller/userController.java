package com.Cristofer.SoftComerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.LoginDTO;
import com.Cristofer.SoftComerce.DTO.ResponseDTO;
import com.Cristofer.SoftComerce.DTO.UserDTO;
import com.Cristofer.SoftComerce.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDTO userDTO) {
        ResponseDTO response = userService.register(userDTO);
        if ("success".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginDTO loginDTO) {
        ResponseDTO response = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
        if ("success".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if ("error".equals(response.getStatus()) && "El usuario está inactivo. Comuníquese con el administrador.".equals(response.getMessage())) {
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    // Obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<Object> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        var user = userService.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    // Eliminar un usuario por su ID (deshabilitar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        ResponseDTO response = userService.deleteById(id);
        if ("success".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Reactivar un usuario por su ID
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Object> reactivateUser(@PathVariable int id) {
        ResponseDTO response = userService.reactivateUser(id);
        if ("success".equals(response.getStatus())) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    // Filtrar usuarios por parámetros opcionales
    @GetMapping("/filter")
    public ResponseEntity<Object> filterUser(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "email") String email,
            @RequestParam(required = false, name = "status") Boolean status) {
        
        var userList = userService.filterUser(name, email, status);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // Actualizar un usuario por su ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        ResponseDTO response = userService.update(id, userDTO);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}