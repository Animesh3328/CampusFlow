package com.campusflow.model;

import java.util.*;

public class ResourceHierarchy {

    private final Map<String, List<Resource>> groups = new LinkedHashMap<>();

    public void add(String category, Resource resource) {
        groups.computeIfAbsent(category, key -> new ArrayList<>()).add(resource);
    }

    public void printRecursively(List<Resource> resources, int index) {
        if (index >= resources.size()) {
            return;
        }
        System.out.println("  - " + resources.get(index).getName());
        printRecursively(resources, index + 1);
    }

    public void display() {
        for (Map.Entry<String, List<Resource>> entry : groups.entrySet()) {
            System.out.println(entry.getKey());
            printRecursively(entry.getValue(), 0);
        }
    }
}
