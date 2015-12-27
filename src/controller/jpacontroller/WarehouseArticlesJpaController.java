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
import modele.entity.Article;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import modele.entity.WarehouseArticles;

/**
 *
 * @author yo
 */
public class WarehouseArticlesJpaController implements Serializable {

    private static WarehouseArticlesJpaController singleton = null;
    
    
    public static WarehouseArticlesJpaController getController(){
        if(singleton==null) singleton = new WarehouseArticlesJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private WarehouseArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WarehouseArticles warehouseArticles) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Article articleIdOrphanCheck = warehouseArticles.getArticleId();
        if (articleIdOrphanCheck != null) {
            WarehouseArticles oldWarehouseArticlesOfArticleId = articleIdOrphanCheck.getWarehouseArticles();
            if (oldWarehouseArticlesOfArticleId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Article " + articleIdOrphanCheck + " already has an item of type WarehouseArticles whose articleId column cannot be null. Please make another selection for the articleId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article articleId = warehouseArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                warehouseArticles.setArticleId(articleId);
            }
            em.persist(warehouseArticles);
            if (articleId != null) {
                articleId.setWarehouseArticles(warehouseArticles);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WarehouseArticles warehouseArticles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WarehouseArticles persistentWarehouseArticles = em.find(WarehouseArticles.class, warehouseArticles.getId());
            Article articleIdOld = persistentWarehouseArticles.getArticleId();
            Article articleIdNew = warehouseArticles.getArticleId();
            List<String> illegalOrphanMessages = null;
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                WarehouseArticles oldWarehouseArticlesOfArticleId = articleIdNew.getWarehouseArticles();
                if (oldWarehouseArticlesOfArticleId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Article " + articleIdNew + " already has an item of type WarehouseArticles whose articleId column cannot be null. Please make another selection for the articleId field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                warehouseArticles.setArticleId(articleIdNew);
            }
            warehouseArticles = em.merge(warehouseArticles);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.setWarehouseArticles(null);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.setWarehouseArticles(warehouseArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = warehouseArticles.getId();
                if (findWarehouseArticles(id) == null) {
                    throw new NonexistentEntityException("The warehouseArticles with id " + id + " no longer exists.");
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
            WarehouseArticles warehouseArticles;
            try {
                warehouseArticles = em.getReference(WarehouseArticles.class, id);
                warehouseArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The warehouseArticles with id " + id + " no longer exists.", enfe);
            }
            Article articleId = warehouseArticles.getArticleId();
            if (articleId != null) {
                articleId.setWarehouseArticles(null);
                articleId = em.merge(articleId);
            }
            em.remove(warehouseArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WarehouseArticles> findWarehouseArticlesEntities() {
        return findWarehouseArticlesEntities(true, -1, -1);
    }

    public List<WarehouseArticles> findWarehouseArticlesEntities(int maxResults, int firstResult) {
        return findWarehouseArticlesEntities(false, maxResults, firstResult);
    }

    private List<WarehouseArticles> findWarehouseArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WarehouseArticles.class));
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

    public WarehouseArticles findWarehouseArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WarehouseArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getWarehouseArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WarehouseArticles> rt = cq.from(WarehouseArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public WarehouseArticles findByArticleId(int articleId){
        List<WarehouseArticles> list = getEntityManager()
            .createNamedQuery("WarehouseArticles.findByArticleId")
            .setParameter("articleId", articleId)
            .getResultList();
        if(list.isEmpty()) return null;
        return list.get(0);
    }
    
    public void reloveAll(){
        EntityManager em = getEntityManager();
        EntityTransaction transac = em.getTransaction();
        transac.begin();
        em.createNamedQuery("WarehouseArticles.removeAll").executeUpdate();
        transac.commit();
        em.close();
    }
    
}
