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
import modele.entity.Employee;
import modele.entity.RetailerCommands;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.Retailer;

/**
 *
 * @author yo
 */
public class RetailerJpaController implements Serializable {
    
    private static RetailerJpaController singleton = null;
    
    
    public static RetailerJpaController getController(){
        if(singleton==null) singleton = new RetailerJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private RetailerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Retailer retailer) {
        if (retailer.getRetailerCommandsCollection() == null) {
            retailer.setRetailerCommandsCollection(new ArrayList<RetailerCommands>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employeeId = retailer.getEmployeeId();
            if (employeeId != null) {
                employeeId = em.getReference(employeeId.getClass(), employeeId.getId());
                retailer.setEmployeeId(employeeId);
            }
            Collection<RetailerCommands> attachedRetailerCommandsCollection = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsCollectionRetailerCommandsToAttach : retailer.getRetailerCommandsCollection()) {
                retailerCommandsCollectionRetailerCommandsToAttach = em.getReference(retailerCommandsCollectionRetailerCommandsToAttach.getClass(), retailerCommandsCollectionRetailerCommandsToAttach.getId());
                attachedRetailerCommandsCollection.add(retailerCommandsCollectionRetailerCommandsToAttach);
            }
            retailer.setRetailerCommandsCollection(attachedRetailerCommandsCollection);
            em.persist(retailer);
            if (employeeId != null) {
                employeeId.getRetailerCollection().add(retailer);
                employeeId = em.merge(employeeId);
            }
            for (RetailerCommands retailerCommandsCollectionRetailerCommands : retailer.getRetailerCommandsCollection()) {
                Retailer oldRetailerIdOfRetailerCommandsCollectionRetailerCommands = retailerCommandsCollectionRetailerCommands.getRetailerId();
                retailerCommandsCollectionRetailerCommands.setRetailerId(retailer);
                retailerCommandsCollectionRetailerCommands = em.merge(retailerCommandsCollectionRetailerCommands);
                if (oldRetailerIdOfRetailerCommandsCollectionRetailerCommands != null) {
                    oldRetailerIdOfRetailerCommandsCollectionRetailerCommands.getRetailerCommandsCollection().remove(retailerCommandsCollectionRetailerCommands);
                    oldRetailerIdOfRetailerCommandsCollectionRetailerCommands = em.merge(oldRetailerIdOfRetailerCommandsCollectionRetailerCommands);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Retailer retailer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Retailer persistentRetailer = em.find(Retailer.class, retailer.getId());
            Employee employeeIdOld = persistentRetailer.getEmployeeId();
            Employee employeeIdNew = retailer.getEmployeeId();
            Collection<RetailerCommands> retailerCommandsCollectionOld = persistentRetailer.getRetailerCommandsCollection();
            Collection<RetailerCommands> retailerCommandsCollectionNew = retailer.getRetailerCommandsCollection();
            List<String> illegalOrphanMessages = null;
            for (RetailerCommands retailerCommandsCollectionOldRetailerCommands : retailerCommandsCollectionOld) {
                if (!retailerCommandsCollectionNew.contains(retailerCommandsCollectionOldRetailerCommands)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RetailerCommands " + retailerCommandsCollectionOldRetailerCommands + " since its retailerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (employeeIdNew != null) {
                employeeIdNew = em.getReference(employeeIdNew.getClass(), employeeIdNew.getId());
                retailer.setEmployeeId(employeeIdNew);
            }
            Collection<RetailerCommands> attachedRetailerCommandsCollectionNew = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsCollectionNewRetailerCommandsToAttach : retailerCommandsCollectionNew) {
                retailerCommandsCollectionNewRetailerCommandsToAttach = em.getReference(retailerCommandsCollectionNewRetailerCommandsToAttach.getClass(), retailerCommandsCollectionNewRetailerCommandsToAttach.getId());
                attachedRetailerCommandsCollectionNew.add(retailerCommandsCollectionNewRetailerCommandsToAttach);
            }
            retailerCommandsCollectionNew = attachedRetailerCommandsCollectionNew;
            retailer.setRetailerCommandsCollection(retailerCommandsCollectionNew);
            retailer = em.merge(retailer);
            if (employeeIdOld != null && !employeeIdOld.equals(employeeIdNew)) {
                employeeIdOld.getRetailerCollection().remove(retailer);
                employeeIdOld = em.merge(employeeIdOld);
            }
            if (employeeIdNew != null && !employeeIdNew.equals(employeeIdOld)) {
                employeeIdNew.getRetailerCollection().add(retailer);
                employeeIdNew = em.merge(employeeIdNew);
            }
            for (RetailerCommands retailerCommandsCollectionNewRetailerCommands : retailerCommandsCollectionNew) {
                if (!retailerCommandsCollectionOld.contains(retailerCommandsCollectionNewRetailerCommands)) {
                    Retailer oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands = retailerCommandsCollectionNewRetailerCommands.getRetailerId();
                    retailerCommandsCollectionNewRetailerCommands.setRetailerId(retailer);
                    retailerCommandsCollectionNewRetailerCommands = em.merge(retailerCommandsCollectionNewRetailerCommands);
                    if (oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands != null && !oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands.equals(retailer)) {
                        oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands.getRetailerCommandsCollection().remove(retailerCommandsCollectionNewRetailerCommands);
                        oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands = em.merge(oldRetailerIdOfRetailerCommandsCollectionNewRetailerCommands);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = retailer.getId();
                if (findRetailer(id) == null) {
                    throw new NonexistentEntityException("The retailer with id " + id + " no longer exists.");
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
            Retailer retailer;
            try {
                retailer = em.getReference(Retailer.class, id);
                retailer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The retailer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RetailerCommands> retailerCommandsCollectionOrphanCheck = retailer.getRetailerCommandsCollection();
            for (RetailerCommands retailerCommandsCollectionOrphanCheckRetailerCommands : retailerCommandsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Retailer (" + retailer + ") cannot be destroyed since the RetailerCommands " + retailerCommandsCollectionOrphanCheckRetailerCommands + " in its retailerCommandsCollection field has a non-nullable retailerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Employee employeeId = retailer.getEmployeeId();
            if (employeeId != null) {
                employeeId.getRetailerCollection().remove(retailer);
                employeeId = em.merge(employeeId);
            }
            em.remove(retailer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Retailer> findRetailerEntities() {
        return findRetailerEntities(true, -1, -1);
    }

    public List<Retailer> findRetailerEntities(int maxResults, int firstResult) {
        return findRetailerEntities(false, maxResults, firstResult);
    }

    private List<Retailer> findRetailerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Retailer.class));
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

    public Retailer findRetailer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Retailer.class, id);
        } finally {
            em.close();
        }
    }

    public int getRetailerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Retailer> rt = cq.from(Retailer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
