/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.jpacontroller;

import controller.jpacontroller.exceptions.IllegalOrphanException;
import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modele.entity.SessionTransactions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import modele.entity.Session;

/**
 *
 * @author yo
 */
public class SessionJpaController implements Serializable {

    private static SessionJpaController singleton = null;
    
    
    public static SessionJpaController getController(){
        if(singleton==null) singleton = new SessionJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private SessionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Session session) {
        if (session.getSessionTransactionsCollection() == null) {
            session.setSessionTransactionsCollection(new ArrayList<SessionTransactions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SessionTransactions> attachedSessionTransactionsCollection = new ArrayList<SessionTransactions>();
            for (SessionTransactions sessionTransactionsCollectionSessionTransactionsToAttach : session.getSessionTransactionsCollection()) {
                sessionTransactionsCollectionSessionTransactionsToAttach = em.getReference(sessionTransactionsCollectionSessionTransactionsToAttach.getClass(), sessionTransactionsCollectionSessionTransactionsToAttach.getId());
                attachedSessionTransactionsCollection.add(sessionTransactionsCollectionSessionTransactionsToAttach);
            }
            session.setSessionTransactionsCollection(attachedSessionTransactionsCollection);
            em.persist(session);
            for (SessionTransactions sessionTransactionsCollectionSessionTransactions : session.getSessionTransactionsCollection()) {
                Session oldSessionIdOfSessionTransactionsCollectionSessionTransactions = sessionTransactionsCollectionSessionTransactions.getSessionId();
                sessionTransactionsCollectionSessionTransactions.setSessionId(session);
                sessionTransactionsCollectionSessionTransactions = em.merge(sessionTransactionsCollectionSessionTransactions);
                if (oldSessionIdOfSessionTransactionsCollectionSessionTransactions != null) {
                    oldSessionIdOfSessionTransactionsCollectionSessionTransactions.getSessionTransactionsCollection().remove(sessionTransactionsCollectionSessionTransactions);
                    oldSessionIdOfSessionTransactionsCollectionSessionTransactions = em.merge(oldSessionIdOfSessionTransactionsCollectionSessionTransactions);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Session session) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Session persistentSession = em.find(Session.class, session.getId());
            Collection<SessionTransactions> sessionTransactionsCollectionOld = persistentSession.getSessionTransactionsCollection();
            Collection<SessionTransactions> sessionTransactionsCollectionNew = session.getSessionTransactionsCollection();
            List<String> illegalOrphanMessages = null;
            for (SessionTransactions sessionTransactionsCollectionOldSessionTransactions : sessionTransactionsCollectionOld) {
                if (!sessionTransactionsCollectionNew.contains(sessionTransactionsCollectionOldSessionTransactions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SessionTransactions " + sessionTransactionsCollectionOldSessionTransactions + " since its sessionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SessionTransactions> attachedSessionTransactionsCollectionNew = new ArrayList<SessionTransactions>();
            for (SessionTransactions sessionTransactionsCollectionNewSessionTransactionsToAttach : sessionTransactionsCollectionNew) {
                sessionTransactionsCollectionNewSessionTransactionsToAttach = em.getReference(sessionTransactionsCollectionNewSessionTransactionsToAttach.getClass(), sessionTransactionsCollectionNewSessionTransactionsToAttach.getId());
                attachedSessionTransactionsCollectionNew.add(sessionTransactionsCollectionNewSessionTransactionsToAttach);
            }
            sessionTransactionsCollectionNew = attachedSessionTransactionsCollectionNew;
            session.setSessionTransactionsCollection(sessionTransactionsCollectionNew);
            session = em.merge(session);
            for (SessionTransactions sessionTransactionsCollectionNewSessionTransactions : sessionTransactionsCollectionNew) {
                if (!sessionTransactionsCollectionOld.contains(sessionTransactionsCollectionNewSessionTransactions)) {
                    Session oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions = sessionTransactionsCollectionNewSessionTransactions.getSessionId();
                    sessionTransactionsCollectionNewSessionTransactions.setSessionId(session);
                    sessionTransactionsCollectionNewSessionTransactions = em.merge(sessionTransactionsCollectionNewSessionTransactions);
                    if (oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions != null && !oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions.equals(session)) {
                        oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions.getSessionTransactionsCollection().remove(sessionTransactionsCollectionNewSessionTransactions);
                        oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions = em.merge(oldSessionIdOfSessionTransactionsCollectionNewSessionTransactions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = session.getId();
                if (findSession(id) == null) {
                    throw new NonexistentEntityException("The session with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Session session;
            try {
                session = em.getReference(Session.class, id);
                session.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The session with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SessionTransactions> sessionTransactionsCollectionOrphanCheck = session.getSessionTransactionsCollection();
            for (SessionTransactions sessionTransactionsCollectionOrphanCheckSessionTransactions : sessionTransactionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Session (" + session + ") cannot be destroyed since the SessionTransactions " + sessionTransactionsCollectionOrphanCheckSessionTransactions + " in its sessionTransactionsCollection field has a non-nullable sessionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(session);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Session> findSessionEntities() {
        return findSessionEntities(true, -1, -1);
    }

    public List<Session> findSessionEntities(int maxResults, int firstResult) {
        return findSessionEntities(false, maxResults, firstResult);
    }

    private List<Session> findSessionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Session.class));
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

    public Session findSession(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Session.class, id);
        } finally {
            em.close();
        }
    }

    public int getSessionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Session> rt = cq.from(Session.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Session findByConnexion(String pseudo, String password){
        List<Session> l = getEntityManager().createNamedQuery("Session.findByConnexion")
            .setParameter("pseudo", pseudo)
            .setParameter("password", password)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
    
    public void reloveAll(){
        EntityManager em = getEntityManager();
        EntityTransaction transac = em.getTransaction();
        transac.begin();
        em.createNamedQuery("Session.removeAll").executeUpdate();
        transac.commit();
        em.close();
    }
}
