package com.campusflow.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "events")
public class CampusEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate eventDate;
    private String venue;
    private int capacity;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    protected CampusEvent() {
    }

    public CampusEvent(String title, String description, LocalDate eventDate,
                        String venue, int capacity, EventCategory category) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.venue = venue;
        this.capacity = capacity;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getEventDate() { return eventDate; }
    public String getVenue() { return venue; }
    public int getCapacity() { return capacity; }
    public EventCategory getCategory() { return category; }

    @Override
    public String toString() {
        return id + " | " + title + " | " + category + " | " +
                eventDate + " | " + venue + " | capacity=" + capacity;
    }
}
