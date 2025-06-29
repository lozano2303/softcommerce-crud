package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleID")
    private int roleID;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // Constructor sin parámetros (predeterminado)
    public Role() {
    }

    // Constructor con parámetros
    public Role(int roleID, String name) {
        this.roleID = roleID;
        this.name = name;
    }

    // Getters y setters
    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}