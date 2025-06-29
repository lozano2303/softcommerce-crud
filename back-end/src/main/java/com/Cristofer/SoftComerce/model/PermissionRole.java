package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "permission_role")
public class PermissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissionRoleID")
    private int permissionRoleID;

    @Column(name = "permissionType", length = 50, nullable = false)
    private String permissionType;

    @ManyToOne
    @JoinColumn(name = "pageRoleID", nullable = false)
    private PageRole pageRoleID;

    // Constructor sin parámetros
    public PermissionRole() {
    }

    // Constructor con todos los campos
    public PermissionRole(int permissionRoleID, String permissionType, PageRole pageRoleID) {
        this.permissionRoleID = permissionRoleID;
        this.permissionType = permissionType;
        this.pageRoleID = pageRoleID;
    }

    // Getters y setters
    public int getPermissionRoleID() {
        return permissionRoleID;
    }

    public void setPermissionRoleID(int permissionRoleID) {
        this.permissionRoleID = permissionRoleID;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public PageRole getPageRoleID() {
        return pageRoleID;
    }

    public void setPageRoleID(PageRole pageRoleID) {
        this.pageRoleID = pageRoleID;
    }
}