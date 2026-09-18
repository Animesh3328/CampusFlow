package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Faculty extends User {

    private String department;

    protected Faculty() {
        super();
    }

    public Faculty(String name, String email, String password, String department) {
        super(name, email, password, UserRole.FACULTY);
        this.department = department;
    }

    @Override
    public String getRoleDescription() {
        return "Faculty";
    }

    public String getDepartment() {
        return department;
    }
}
