package com.campusflow.model;

import jakarta.persistence.Entity;

@Entity
public class Equipment extends Resource {

    private String equipmentCondition;

    protected Equipment() {
        super();
    }

    public Equipment(String name, String location, int capacity, String equipmentCondition) {
        super(name, location, capacity, ResourceType.EQUIPMENT);
        this.equipmentCondition = equipmentCondition;
    }

    @Override
    public double calculateBookingCost() {
        return 50.0;
    }

    public String getEquipmentCondition() {
        return equipmentCondition;
    }
}
