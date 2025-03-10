package com.Cristofer.SoftComerce.controller;

import com.Cristofer.SoftComerce.DTO.userroleDTO;
import com.Cristofer.SoftComerce.service.userroleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/userrole")
public class userroleController {

    @Autowired
    private userroleService userroleService;

    @PostMapping("/")
    public ResponseEntity<Object> createUserRole(@RequestBody userroleDTO userroleDTO) {
        userroleService.save(userroleDTO);
        return new ResponseEntity<>("userrole created successfully", HttpStatus.CREATED);
    }
}