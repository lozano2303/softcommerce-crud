package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "page_role")
public class PageRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pageRoleID")
    private int pageRoleID;

    @ManyToOne
    @JoinColumn(name = "pageID", nullable = false)
    private Page pageID;

    @ManyToOne
    @JoinColumn(name = "roleID", nullable = false)
    private Role roleID;

    // Constructor sin parámetros
    public PageRole() {
    }

    // Constructor con todos los campos
    public PageRole(int pageRoleID, Page pageID, Role roleID) {
        this.pageRoleID = pageRoleID;
        this.pageID = pageID;
        this.roleID = roleID;
    }

    // Getters y setters
    public int getPageRoleID() {
        return pageRoleID;
    }

    public void setPageRoleID(int pageRoleID) {
        this.pageRoleID = pageRoleID;
    }

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