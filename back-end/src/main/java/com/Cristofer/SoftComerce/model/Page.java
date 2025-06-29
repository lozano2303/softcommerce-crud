package com.Cristofer.SoftComerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "page")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pageID")
    private int pageID;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    // Constructor sin parámetros
    public Page() {
    }

    // Constructor con todos los campos
    public Page(int pageID, String name) {
        this.pageID = pageID;
        this.name = name;
    }

    // Getters y setters
    public int getPageID() {
        return pageID;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}