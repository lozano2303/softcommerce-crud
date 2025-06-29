package com.Cristofer.SoftComerce.DTO;

import com.Cristofer.SoftComerce.model.Page;
import com.Cristofer.SoftComerce.model.Role;

public class PageRoleDTO {
    private Page pageID;
    private Role roleID;

    // Constructor sin parámetros
    public PageRoleDTO() {
    }

    // Constructor con parámetros
    public PageRoleDTO(Page pageID, Role roleID) {
        this.pageID = pageID;
        this.roleID = roleID;
    }

    // Getters y setters
    public Page getPageID() {
        return pageID;
    }

    public void setPageID(Page pageID) {
        this.pageID = pageID;
    }

    public Role getRoleID() {
        return roleID;
    }

    public void setRoleID(Role roleID) {
        this.roleID = roleID;
    }
}