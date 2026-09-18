package com.campusflow.service;

import com.campusflow.exception.ValidationException;
import com.campusflow.model.*;
import com.campusflow.repository.EventRepository;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class EventService {

    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    public EventService(EventRepository eventRepository,
                        NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    public void createEvent(String title, String description,
                            java.time.LocalDate date, String venue,
                            int capacity, EventCategory category)
            throws ValidationException {

        if (title == null || title.isBlank()) {
            throw new ValidationException("Event title is required.");
        }
        if (date == null || date.isBefore(java.time.LocalDate.now())) {
            throw new ValidationException("Event date must be today or later.");
        }
        if (capacity <= 0) {
            throw new ValidationException("Capacity must be positive.");
        }

        eventRepository.save(
                new CampusEvent(title, description, date, venue, capacity, category));
    }

    public List<CampusEvent> listEvents() {
        return eventRepository.findAll();
    }

    public void register(User user, Long eventId) throws ValidationException {
        CampusEvent event = eventRepository.findById(eventId);

        if (event == null) {
            throw new ValidationException("Event not found.");
        }

        EntityManager em = JPAUtil.openEntityManager();
        try {
            Long count = em.createQuery("""
                    select count(r) from EventRegistration r
                    where r.eventId = :event
                    """, Long.class)
                    .setParameter("event", eventId)
                    .getSingleResult();

            if (count >= event.getCapacity()) {
                throw new ValidationException("Event capacity has been reached.");
            }

            Long existing = em.createQuery("""
                    select count(r) from EventRegistration r
                    where r.eventId = :event and r.userId = :user
                    """, Long.class)
                    .setParameter("event", eventId)
                    .setParameter("user", user.getId())
                    .getSingleResult();

            if (existing > 0) {
                throw new ValidationException("You are already registered for this event.");
            }

            em.getTransaction().begin();
            em.persist(new EventRegistration(eventId, user.getId()));
            em.getTransaction().commit();

            notificationService.send(user.getId(),
                    "Registration confirmed for " + event.getTitle());
        } finally {
            em.close();
        }
    }
}
