package by.rozze.weblab3.hibernate;

import by.rozze.weblab3.model.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ResultDao {
    public void save(Result result) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(result);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}
