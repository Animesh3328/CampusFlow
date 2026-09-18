package com.campusflow.util;

import jakarta.persistence.*;

public final class JPAUtil {

    private static EntityManagerFactory factory;

    private JPAUtil() {
    }

    public static synchronized EntityManagerFactory getFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("campusflowPU");
        }
        return factory;
    }

    public static EntityManager openEntityManager() {
        return getFactory().createEntityManager();
    }

    public static void close() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }
}
