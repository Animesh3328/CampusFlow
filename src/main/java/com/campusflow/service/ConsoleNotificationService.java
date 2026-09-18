package com.campusflow.service;

import com.campusflow.model.Notification;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void send(Long userId, String message) {
        System.out.println("\n[Notification] " + message);

        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Notification(userId, message));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
