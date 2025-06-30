package com.Cristofer.SoftComerce.DTO;

public class RoleAssignDTO {
    private int roleID;

    public RoleAssignDTO() {}

    public RoleAssignDTO(int roleID) {
        this.roleID = roleID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}