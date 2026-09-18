package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    protected Admin() {
        super();
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, UserRole.ADMIN);
    }

    @Override
    public String getRoleDescription() {
        return "Administrator";
    }
}
