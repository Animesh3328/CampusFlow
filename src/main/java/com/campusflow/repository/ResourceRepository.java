package com.campusflow.repository;

import com.campusflow.model.Resource;
import com.campusflow.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ResourceRepository {

    public void save(Resource resource) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(resource);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Resource> findAll() {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.createQuery("select r from Resource r order by r.id", Resource.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Resource findById(Long id) {
        EntityManager em = JPAUtil.openEntityManager();
        try {
            return em.find(Resource.class, id);
        } finally {
            em.close();
        }
    }
}
