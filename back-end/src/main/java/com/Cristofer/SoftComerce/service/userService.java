package com.Cristofer.SoftComerce.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Cristofer.SoftComerce.DTO.userDTO;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.repository.Iuser;

import java.time.LocalDateTime;

@Service
public class userService {

    @Autowired
    private Iuser data;

    public void save(userDTO userDTO) {
        user userRegister = converToModel(userDTO);
        data.save(userRegister);
    }

    public userDTO convertToDTO(user user) {
        userDTO userdto = new userDTO(
            user.getname(),
            user.getemail(),
            user.getpassword());
            return userdto;
    }

    public user converToModel(userDTO userDTO){
        user user = new user(
            0,
            userDTO.getName(),
            userDTO.getEmail(),
            userDTO.getPassword(),
            LocalDateTime.now());
        return user;
    }

}
