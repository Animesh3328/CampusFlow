package com.campusflow.repository;

import com.campusflow.model.CampusEvent;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class EventRepository {

    public void save(CampusEvent event) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<CampusEvent> findAll() {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.createQuery(
                    "select e from CampusEvent e order by e.eventDate", CampusEvent.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public CampusEvent findById(Long id) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.find(CampusEvent.class, id);
        } finally {
            em.close();
        }
    }
}
