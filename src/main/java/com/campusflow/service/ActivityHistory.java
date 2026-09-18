package com.campusflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class ActivityHistory {
    private final List<String> activities = new ArrayList<>();
    private final Vector<String> notifications = new Vector<>();
    private final Stack<String> recentActions = new Stack<>();

    public void record(String action) {
        activities.add(action);
        notifications.add(action);
        recentActions.push(action);
    }

    public List<String> getActivities() {
        return new ArrayList<>(activities);
    }

    public String undoLastAction() {
        return recentActions.empty() ? "No recent action" : recentActions.pop();
    }

    public int notificationCount() {
        return notifications.size();
    }
}
