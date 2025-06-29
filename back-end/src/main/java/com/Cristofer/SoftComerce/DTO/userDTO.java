package com.Cristofer.SoftComerce.DTO;

import com.Cristofer.SoftComerce.model.Role;

public class UserDTO {

    private String name;
    private String email;
    private String password;
    private Role roleID; // Campo adicional para manejar el rol del usuario

    public UserDTO() {}

    public UserDTO(String name, String email, String password, Role roleID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoleID() {
        return roleID;
    }

    public void setRoleID(Role roleID) {
        this.roleID = roleID;
    }
}