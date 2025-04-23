package com.Cristofer.SoftComerce.DTO;

public class userDTO {

    private String name;

    private String email;

    private String password;

    private int roleID; // Campo adicional para manejar el rol del usuario

    public userDTO(String name, String email, String password, int roleID) {
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

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}