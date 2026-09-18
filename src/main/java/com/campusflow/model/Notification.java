package com.campusflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;

    protected Notification() {
    }

    public Notification(Long userId, String message) {
        this.userId = userId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isReadStatus() { return readStatus; }

    public void markRead() {
        readStatus = true;
    }
}
