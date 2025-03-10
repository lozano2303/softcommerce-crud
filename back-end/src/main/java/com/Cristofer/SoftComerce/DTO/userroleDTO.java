package com.Cristofer.SoftComerce.DTO;

public class userroleDTO {

    private int userID;
    private int roleID;

    // Constructor
    public userroleDTO(int userID, int roleID) {
        this.userID = userID;
        this.roleID = roleID;
    }

    // Getters y Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}