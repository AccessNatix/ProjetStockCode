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
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modele.entity.Article;
import modele.entity.Transaction;
import modele.entity.TransactionArticles;

/**
 *
 * @author yo
 */
public class TransactionArticlesJpaController implements Serializable {

    private static TransactionArticlesJpaController singleton = null;
    
    
    public static TransactionArticlesJpaController getController(){
        if(singleton==null) singleton = new TransactionArticlesJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private TransactionArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TransactionArticles transactionArticles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article articleId = transactionArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                transactionArticles.setArticleId(articleId);
            }
            Transaction transactionId = transactionArticles.getTransactionId();
            if (transactionId != null) {
                transactionId = em.getReference(transactionId.getClass(), transactionId.getId());
                transactionArticles.setTransactionId(transactionId);
            }
            em.persist(transactionArticles);
            if (articleId != null) {
                articleId.getTransactionArticlesCollection().add(transactionArticles);
                articleId = em.merge(articleId);
            }
            if (transactionId != null) {
                transactionId.getTransactionArticlesCollection().add(transactionArticles);
                transactionId = em.merge(transactionId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TransactionArticles transactionArticles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransactionArticles persistentTransactionArticles = em.find(TransactionArticles.class, transactionArticles.getId());
            Article articleIdOld = persistentTransactionArticles.getArticleId();
            Article articleIdNew = transactionArticles.getArticleId();
            Transaction transactionIdOld = persistentTransactionArticles.getTransactionId();
            Transaction transactionIdNew = transactionArticles.getTransactionId();
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                transactionArticles.setArticleId(articleIdNew);
            }
            if (transactionIdNew != null) {
                transactionIdNew = em.getReference(transactionIdNew.getClass(), transactionIdNew.getId());
                transactionArticles.setTransactionId(transactionIdNew);
            }
            transactionArticles = em.merge(transactionArticles);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getTransactionArticlesCollection().remove(transactionArticles);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getTransactionArticlesCollection().add(transactionArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            if (transactionIdOld != null && !transactionIdOld.equals(transactionIdNew)) {
                transactionIdOld.getTransactionArticlesCollection().remove(transactionArticles);
                transactionIdOld = em.merge(transactionIdOld);
            }
            if (transactionIdNew != null && !transactionIdNew.equals(transactionIdOld)) {
                transactionIdNew.getTransactionArticlesCollection().add(transactionArticles);
                transactionIdNew = em.merge(transactionIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transactionArticles.getId();
                if (findTransactionArticles(id) == null) {
                    throw new NonexistentEntityException("The transactionArticles with id " + id + " no longer exists.");
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
            TransactionArticles transactionArticles;
            try {
                transactionArticles = em.getReference(TransactionArticles.class, id);
                transactionArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transactionArticles with id " + id + " no longer exists.", enfe);
            }
            Article articleId = transactionArticles.getArticleId();
            if (articleId != null) {
                articleId.getTransactionArticlesCollection().remove(transactionArticles);
                articleId = em.merge(articleId);
            }
            Transaction transactionId = transactionArticles.getTransactionId();
            if (transactionId != null) {
                transactionId.getTransactionArticlesCollection().remove(transactionArticles);
                transactionId = em.merge(transactionId);
            }
            em.remove(transactionArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TransactionArticles> findTransactionArticlesEntities() {
        return findTransactionArticlesEntities(true, -1, -1);
    }

    public List<TransactionArticles> findTransactionArticlesEntities(int maxResults, int firstResult) {
        return findTransactionArticlesEntities(false, maxResults, firstResult);
    }

    private List<TransactionArticles> findTransactionArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TransactionArticles.class));
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

    public TransactionArticles findTransactionArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TransactionArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransactionArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransactionArticles> rt = cq.from(TransactionArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<TransactionArticles> findByTransactionId(int transactionId){
        return getEntityManager()
            .createNamedQuery("TransactionArticles.findByTransactionId")
            .setParameter("transactionId", transactionId)
            .getResultList();
    }
    
    public TransactionArticles findByIds(int articleId, int transactionId){
        List<TransactionArticles> l = getEntityManager().createNamedQuery("TransactionArticles.findByIds")
            .setParameter("articleId", articleId)
            .setParameter("transactionId", transactionId)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
    public void reloveAll(){
        EntityManager em = getEntityManager();
        EntityTransaction transac = em.getTransaction();
        transac.begin();
        em.createNamedQuery("TransactionArticles.removeAll").executeUpdate();
        transac.commit();
        em.close();
    }
    
}
