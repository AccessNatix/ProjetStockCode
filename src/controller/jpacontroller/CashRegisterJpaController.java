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
import modele.entity.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.CashRegister;

/**
 *
 * @author yo
 */
public class CashRegisterJpaController implements Serializable {
    
    private static CashRegisterJpaController singleton = null;
    
    
    public static CashRegisterJpaController getController(){
        if(singleton==null) singleton = new CashRegisterJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private CashRegisterJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CashRegister cashRegister) {
        if (cashRegister.getKeyCollection() == null) {
            cashRegister.setKeyCollection(new ArrayList<Key>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Key> attachedKeyCollection = new ArrayList<Key>();
            for (Key keyCollectionKeyToAttach : cashRegister.getKeyCollection()) {
                keyCollectionKeyToAttach = em.getReference(keyCollectionKeyToAttach.getClass(), keyCollectionKeyToAttach.getId());
                attachedKeyCollection.add(keyCollectionKeyToAttach);
            }
            cashRegister.setKeyCollection(attachedKeyCollection);
            em.persist(cashRegister);
            for (Key keyCollectionKey : cashRegister.getKeyCollection()) {
                CashRegister oldCashRegisteridOfKeyCollectionKey = keyCollectionKey.getCashRegisterid();
                keyCollectionKey.setCashRegisterid(cashRegister);
                keyCollectionKey = em.merge(keyCollectionKey);
                if (oldCashRegisteridOfKeyCollectionKey != null) {
                    oldCashRegisteridOfKeyCollectionKey.getKeyCollection().remove(keyCollectionKey);
                    oldCashRegisteridOfKeyCollectionKey = em.merge(oldCashRegisteridOfKeyCollectionKey);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CashRegister cashRegister) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CashRegister persistentCashRegister = em.find(CashRegister.class, cashRegister.getId());
            Collection<Key> keyCollectionOld = persistentCashRegister.getKeyCollection();
            Collection<Key> keyCollectionNew = cashRegister.getKeyCollection();
            List<String> illegalOrphanMessages = null;
            for (Key keyCollectionOldKey : keyCollectionOld) {
                if (!keyCollectionNew.contains(keyCollectionOldKey)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Key " + keyCollectionOldKey + " since its cashRegisterid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Key> attachedKeyCollectionNew = new ArrayList<Key>();
            for (Key keyCollectionNewKeyToAttach : keyCollectionNew) {
                keyCollectionNewKeyToAttach = em.getReference(keyCollectionNewKeyToAttach.getClass(), keyCollectionNewKeyToAttach.getId());
                attachedKeyCollectionNew.add(keyCollectionNewKeyToAttach);
            }
            keyCollectionNew = attachedKeyCollectionNew;
            cashRegister.setKeyCollection(keyCollectionNew);
            cashRegister = em.merge(cashRegister);
            for (Key keyCollectionNewKey : keyCollectionNew) {
                if (!keyCollectionOld.contains(keyCollectionNewKey)) {
                    CashRegister oldCashRegisteridOfKeyCollectionNewKey = keyCollectionNewKey.getCashRegisterid();
                    keyCollectionNewKey.setCashRegisterid(cashRegister);
                    keyCollectionNewKey = em.merge(keyCollectionNewKey);
                    if (oldCashRegisteridOfKeyCollectionNewKey != null && !oldCashRegisteridOfKeyCollectionNewKey.equals(cashRegister)) {
                        oldCashRegisteridOfKeyCollectionNewKey.getKeyCollection().remove(keyCollectionNewKey);
                        oldCashRegisteridOfKeyCollectionNewKey = em.merge(oldCashRegisteridOfKeyCollectionNewKey);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cashRegister.getId();
                if (findCashRegister(id) == null) {
                    throw new NonexistentEntityException("The cashRegister with id " + id + " no longer exists.");
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
            CashRegister cashRegister;
            try {
                cashRegister = em.getReference(CashRegister.class, id);
                cashRegister.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cashRegister with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Key> keyCollectionOrphanCheck = cashRegister.getKeyCollection();
            for (Key keyCollectionOrphanCheckKey : keyCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CashRegister (" + cashRegister + ") cannot be destroyed since the Key " + keyCollectionOrphanCheckKey + " in its keyCollection field has a non-nullable cashRegisterid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cashRegister);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CashRegister> findCashRegisterEntities() {
        return findCashRegisterEntities(true, -1, -1);
    }

    public List<CashRegister> findCashRegisterEntities(int maxResults, int firstResult) {
        return findCashRegisterEntities(false, maxResults, firstResult);
    }

    private List<CashRegister> findCashRegisterEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CashRegister.class));
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

    public CashRegister findCashRegister(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CashRegister.class, id);
        } finally {
            em.close();
        }
    }

    public int getCashRegisterCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CashRegister> rt = cq.from(CashRegister.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
