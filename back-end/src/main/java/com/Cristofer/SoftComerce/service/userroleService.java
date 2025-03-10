package com.Cristofer.SoftComerce.service;

import com.Cristofer.SoftComerce.DTO.userroleDTO;
import com.Cristofer.SoftComerce.model.user;
import com.Cristofer.SoftComerce.model.role;
import com.Cristofer.SoftComerce.model.userrole;
import com.Cristofer.SoftComerce.model.userroleId;
import com.Cristofer.SoftComerce.repository.Iuserrole;
import com.Cristofer.SoftComerce.repository.Iuser;
import com.Cristofer.SoftComerce.repository.Irole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userroleService {

    @Autowired
    private Iuserrole userroleRepository;

    @Autowired
    private Iuser userRepository;

    @Autowired
    private Irole roleRepository;

    // Guardar una relación user-role
    public void save(userroleDTO userroleDTO) {
        user user = userRepository.findById(userroleDTO.getUserID()).orElse(null);
        role role = roleRepository.findById(userroleDTO.getRoleID()).orElse(null);

        if (user != null && role != null) {
            userroleId id = new userroleId();
            id.setUserID(userroleDTO.getUserID());
            id.setRoleID(userroleDTO.getRoleID());

            userrole userrole = new userrole();
            userrole.setId(id);
            userrole.setUser(user);
            userrole.setRole(role);

            userroleRepository.save(userrole);
        }
    }

    // Convertir de userrole a userroleDTO
    public userroleDTO convertToDTO(userrole userrole) {
        return new userroleDTO(
            userrole.getUser().getuserID(),
            userrole.getRole().getroleID()
        );
    }

    // Convertir de userroleDTO a userrole
    public userrole convertToModel(userroleDTO userroleDTO) {
        userroleId id = new userroleId();
        id.setUserID(userroleDTO.getUserID());
        id.setRoleID(userroleDTO.getRoleID());

        userrole userrole = new userrole();
        userrole.setId(id);

        return userrole;
    }
}