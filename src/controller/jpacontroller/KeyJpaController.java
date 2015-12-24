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
import modele.entity.CashRegister;
import modele.entity.Key;

/**
 *
 * @author yo
 */
public class KeyJpaController implements Serializable {
    
    private static KeyJpaController singleton = null;
    
    
    public static KeyJpaController getController(){
        if(singleton==null) singleton = new KeyJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private KeyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Key key) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CashRegister cashRegisterid = key.getCashRegisterid();
            if (cashRegisterid != null) {
                cashRegisterid = em.getReference(cashRegisterid.getClass(), cashRegisterid.getId());
                key.setCashRegisterid(cashRegisterid);
            }
            em.persist(key);
            if (cashRegisterid != null) {
                cashRegisterid.getKeyCollection().add(key);
                cashRegisterid = em.merge(cashRegisterid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Key key) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Key persistentKey = em.find(Key.class, key.getId());
            CashRegister cashRegisteridOld = persistentKey.getCashRegisterid();
            CashRegister cashRegisteridNew = key.getCashRegisterid();
            if (cashRegisteridNew != null) {
                cashRegisteridNew = em.getReference(cashRegisteridNew.getClass(), cashRegisteridNew.getId());
                key.setCashRegisterid(cashRegisteridNew);
            }
            key = em.merge(key);
            if (cashRegisteridOld != null && !cashRegisteridOld.equals(cashRegisteridNew)) {
                cashRegisteridOld.getKeyCollection().remove(key);
                cashRegisteridOld = em.merge(cashRegisteridOld);
            }
            if (cashRegisteridNew != null && !cashRegisteridNew.equals(cashRegisteridOld)) {
                cashRegisteridNew.getKeyCollection().add(key);
                cashRegisteridNew = em.merge(cashRegisteridNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = key.getId();
                if (findKey(id) == null) {
                    throw new NonexistentEntityException("The key with id " + id + " no longer exists.");
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
            Key key;
            try {
                key = em.getReference(Key.class, id);
                key.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The key with id " + id + " no longer exists.", enfe);
            }
            CashRegister cashRegisterid = key.getCashRegisterid();
            if (cashRegisterid != null) {
                cashRegisterid.getKeyCollection().remove(key);
                cashRegisterid = em.merge(cashRegisterid);
            }
            em.remove(key);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Key> findKeyEntities() {
        return findKeyEntities(true, -1, -1);
    }

    public List<Key> findKeyEntities(int maxResults, int firstResult) {
        return findKeyEntities(false, maxResults, firstResult);
    }

    private List<Key> findKeyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Key.class));
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

    public Key findKey(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Key.class, id);
        } finally {
            em.close();
        }
    }

    public int getKeyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Key> rt = cq.from(Key.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Key findByCashRegisterId(int cashRegisterId){
        List<Key> l = getEntityManager()
            .createNamedQuery("Key.findByCashRegisterId")
            .setParameter("cashRegisterId", cashRegisterId)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
}
