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
import modele.entity.Provider;
import modele.entity.ClientArticles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.CommandedArticles;
import modele.entity.WarehouseArticles;
import modele.entity.AisleArticles;
import modele.entity.Article;
import modele.entity.TransactionArticles;

/**
 *
 * @author yo
 */
public class ArticleJpaController implements Serializable {
    
    private static ArticleJpaController singleton = null;
    
    
    public static ArticleJpaController getController(){
        if(singleton==null) singleton = new ArticleJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private ArticleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Article article) {
        if (article.getClientArticlesCollection() == null) {
            article.setClientArticlesCollection(new ArrayList<ClientArticles>());
        }
        if (article.getCommandedArticlesCollection() == null) {
            article.setCommandedArticlesCollection(new ArrayList<CommandedArticles>());
        }
        if (article.getWarehouseArticlesCollection() == null) {
            article.setWarehouseArticlesCollection(new ArrayList<WarehouseArticles>());
        }
        if (article.getAisleArticlesCollection() == null) {
            article.setAisleArticlesCollection(new ArrayList<AisleArticles>());
        }
        if (article.getTransactionArticlesCollection() == null) {
            article.setTransactionArticlesCollection(new ArrayList<TransactionArticles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider providerId = article.getProviderId();
            if (providerId != null) {
                providerId = em.getReference(providerId.getClass(), providerId.getId());
                article.setProviderId(providerId);
            }
            Collection<ClientArticles> attachedClientArticlesCollection = new ArrayList<ClientArticles>();
            for (ClientArticles clientArticlesCollectionClientArticlesToAttach : article.getClientArticlesCollection()) {
                clientArticlesCollectionClientArticlesToAttach = em.getReference(clientArticlesCollectionClientArticlesToAttach.getClass(), clientArticlesCollectionClientArticlesToAttach.getId());
                attachedClientArticlesCollection.add(clientArticlesCollectionClientArticlesToAttach);
            }
            article.setClientArticlesCollection(attachedClientArticlesCollection);
            Collection<CommandedArticles> attachedCommandedArticlesCollection = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesCollectionCommandedArticlesToAttach : article.getCommandedArticlesCollection()) {
                commandedArticlesCollectionCommandedArticlesToAttach = em.getReference(commandedArticlesCollectionCommandedArticlesToAttach.getClass(), commandedArticlesCollectionCommandedArticlesToAttach.getId());
                attachedCommandedArticlesCollection.add(commandedArticlesCollectionCommandedArticlesToAttach);
            }
            article.setCommandedArticlesCollection(attachedCommandedArticlesCollection);
            Collection<WarehouseArticles> attachedWarehouseArticlesCollection = new ArrayList<WarehouseArticles>();
            for (WarehouseArticles warehouseArticlesCollectionWarehouseArticlesToAttach : article.getWarehouseArticlesCollection()) {
                warehouseArticlesCollectionWarehouseArticlesToAttach = em.getReference(warehouseArticlesCollectionWarehouseArticlesToAttach.getClass(), warehouseArticlesCollectionWarehouseArticlesToAttach.getId());
                attachedWarehouseArticlesCollection.add(warehouseArticlesCollectionWarehouseArticlesToAttach);
            }
            article.setWarehouseArticlesCollection(attachedWarehouseArticlesCollection);
            Collection<AisleArticles> attachedAisleArticlesCollection = new ArrayList<AisleArticles>();
            for (AisleArticles aisleArticlesCollectionAisleArticlesToAttach : article.getAisleArticlesCollection()) {
                aisleArticlesCollectionAisleArticlesToAttach = em.getReference(aisleArticlesCollectionAisleArticlesToAttach.getClass(), aisleArticlesCollectionAisleArticlesToAttach.getId());
                attachedAisleArticlesCollection.add(aisleArticlesCollectionAisleArticlesToAttach);
            }
            article.setAisleArticlesCollection(attachedAisleArticlesCollection);
            Collection<TransactionArticles> attachedTransactionArticlesCollection = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionTransactionArticlesToAttach : article.getTransactionArticlesCollection()) {
                transactionArticlesCollectionTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionTransactionArticlesToAttach.getClass(), transactionArticlesCollectionTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollection.add(transactionArticlesCollectionTransactionArticlesToAttach);
            }
            article.setTransactionArticlesCollection(attachedTransactionArticlesCollection);
            em.persist(article);
            if (providerId != null) {
                providerId.getArticleCollection().add(article);
                providerId = em.merge(providerId);
            }
            for (ClientArticles clientArticlesCollectionClientArticles : article.getClientArticlesCollection()) {
                Article oldArticleIdOfClientArticlesCollectionClientArticles = clientArticlesCollectionClientArticles.getArticleId();
                clientArticlesCollectionClientArticles.setArticleId(article);
                clientArticlesCollectionClientArticles = em.merge(clientArticlesCollectionClientArticles);
                if (oldArticleIdOfClientArticlesCollectionClientArticles != null) {
                    oldArticleIdOfClientArticlesCollectionClientArticles.getClientArticlesCollection().remove(clientArticlesCollectionClientArticles);
                    oldArticleIdOfClientArticlesCollectionClientArticles = em.merge(oldArticleIdOfClientArticlesCollectionClientArticles);
                }
            }
            for (CommandedArticles commandedArticlesCollectionCommandedArticles : article.getCommandedArticlesCollection()) {
                Article oldArticleIdOfCommandedArticlesCollectionCommandedArticles = commandedArticlesCollectionCommandedArticles.getArticleId();
                commandedArticlesCollectionCommandedArticles.setArticleId(article);
                commandedArticlesCollectionCommandedArticles = em.merge(commandedArticlesCollectionCommandedArticles);
                if (oldArticleIdOfCommandedArticlesCollectionCommandedArticles != null) {
                    oldArticleIdOfCommandedArticlesCollectionCommandedArticles.getCommandedArticlesCollection().remove(commandedArticlesCollectionCommandedArticles);
                    oldArticleIdOfCommandedArticlesCollectionCommandedArticles = em.merge(oldArticleIdOfCommandedArticlesCollectionCommandedArticles);
                }
            }
            for (WarehouseArticles warehouseArticlesCollectionWarehouseArticles : article.getWarehouseArticlesCollection()) {
                Article oldArticleIdOfWarehouseArticlesCollectionWarehouseArticles = warehouseArticlesCollectionWarehouseArticles.getArticleId();
                warehouseArticlesCollectionWarehouseArticles.setArticleId(article);
                warehouseArticlesCollectionWarehouseArticles = em.merge(warehouseArticlesCollectionWarehouseArticles);
                if (oldArticleIdOfWarehouseArticlesCollectionWarehouseArticles != null) {
                    oldArticleIdOfWarehouseArticlesCollectionWarehouseArticles.getWarehouseArticlesCollection().remove(warehouseArticlesCollectionWarehouseArticles);
                    oldArticleIdOfWarehouseArticlesCollectionWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticlesCollectionWarehouseArticles);
                }
            }
            for (AisleArticles aisleArticlesCollectionAisleArticles : article.getAisleArticlesCollection()) {
                Article oldArticleIdOfAisleArticlesCollectionAisleArticles = aisleArticlesCollectionAisleArticles.getArticleId();
                aisleArticlesCollectionAisleArticles.setArticleId(article);
                aisleArticlesCollectionAisleArticles = em.merge(aisleArticlesCollectionAisleArticles);
                if (oldArticleIdOfAisleArticlesCollectionAisleArticles != null) {
                    oldArticleIdOfAisleArticlesCollectionAisleArticles.getAisleArticlesCollection().remove(aisleArticlesCollectionAisleArticles);
                    oldArticleIdOfAisleArticlesCollectionAisleArticles = em.merge(oldArticleIdOfAisleArticlesCollectionAisleArticles);
                }
            }
            for (TransactionArticles transactionArticlesCollectionTransactionArticles : article.getTransactionArticlesCollection()) {
                Article oldArticleIdOfTransactionArticlesCollectionTransactionArticles = transactionArticlesCollectionTransactionArticles.getArticleId();
                transactionArticlesCollectionTransactionArticles.setArticleId(article);
                transactionArticlesCollectionTransactionArticles = em.merge(transactionArticlesCollectionTransactionArticles);
                if (oldArticleIdOfTransactionArticlesCollectionTransactionArticles != null) {
                    oldArticleIdOfTransactionArticlesCollectionTransactionArticles.getTransactionArticlesCollection().remove(transactionArticlesCollectionTransactionArticles);
                    oldArticleIdOfTransactionArticlesCollectionTransactionArticles = em.merge(oldArticleIdOfTransactionArticlesCollectionTransactionArticles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Article article) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Article persistentArticle = em.find(Article.class, article.getId());
            Provider providerIdOld = persistentArticle.getProviderId();
            Provider providerIdNew = article.getProviderId();
            Collection<ClientArticles> clientArticlesCollectionOld = persistentArticle.getClientArticlesCollection();
            Collection<ClientArticles> clientArticlesCollectionNew = article.getClientArticlesCollection();
            Collection<CommandedArticles> commandedArticlesCollectionOld = persistentArticle.getCommandedArticlesCollection();
            Collection<CommandedArticles> commandedArticlesCollectionNew = article.getCommandedArticlesCollection();
            Collection<WarehouseArticles> warehouseArticlesCollectionOld = persistentArticle.getWarehouseArticlesCollection();
            Collection<WarehouseArticles> warehouseArticlesCollectionNew = article.getWarehouseArticlesCollection();
            Collection<AisleArticles> aisleArticlesCollectionOld = persistentArticle.getAisleArticlesCollection();
            Collection<AisleArticles> aisleArticlesCollectionNew = article.getAisleArticlesCollection();
            Collection<TransactionArticles> transactionArticlesCollectionOld = persistentArticle.getTransactionArticlesCollection();
            Collection<TransactionArticles> transactionArticlesCollectionNew = article.getTransactionArticlesCollection();
            List<String> illegalOrphanMessages = null;
            for (ClientArticles clientArticlesCollectionOldClientArticles : clientArticlesCollectionOld) {
                if (!clientArticlesCollectionNew.contains(clientArticlesCollectionOldClientArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientArticles " + clientArticlesCollectionOldClientArticles + " since its articleId field is not nullable.");
                }
            }
            for (CommandedArticles commandedArticlesCollectionOldCommandedArticles : commandedArticlesCollectionOld) {
                if (!commandedArticlesCollectionNew.contains(commandedArticlesCollectionOldCommandedArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommandedArticles " + commandedArticlesCollectionOldCommandedArticles + " since its articleId field is not nullable.");
                }
            }
            for (WarehouseArticles warehouseArticlesCollectionOldWarehouseArticles : warehouseArticlesCollectionOld) {
                if (!warehouseArticlesCollectionNew.contains(warehouseArticlesCollectionOldWarehouseArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WarehouseArticles " + warehouseArticlesCollectionOldWarehouseArticles + " since its articleId field is not nullable.");
                }
            }
            for (AisleArticles aisleArticlesCollectionOldAisleArticles : aisleArticlesCollectionOld) {
                if (!aisleArticlesCollectionNew.contains(aisleArticlesCollectionOldAisleArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AisleArticles " + aisleArticlesCollectionOldAisleArticles + " since its articleId field is not nullable.");
                }
            }
            for (TransactionArticles transactionArticlesCollectionOldTransactionArticles : transactionArticlesCollectionOld) {
                if (!transactionArticlesCollectionNew.contains(transactionArticlesCollectionOldTransactionArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionArticles " + transactionArticlesCollectionOldTransactionArticles + " since its articleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (providerIdNew != null) {
                providerIdNew = em.getReference(providerIdNew.getClass(), providerIdNew.getId());
                article.setProviderId(providerIdNew);
            }
            Collection<ClientArticles> attachedClientArticlesCollectionNew = new ArrayList<ClientArticles>();
            for (ClientArticles clientArticlesCollectionNewClientArticlesToAttach : clientArticlesCollectionNew) {
                clientArticlesCollectionNewClientArticlesToAttach = em.getReference(clientArticlesCollectionNewClientArticlesToAttach.getClass(), clientArticlesCollectionNewClientArticlesToAttach.getId());
                attachedClientArticlesCollectionNew.add(clientArticlesCollectionNewClientArticlesToAttach);
            }
            clientArticlesCollectionNew = attachedClientArticlesCollectionNew;
            article.setClientArticlesCollection(clientArticlesCollectionNew);
            Collection<CommandedArticles> attachedCommandedArticlesCollectionNew = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesCollectionNewCommandedArticlesToAttach : commandedArticlesCollectionNew) {
                commandedArticlesCollectionNewCommandedArticlesToAttach = em.getReference(commandedArticlesCollectionNewCommandedArticlesToAttach.getClass(), commandedArticlesCollectionNewCommandedArticlesToAttach.getId());
                attachedCommandedArticlesCollectionNew.add(commandedArticlesCollectionNewCommandedArticlesToAttach);
            }
            commandedArticlesCollectionNew = attachedCommandedArticlesCollectionNew;
            article.setCommandedArticlesCollection(commandedArticlesCollectionNew);
            Collection<WarehouseArticles> attachedWarehouseArticlesCollectionNew = new ArrayList<WarehouseArticles>();
            for (WarehouseArticles warehouseArticlesCollectionNewWarehouseArticlesToAttach : warehouseArticlesCollectionNew) {
                warehouseArticlesCollectionNewWarehouseArticlesToAttach = em.getReference(warehouseArticlesCollectionNewWarehouseArticlesToAttach.getClass(), warehouseArticlesCollectionNewWarehouseArticlesToAttach.getId());
                attachedWarehouseArticlesCollectionNew.add(warehouseArticlesCollectionNewWarehouseArticlesToAttach);
            }
            warehouseArticlesCollectionNew = attachedWarehouseArticlesCollectionNew;
            article.setWarehouseArticlesCollection(warehouseArticlesCollectionNew);
            Collection<AisleArticles> attachedAisleArticlesCollectionNew = new ArrayList<AisleArticles>();
            for (AisleArticles aisleArticlesCollectionNewAisleArticlesToAttach : aisleArticlesCollectionNew) {
                aisleArticlesCollectionNewAisleArticlesToAttach = em.getReference(aisleArticlesCollectionNewAisleArticlesToAttach.getClass(), aisleArticlesCollectionNewAisleArticlesToAttach.getId());
                attachedAisleArticlesCollectionNew.add(aisleArticlesCollectionNewAisleArticlesToAttach);
            }
            aisleArticlesCollectionNew = attachedAisleArticlesCollectionNew;
            article.setAisleArticlesCollection(aisleArticlesCollectionNew);
            Collection<TransactionArticles> attachedTransactionArticlesCollectionNew = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionNewTransactionArticlesToAttach : transactionArticlesCollectionNew) {
                transactionArticlesCollectionNewTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionNewTransactionArticlesToAttach.getClass(), transactionArticlesCollectionNewTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollectionNew.add(transactionArticlesCollectionNewTransactionArticlesToAttach);
            }
            transactionArticlesCollectionNew = attachedTransactionArticlesCollectionNew;
            article.setTransactionArticlesCollection(transactionArticlesCollectionNew);
            article = em.merge(article);
            if (providerIdOld != null && !providerIdOld.equals(providerIdNew)) {
                providerIdOld.getArticleCollection().remove(article);
                providerIdOld = em.merge(providerIdOld);
            }
            if (providerIdNew != null && !providerIdNew.equals(providerIdOld)) {
                providerIdNew.getArticleCollection().add(article);
                providerIdNew = em.merge(providerIdNew);
            }
            for (ClientArticles clientArticlesCollectionNewClientArticles : clientArticlesCollectionNew) {
                if (!clientArticlesCollectionOld.contains(clientArticlesCollectionNewClientArticles)) {
                    Article oldArticleIdOfClientArticlesCollectionNewClientArticles = clientArticlesCollectionNewClientArticles.getArticleId();
                    clientArticlesCollectionNewClientArticles.setArticleId(article);
                    clientArticlesCollectionNewClientArticles = em.merge(clientArticlesCollectionNewClientArticles);
                    if (oldArticleIdOfClientArticlesCollectionNewClientArticles != null && !oldArticleIdOfClientArticlesCollectionNewClientArticles.equals(article)) {
                        oldArticleIdOfClientArticlesCollectionNewClientArticles.getClientArticlesCollection().remove(clientArticlesCollectionNewClientArticles);
                        oldArticleIdOfClientArticlesCollectionNewClientArticles = em.merge(oldArticleIdOfClientArticlesCollectionNewClientArticles);
                    }
                }
            }
            for (CommandedArticles commandedArticlesCollectionNewCommandedArticles : commandedArticlesCollectionNew) {
                if (!commandedArticlesCollectionOld.contains(commandedArticlesCollectionNewCommandedArticles)) {
                    Article oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles = commandedArticlesCollectionNewCommandedArticles.getArticleId();
                    commandedArticlesCollectionNewCommandedArticles.setArticleId(article);
                    commandedArticlesCollectionNewCommandedArticles = em.merge(commandedArticlesCollectionNewCommandedArticles);
                    if (oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles != null && !oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles.equals(article)) {
                        oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles.getCommandedArticlesCollection().remove(commandedArticlesCollectionNewCommandedArticles);
                        oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles = em.merge(oldArticleIdOfCommandedArticlesCollectionNewCommandedArticles);
                    }
                }
            }
            for (WarehouseArticles warehouseArticlesCollectionNewWarehouseArticles : warehouseArticlesCollectionNew) {
                if (!warehouseArticlesCollectionOld.contains(warehouseArticlesCollectionNewWarehouseArticles)) {
                    Article oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles = warehouseArticlesCollectionNewWarehouseArticles.getArticleId();
                    warehouseArticlesCollectionNewWarehouseArticles.setArticleId(article);
                    warehouseArticlesCollectionNewWarehouseArticles = em.merge(warehouseArticlesCollectionNewWarehouseArticles);
                    if (oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles != null && !oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles.equals(article)) {
                        oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles.getWarehouseArticlesCollection().remove(warehouseArticlesCollectionNewWarehouseArticles);
                        oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticlesCollectionNewWarehouseArticles);
                    }
                }
            }
            for (AisleArticles aisleArticlesCollectionNewAisleArticles : aisleArticlesCollectionNew) {
                if (!aisleArticlesCollectionOld.contains(aisleArticlesCollectionNewAisleArticles)) {
                    Article oldArticleIdOfAisleArticlesCollectionNewAisleArticles = aisleArticlesCollectionNewAisleArticles.getArticleId();
                    aisleArticlesCollectionNewAisleArticles.setArticleId(article);
                    aisleArticlesCollectionNewAisleArticles = em.merge(aisleArticlesCollectionNewAisleArticles);
                    if (oldArticleIdOfAisleArticlesCollectionNewAisleArticles != null && !oldArticleIdOfAisleArticlesCollectionNewAisleArticles.equals(article)) {
                        oldArticleIdOfAisleArticlesCollectionNewAisleArticles.getAisleArticlesCollection().remove(aisleArticlesCollectionNewAisleArticles);
                        oldArticleIdOfAisleArticlesCollectionNewAisleArticles = em.merge(oldArticleIdOfAisleArticlesCollectionNewAisleArticles);
                    }
                }
            }
            for (TransactionArticles transactionArticlesCollectionNewTransactionArticles : transactionArticlesCollectionNew) {
                if (!transactionArticlesCollectionOld.contains(transactionArticlesCollectionNewTransactionArticles)) {
                    Article oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles = transactionArticlesCollectionNewTransactionArticles.getArticleId();
                    transactionArticlesCollectionNewTransactionArticles.setArticleId(article);
                    transactionArticlesCollectionNewTransactionArticles = em.merge(transactionArticlesCollectionNewTransactionArticles);
                    if (oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles != null && !oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles.equals(article)) {
                        oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles.getTransactionArticlesCollection().remove(transactionArticlesCollectionNewTransactionArticles);
                        oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles = em.merge(oldArticleIdOfTransactionArticlesCollectionNewTransactionArticles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = article.getId();
                if (findArticle(id) == null) {
                    throw new NonexistentEntityException("The article with id " + id + " no longer exists.");
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
            Article article;
            try {
                article = em.getReference(Article.class, id);
                article.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The article with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ClientArticles> clientArticlesCollectionOrphanCheck = article.getClientArticlesCollection();
            for (ClientArticles clientArticlesCollectionOrphanCheckClientArticles : clientArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the ClientArticles " + clientArticlesCollectionOrphanCheckClientArticles + " in its clientArticlesCollection field has a non-nullable articleId field.");
            }
            Collection<CommandedArticles> commandedArticlesCollectionOrphanCheck = article.getCommandedArticlesCollection();
            for (CommandedArticles commandedArticlesCollectionOrphanCheckCommandedArticles : commandedArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the CommandedArticles " + commandedArticlesCollectionOrphanCheckCommandedArticles + " in its commandedArticlesCollection field has a non-nullable articleId field.");
            }
            Collection<WarehouseArticles> warehouseArticlesCollectionOrphanCheck = article.getWarehouseArticlesCollection();
            for (WarehouseArticles warehouseArticlesCollectionOrphanCheckWarehouseArticles : warehouseArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the WarehouseArticles " + warehouseArticlesCollectionOrphanCheckWarehouseArticles + " in its warehouseArticlesCollection field has a non-nullable articleId field.");
            }
            Collection<AisleArticles> aisleArticlesCollectionOrphanCheck = article.getAisleArticlesCollection();
            for (AisleArticles aisleArticlesCollectionOrphanCheckAisleArticles : aisleArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the AisleArticles " + aisleArticlesCollectionOrphanCheckAisleArticles + " in its aisleArticlesCollection field has a non-nullable articleId field.");
            }
            Collection<TransactionArticles> transactionArticlesCollectionOrphanCheck = article.getTransactionArticlesCollection();
            for (TransactionArticles transactionArticlesCollectionOrphanCheckTransactionArticles : transactionArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the TransactionArticles " + transactionArticlesCollectionOrphanCheckTransactionArticles + " in its transactionArticlesCollection field has a non-nullable articleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provider providerId = article.getProviderId();
            if (providerId != null) {
                providerId.getArticleCollection().remove(article);
                providerId = em.merge(providerId);
            }
            em.remove(article);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Article> findArticleEntities() {
        return findArticleEntities(true, -1, -1);
    }

    public List<Article> findArticleEntities(int maxResults, int firstResult) {
        return findArticleEntities(false, maxResults, firstResult);
    }

    private List<Article> findArticleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Article.class));
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

    public Article findArticle(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Article.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Article> rt = cq.from(Article.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
