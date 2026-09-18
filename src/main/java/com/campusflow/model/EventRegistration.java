package com.campusflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations")
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private Long userId;
    private LocalDateTime registeredAt;

    protected EventRegistration() {
    }

    public EventRegistration(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.registeredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public Long getUserId() { return userId; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
