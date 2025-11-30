package by.rozze.weblab3.hibernate;

import by.rozze.weblab3.model.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Logger;

public class HibernateUtil {
    private static final Logger log = Logger.getLogger(String.valueOf(HibernateUtil.class));
    private static final EntityManagerFactory emf;

    static {
        try {
            log.info("Инициализация JPA EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("webLab3PU");
            EntityManager em = emf.createEntityManager();
            try {
                String dbUrl = em.unwrap(org.hibernate.Session.class)
                        .doReturningWork(connection -> connection.getMetaData().getURL());
                String dbProduct = em.unwrap(org.hibernate.Session.class)
                        .doReturningWork(connection -> connection.getMetaData().getDatabaseProductName());
            } finally {
                em.close();
            }
        } catch (Exception e) {
            log.severe("Ошибка создания JPA EntityManagerFactory");
            throw new RuntimeException(e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        log.info("Закрытие JPA EntityManagerFactory");
        emf.close();
    }
}