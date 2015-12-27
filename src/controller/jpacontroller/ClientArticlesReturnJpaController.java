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
import modele.entity.ClientArticlesReturn;

/**
 *
 * @author yo
 */
public class ClientArticlesReturnJpaController implements Serializable {

    private static ClientArticlesReturnJpaController singleton = null;
    
    
    public static ClientArticlesReturnJpaController getController(){
        if(singleton==null) singleton = new ClientArticlesReturnJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private ClientArticlesReturnJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClientArticlesReturn clientArticlesReturn) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client clientId = clientArticlesReturn.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getId());
                clientArticlesReturn.setClientId(clientId);
            }
            Article articleId = clientArticlesReturn.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                clientArticlesReturn.setArticleId(articleId);
            }
            em.persist(clientArticlesReturn);
            if (clientId != null) {
                clientId.getClientArticlesReturnCollection().add(clientArticlesReturn);
                clientId = em.merge(clientId);
            }
            if (articleId != null) {
                articleId.getClientArticlesReturnCollection().add(clientArticlesReturn);
                articleId = em.merge(articleId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClientArticlesReturn clientArticlesReturn) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClientArticlesReturn persistentClientArticlesReturn = em.find(ClientArticlesReturn.class, clientArticlesReturn.getId());
            Client clientIdOld = persistentClientArticlesReturn.getClientId();
            Client clientIdNew = clientArticlesReturn.getClientId();
            Article articleIdOld = persistentClientArticlesReturn.getArticleId();
            Article articleIdNew = clientArticlesReturn.getArticleId();
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getId());
                clientArticlesReturn.setClientId(clientIdNew);
            }
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                clientArticlesReturn.setArticleId(articleIdNew);
            }
            clientArticlesReturn = em.merge(clientArticlesReturn);
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getClientArticlesReturnCollection().remove(clientArticlesReturn);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getClientArticlesReturnCollection().add(clientArticlesReturn);
                clientIdNew = em.merge(clientIdNew);
            }
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getClientArticlesReturnCollection().remove(clientArticlesReturn);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getClientArticlesReturnCollection().add(clientArticlesReturn);
                articleIdNew = em.merge(articleIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clientArticlesReturn.getId();
                if (findClientArticlesReturn(id) == null) {
                    throw new NonexistentEntityException("The clientArticlesReturn with id " + id + " no longer exists.");
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
            ClientArticlesReturn clientArticlesReturn;
            try {
                clientArticlesReturn = em.getReference(ClientArticlesReturn.class, id);
                clientArticlesReturn.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientArticlesReturn with id " + id + " no longer exists.", enfe);
            }
            Client clientId = clientArticlesReturn.getClientId();
            if (clientId != null) {
                clientId.getClientArticlesReturnCollection().remove(clientArticlesReturn);
                clientId = em.merge(clientId);
            }
            Article articleId = clientArticlesReturn.getArticleId();
            if (articleId != null) {
                articleId.getClientArticlesReturnCollection().remove(clientArticlesReturn);
                articleId = em.merge(articleId);
            }
            em.remove(clientArticlesReturn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClientArticlesReturn> findClientArticlesReturnEntities() {
        return findClientArticlesReturnEntities(true, -1, -1);
    }

    public List<ClientArticlesReturn> findClientArticlesReturnEntities(int maxResults, int firstResult) {
        return findClientArticlesReturnEntities(false, maxResults, firstResult);
    }

    private List<ClientArticlesReturn> findClientArticlesReturnEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClientArticlesReturn.class));
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

    public ClientArticlesReturn findClientArticlesReturn(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClientArticlesReturn.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientArticlesReturnCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClientArticlesReturn> rt = cq.from(ClientArticlesReturn.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<ClientArticlesReturn> findByClientId(int clientId){
        return getEntityManager()
            .createNamedQuery("ClientArticlesReturn.findByClientId")
            .setParameter("clientId", clientId)
            .getResultList();
    }
    
    public ClientArticlesReturn findByIds(int clientId, int articleId){
        List<ClientArticlesReturn> l = getEntityManager()
            .createNamedQuery("ClientArticlesReturn.findByIds")
            .setParameter("clientId", clientId)
            .setParameter("articleId", articleId)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
}
