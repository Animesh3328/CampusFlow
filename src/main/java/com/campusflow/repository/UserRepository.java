package com.campusflow.repository;

import com.campusflow.model.User;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class UserRepository {

    public void save(User user) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            List<User> users = em.createQuery(
                    "select u from User u where u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally {
            em.close();
        }
    }

    public List<User> findAll() {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.createQuery("select u from User u order by u.id", User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
