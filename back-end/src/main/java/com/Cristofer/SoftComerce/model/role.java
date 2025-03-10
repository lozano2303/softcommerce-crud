package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity (name = "role")

public class role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleID")
    private int roleID;

    @Column(name = "roleName", length = 150, nullable = false)
    private String roleName;

    public role(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public role(){
}

    public int getroleID() {
        return roleID;
    }

    public void setroleID(int roleID) {
        this.roleID = roleID;
    }

    public String getroleName() {
        return roleName;
    }

    public void setroleName(String roleName) {
        this.roleName = roleName;
    }
}
