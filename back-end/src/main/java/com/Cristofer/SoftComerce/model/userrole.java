package com.Cristofer.SoftComerce.model;

import jakarta.persistence.*;

@Entity(name = "userrole")
public class userrole {

    @EmbeddedId
    private userroleId id; // Clave primaria compuesta

    @ManyToOne
    @MapsId("userID") // Mapea la parte de userID de la clave compuesta
    @JoinColumn(name = "userID")
    private user user;

    @ManyToOne
    @MapsId("roleID") // Mapea la parte de roleID de la clave compuesta
    @JoinColumn(name = "roleID")
    private role role;

    // Getters y Setters
    public userroleId getId() {
        return id;
    }

    public void setId(userroleId id) {
        this.id = id;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }
}