package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.userDTO;
import com.Cristofer.SoftComerce.service.userService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/user")
public class userController {

    @Autowired
    private userService userService;

    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@RequestBody userDTO user) {
        userService.save(user);
        return new ResponseEntity<>("register OK", HttpStatus.OK);
    }
    

}
