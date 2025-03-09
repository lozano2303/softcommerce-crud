package com.Cristofer.SoftComerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.roleDTO;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.repository.Irole;

@Service
public class roleService {
    @Autowired
    private Irole data;

    public void save(roleDTO roleDTO){
        role roleRegister = convertToModel(roleDTO);
        data.save(roleRegister);
    }

    public roleDTO conveetToDTO(role role){
        roleDTO roledto = new roleDTO(
            role.getroleName());
            return roledto;
    }

    public role convertToModel(roleDTO roleDTO){
        role role = new role(
            0,
            roleDTO.getroleName());
            return role;
    }

}
