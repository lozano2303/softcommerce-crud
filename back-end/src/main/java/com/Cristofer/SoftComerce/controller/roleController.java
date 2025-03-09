package com.Cristofer.SoftComerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Cristofer.SoftComerce.DTO.roleDTO;
import com.Cristofer.SoftComerce.service.roleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/role")
public class roleController {

    @Autowired
    private roleService roleService;

    @PostMapping("/")
    public ResponseEntity<Object> registerRole(@RequestBody roleDTO role){
        roleService.save(role);
        return new ResponseEntity<>("register role OK", HttpStatus.OK);
    }

}
