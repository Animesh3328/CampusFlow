package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Classroom extends Resource {

    private boolean smartBoard;

    protected Classroom() {
        super();
    }

    public Classroom(String name, String location, int capacity, boolean smartBoard) {
        super(name, location, capacity, ResourceType.CLASSROOM);
        this.smartBoard = smartBoard;
    }

    @Override
    public double calculateBookingCost() {
        return 100.0;
    }

    public boolean hasSmartBoard() {
        return smartBoard;
    }
}
