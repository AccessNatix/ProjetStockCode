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
import modele.entity.Client;
import modele.entity.Article;
import modele.entity.ClientArticles;

/**
 *
 * @author yo
 */
public class ClientArticlesJpaController implements Serializable {

    private static ClientArticlesJpaController singleton = null;
    
    
    public static ClientArticlesJpaController getController(){
        if(singleton==null) singleton = new ClientArticlesJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private ClientArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientArticles clientArticles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client clientId = clientArticles.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getId());
                clientArticles.setClientId(clientId);
            }
            Article articleId = clientArticles.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                clientArticles.setArticleId(articleId);
            }
            em.persist(clientArticles);
            if (clientId != null) {
                clientId.getClientArticlesCollection().add(clientArticles);
                clientId = em.merge(clientId);
            }
            if (articleId != null) {
                articleId.getClientArticlesCollection().add(clientArticles);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientArticles clientArticles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientArticles persistentClientArticles = em.find(ClientArticles.class, clientArticles.getId());
            Client clientIdOld = persistentClientArticles.getClientId();
            Client clientIdNew = clientArticles.getClientId();
            Article articleIdOld = persistentClientArticles.getArticleId();
            Article articleIdNew = clientArticles.getArticleId();
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getId());
                clientArticles.setClientId(clientIdNew);
            }
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                clientArticles.setArticleId(articleIdNew);
            }
            clientArticles = em.merge(clientArticles);
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getClientArticlesCollection().remove(clientArticles);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getClientArticlesCollection().add(clientArticles);
                clientIdNew = em.merge(clientIdNew);
            }
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getClientArticlesCollection().remove(clientArticles);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getClientArticlesCollection().add(clientArticles);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientArticles.getId();
                if (findClientArticles(id) == null) {
                    throw new NonexistentEntityException("The clientArticles with id " + id + " no longer exists.");
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
            ClientArticles clientArticles;
            try {
                clientArticles = em.getReference(ClientArticles.class, id);
                clientArticles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientArticles with id " + id + " no longer exists.", enfe);
            }
            Client clientId = clientArticles.getClientId();
            if (clientId != null) {
                clientId.getClientArticlesCollection().remove(clientArticles);
                clientId = em.merge(clientId);
            }
            Article articleId = clientArticles.getArticleId();
            if (articleId != null) {
                articleId.getClientArticlesCollection().remove(clientArticles);
                articleId = em.merge(articleId);
            }
            em.remove(clientArticles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClientArticles> findClientArticlesEntities() {
        return findClientArticlesEntities(true, -1, -1);
    }

    public List<ClientArticles> findClientArticlesEntities(int maxResults, int firstResult) {
        return findClientArticlesEntities(false, maxResults, firstResult);
    }

    private List<ClientArticles> findClientArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientArticles.class));
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

    public ClientArticles findClientArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientArticles.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientArticles> rt = cq.from(ClientArticles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<ClientArticles> findByClientId(int clientId){
        return getEntityManager()
            .createNamedQuery("ClientArticles.findByClientId")
            .setParameter("clientId", clientId)
            .getResultList();
    }
    
    public ClientArticles findByIds(int clientId, int articleId){
        List<ClientArticles> l = getEntityManager()
            .createNamedQuery("ClientArticles.findByIds")
            .setParameter("clientId", clientId)
            .setParameter("articleId", articleId)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
    public void removeByClientId(int clientId){
        getEntityManager().createNamedQuery("ClientArticles.removeByClientId")
            .setParameter("clientId", clientId)
            .executeUpdate();
    }
    
    
    public void reloveAll(){
        getEntityManager().createNamedQuery("ClientArticles.removeAll").executeUpdate();
    }
}
