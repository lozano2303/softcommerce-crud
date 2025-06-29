package com.Cristofer.SoftComerce.DTO;

import com.Cristofer.SoftComerce.model.PageRole;

public class PermissionRoleDTO {
    private PermissionType permissionType;
    private PageRole pageRoleID;

    // Enum debe estar fuera de la clase o ser static, aquí lo dejamos público y estático para fácil acceso
    public enum PermissionType {
        READ,
        WRITE,
        UPDATE,
        DELETE
        // Agrega más tipos si es necesario
    }

    // Constructor sin parámetros
    public PermissionRoleDTO() {
    }

    // Constructor con parámetros
    public PermissionRoleDTO(PermissionType permissionType, PageRole pageRoleID) {
        this.permissionType = permissionType;
        this.pageRoleID = pageRoleID;
    }

    // Getters y setters
    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public PageRole getPageRoleID() {
        return pageRoleID;
    }

    public void setPageRoleID(PageRole pageRoleID) {
        this.pageRoleID = pageRoleID;
    }
}