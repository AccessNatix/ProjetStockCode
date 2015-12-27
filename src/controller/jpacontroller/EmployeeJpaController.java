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
import modele.entity.Retailer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.Cashier;
import modele.entity.Employee;

/**
 *
 * @author yo
 */
public class EmployeeJpaController implements Serializable {

    private static EmployeeJpaController singleton = null;
    
    
    public static EmployeeJpaController getController(){
        if(singleton==null) singleton = new EmployeeJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private EmployeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) {
        if (employee.getRetailerCollection() == null) {
            employee.setRetailerCollection(new ArrayList<Retailer>());
        }
        if (employee.getCashierCollection() == null) {
            employee.setCashierCollection(new ArrayList<Cashier>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Retailer> attachedRetailerCollection = new ArrayList<Retailer>();
            for (Retailer retailerCollectionRetailerToAttach : employee.getRetailerCollection()) {
                retailerCollectionRetailerToAttach = em.getReference(retailerCollectionRetailerToAttach.getClass(), retailerCollectionRetailerToAttach.getId());
                attachedRetailerCollection.add(retailerCollectionRetailerToAttach);
            }
            employee.setRetailerCollection(attachedRetailerCollection);
            Collection<Cashier> attachedCashierCollection = new ArrayList<Cashier>();
            for (Cashier cashierCollectionCashierToAttach : employee.getCashierCollection()) {
                cashierCollectionCashierToAttach = em.getReference(cashierCollectionCashierToAttach.getClass(), cashierCollectionCashierToAttach.getId());
                attachedCashierCollection.add(cashierCollectionCashierToAttach);
            }
            employee.setCashierCollection(attachedCashierCollection);
            em.persist(employee);
            for (Retailer retailerCollectionRetailer : employee.getRetailerCollection()) {
                Employee oldEmployeeIdOfRetailerCollectionRetailer = retailerCollectionRetailer.getEmployeeId();
                retailerCollectionRetailer.setEmployeeId(employee);
                retailerCollectionRetailer = em.merge(retailerCollectionRetailer);
                if (oldEmployeeIdOfRetailerCollectionRetailer != null) {
                    oldEmployeeIdOfRetailerCollectionRetailer.getRetailerCollection().remove(retailerCollectionRetailer);
                    oldEmployeeIdOfRetailerCollectionRetailer = em.merge(oldEmployeeIdOfRetailerCollectionRetailer);
                }
            }
            for (Cashier cashierCollectionCashier : employee.getCashierCollection()) {
                Employee oldEmployeeIdOfCashierCollectionCashier = cashierCollectionCashier.getEmployeeId();
                cashierCollectionCashier.setEmployeeId(employee);
                cashierCollectionCashier = em.merge(cashierCollectionCashier);
                if (oldEmployeeIdOfCashierCollectionCashier != null) {
                    oldEmployeeIdOfCashierCollectionCashier.getCashierCollection().remove(cashierCollectionCashier);
                    oldEmployeeIdOfCashierCollectionCashier = em.merge(oldEmployeeIdOfCashierCollectionCashier);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            Collection<Retailer> retailerCollectionOld = persistentEmployee.getRetailerCollection();
            Collection<Retailer> retailerCollectionNew = employee.getRetailerCollection();
            Collection<Cashier> cashierCollectionOld = persistentEmployee.getCashierCollection();
            Collection<Cashier> cashierCollectionNew = employee.getCashierCollection();
            List<String> illegalOrphanMessages = null;
            for (Retailer retailerCollectionOldRetailer : retailerCollectionOld) {
                if (!retailerCollectionNew.contains(retailerCollectionOldRetailer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Retailer " + retailerCollectionOldRetailer + " since its employeeId field is not nullable.");
                }
            }
            for (Cashier cashierCollectionOldCashier : cashierCollectionOld) {
                if (!cashierCollectionNew.contains(cashierCollectionOldCashier)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cashier " + cashierCollectionOldCashier + " since its employeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Retailer> attachedRetailerCollectionNew = new ArrayList<Retailer>();
            for (Retailer retailerCollectionNewRetailerToAttach : retailerCollectionNew) {
                retailerCollectionNewRetailerToAttach = em.getReference(retailerCollectionNewRetailerToAttach.getClass(), retailerCollectionNewRetailerToAttach.getId());
                attachedRetailerCollectionNew.add(retailerCollectionNewRetailerToAttach);
            }
            retailerCollectionNew = attachedRetailerCollectionNew;
            employee.setRetailerCollection(retailerCollectionNew);
            Collection<Cashier> attachedCashierCollectionNew = new ArrayList<Cashier>();
            for (Cashier cashierCollectionNewCashierToAttach : cashierCollectionNew) {
                cashierCollectionNewCashierToAttach = em.getReference(cashierCollectionNewCashierToAttach.getClass(), cashierCollectionNewCashierToAttach.getId());
                attachedCashierCollectionNew.add(cashierCollectionNewCashierToAttach);
            }
            cashierCollectionNew = attachedCashierCollectionNew;
            employee.setCashierCollection(cashierCollectionNew);
            employee = em.merge(employee);
            for (Retailer retailerCollectionNewRetailer : retailerCollectionNew) {
                if (!retailerCollectionOld.contains(retailerCollectionNewRetailer)) {
                    Employee oldEmployeeIdOfRetailerCollectionNewRetailer = retailerCollectionNewRetailer.getEmployeeId();
                    retailerCollectionNewRetailer.setEmployeeId(employee);
                    retailerCollectionNewRetailer = em.merge(retailerCollectionNewRetailer);
                    if (oldEmployeeIdOfRetailerCollectionNewRetailer != null && !oldEmployeeIdOfRetailerCollectionNewRetailer.equals(employee)) {
                        oldEmployeeIdOfRetailerCollectionNewRetailer.getRetailerCollection().remove(retailerCollectionNewRetailer);
                        oldEmployeeIdOfRetailerCollectionNewRetailer = em.merge(oldEmployeeIdOfRetailerCollectionNewRetailer);
                    }
                }
            }
            for (Cashier cashierCollectionNewCashier : cashierCollectionNew) {
                if (!cashierCollectionOld.contains(cashierCollectionNewCashier)) {
                    Employee oldEmployeeIdOfCashierCollectionNewCashier = cashierCollectionNewCashier.getEmployeeId();
                    cashierCollectionNewCashier.setEmployeeId(employee);
                    cashierCollectionNewCashier = em.merge(cashierCollectionNewCashier);
                    if (oldEmployeeIdOfCashierCollectionNewCashier != null && !oldEmployeeIdOfCashierCollectionNewCashier.equals(employee)) {
                        oldEmployeeIdOfCashierCollectionNewCashier.getCashierCollection().remove(cashierCollectionNewCashier);
                        oldEmployeeIdOfCashierCollectionNewCashier = em.merge(oldEmployeeIdOfCashierCollectionNewCashier);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Retailer> retailerCollectionOrphanCheck = employee.getRetailerCollection();
            for (Retailer retailerCollectionOrphanCheckRetailer : retailerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Retailer " + retailerCollectionOrphanCheckRetailer + " in its retailerCollection field has a non-nullable employeeId field.");
            }
            Collection<Cashier> cashierCollectionOrphanCheck = employee.getCashierCollection();
            for (Cashier cashierCollectionOrphanCheckCashier : cashierCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Cashier " + cashierCollectionOrphanCheckCashier + " in its cashierCollection field has a non-nullable employeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void reloveAll(){
        getEntityManager().createNamedQuery("Employee.removeAll").executeUpdate();
    }
    
}
