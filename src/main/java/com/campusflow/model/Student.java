package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Student extends User {

    private String programme;

    protected Student() {
        super();
    }

    public Student(String name, String email, String password, String programme) {
        super(name, email, password, UserRole.STUDENT);
        this.programme = programme;
    }

    @Override
    public String getRoleDescription() {
        return "Student";
    }

    public String getProgramme() {
        return programme;
    }
}
