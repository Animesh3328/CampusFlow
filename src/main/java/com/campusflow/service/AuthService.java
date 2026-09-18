package com.campusflow.service;

import com.campusflow.model.*;
import com.campusflow.repository.UserRepository;
import com.campusflow.exception.ValidationException;

public class AuthService {

    private final UserRepository repository;

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public User registerStudent(String name, String email, String password, String programme)
            throws ValidationException {
        validate(name, email, password);
        if (repository.findByEmail(email) != null) {
            throw new ValidationException("An account with this email already exists.");
        }
        Student student = new Student(name, email, password, programme);
        repository.save(student);
        return student;
    }

    public User authenticate(String email, String password) {
        User user = repository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    private void validate(String name, String email, String password)
            throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Enter a valid email address.");
        }
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must contain at least 6 characters.");
        }
    }
}
