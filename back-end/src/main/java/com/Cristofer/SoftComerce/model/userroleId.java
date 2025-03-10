package com.Cristofer.SoftComerce.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class userroleId implements Serializable {

    private int userID;
    private int roleID;

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

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        userroleId that = (userroleId) o;
        return userID == that.userID && roleID == that.roleID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, roleID);
    }
}