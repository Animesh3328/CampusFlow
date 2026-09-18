package com.campusflow.repository;

import com.campusflow.model.Booking;
import com.campusflow.model.BookingStatus;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class BookingRepository {

    public void save(Booking booking) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Booking> findForResource(Long resourceId, LocalDate date) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.createQuery("""
                    select b from Booking b
                    where b.resourceId = :resource
                    and b.bookingDate = :date
                    and b.status = :status
                    order by b.startTime
                    """, Booking.class)
                    .setParameter("resource", resourceId)
                    .setParameter("date", date)
                    .setParameter("status", BookingStatus.CONFIRMED)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Booking> findForUser(Long userId) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.createQuery("""
                    select b from Booking b
                    where b.userId = :user
                    order by b.bookingDate desc
                    """, Booking.class)
                    .setParameter("user", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Booking findById(Long id) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Booking booking) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(booking);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
