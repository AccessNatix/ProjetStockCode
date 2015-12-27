/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.jpacontroller;

import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modele.entity.Session;
import modele.entity.SessionTransactions;
import modele.entity.Transaction;

/**
 *
 * @author yo
 */
public class SessionTransactionsJpaController implements Serializable {

    private static SessionTransactionsJpaController singleton = null;
    
    
    public static SessionTransactionsJpaController getController(){
        if(singleton==null) singleton = new SessionTransactionsJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private SessionTransactionsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SessionTransactions sessionTransactions) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Session sessionId = sessionTransactions.getSessionId();
            if (sessionId != null) {
                sessionId = em.getReference(sessionId.getClass(), sessionId.getId());
                sessionTransactions.setSessionId(sessionId);
            }
            Transaction transactionId = sessionTransactions.getTransactionId();
            if (transactionId != null) {
                transactionId = em.getReference(transactionId.getClass(), transactionId.getId());
                sessionTransactions.setTransactionId(transactionId);
            }
            em.persist(sessionTransactions);
            if (sessionId != null) {
                sessionId.getSessionTransactionsCollection().add(sessionTransactions);
                sessionId = em.merge(sessionId);
            }
            if (transactionId != null) {
                transactionId.getSessionTransactionsCollection().add(sessionTransactions);
                transactionId = em.merge(transactionId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SessionTransactions sessionTransactions) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SessionTransactions persistentSessionTransactions = em.find(SessionTransactions.class, sessionTransactions.getId());
            Session sessionIdOld = persistentSessionTransactions.getSessionId();
            Session sessionIdNew = sessionTransactions.getSessionId();
            Transaction transactionIdOld = persistentSessionTransactions.getTransactionId();
            Transaction transactionIdNew = sessionTransactions.getTransactionId();
            if (sessionIdNew != null) {
                sessionIdNew = em.getReference(sessionIdNew.getClass(), sessionIdNew.getId());
                sessionTransactions.setSessionId(sessionIdNew);
            }
            if (transactionIdNew != null) {
                transactionIdNew = em.getReference(transactionIdNew.getClass(), transactionIdNew.getId());
                sessionTransactions.setTransactionId(transactionIdNew);
            }
            sessionTransactions = em.merge(sessionTransactions);
            if (sessionIdOld != null && !sessionIdOld.equals(sessionIdNew)) {
                sessionIdOld.getSessionTransactionsCollection().remove(sessionTransactions);
                sessionIdOld = em.merge(sessionIdOld);
            }
            if (sessionIdNew != null && !sessionIdNew.equals(sessionIdOld)) {
                sessionIdNew.getSessionTransactionsCollection().add(sessionTransactions);
                sessionIdNew = em.merge(sessionIdNew);
            }
            if (transactionIdOld != null && !transactionIdOld.equals(transactionIdNew)) {
                transactionIdOld.getSessionTransactionsCollection().remove(sessionTransactions);
                transactionIdOld = em.merge(transactionIdOld);
            }
            if (transactionIdNew != null && !transactionIdNew.equals(transactionIdOld)) {
                transactionIdNew.getSessionTransactionsCollection().add(sessionTransactions);
                transactionIdNew = em.merge(transactionIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sessionTransactions.getId();
                if (findSessionTransactions(id) == null) {
                    throw new NonexistentEntityException("The sessionTransactions with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SessionTransactions sessionTransactions;
            try {
                sessionTransactions = em.getReference(SessionTransactions.class, id);
                sessionTransactions.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sessionTransactions with id " + id + " no longer exists.", enfe);
            }
            Session sessionId = sessionTransactions.getSessionId();
            if (sessionId != null) {
                sessionId.getSessionTransactionsCollection().remove(sessionTransactions);
                sessionId = em.merge(sessionId);
            }
            Transaction transactionId = sessionTransactions.getTransactionId();
            if (transactionId != null) {
                transactionId.getSessionTransactionsCollection().remove(sessionTransactions);
                transactionId = em.merge(transactionId);
            }
            em.remove(sessionTransactions);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SessionTransactions> findSessionTransactionsEntities() {
        return findSessionTransactionsEntities(true, -1, -1);
    }

    public List<SessionTransactions> findSessionTransactionsEntities(int maxResults, int firstResult) {
        return findSessionTransactionsEntities(false, maxResults, firstResult);
    }

    private List<SessionTransactions> findSessionTransactionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SessionTransactions.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public SessionTransactions findSessionTransactions(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SessionTransactions.class, id);
        } finally {
            em.close();
        }
    }

    public int getSessionTransactionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SessionTransactions> rt = cq.from(SessionTransactions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<SessionTransactions> findBySessionId(int sessionId){
        return getEntityManager()
            .createNamedQuery("SessionTransactions.findBySessionId")
            .setParameter("sessionId", sessionId)
            .getResultList();
    }
    
    public SessionTransactions findByIds(int sessionId, int transactionId){
        List<SessionTransactions> l = getEntityManager()
            .createNamedQuery("SessionTransactions.findByIds")
            .setParameter("transactionId", transactionId)
            .setParameter("sessionId", sessionId)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
    public void reloveAll(){
        getEntityManager().createNamedQuery("SessionTransactions.removeAll").executeUpdate();
    }
    
}
