package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Laboratory extends Resource {

    private int computerCount;

    protected Laboratory() {
        super();
    }

    public Laboratory(String name, String location, int capacity, int computerCount) {
        super(name, location, capacity, ResourceType.LABORATORY);
        this.computerCount = computerCount;
    }

    @Override
    public double calculateBookingCost() {
        return 250.0;
    }

    public int getComputerCount() {
        return computerCount;
    }
}
