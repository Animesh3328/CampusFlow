package com.campusflow.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private int capacity;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    private boolean available = true;

    protected Resource() {
    }

    protected Resource(String name, String location, int capacity, ResourceType type) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.type = type;
    }

    public abstract double calculateBookingCost();

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public ResourceType getType() { return type; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + type + " | " + location +
                " | capacity=" + capacity + " | available=" + available;
    }
}
