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
import modele.entity.WarehouseArticles;
import modele.entity.AisleArticles;
import modele.entity.ClientArticles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.Article;
import modele.entity.CommandedArticles;
import modele.entity.TransactionArticles;
import modele.entity.ClientArticlesReturn;

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
        if (article.getTransactionArticlesCollection() == null) {
            article.setTransactionArticlesCollection(new ArrayList<TransactionArticles>());
        }
        if (article.getClientArticlesReturnCollection() == null) {
            article.setClientArticlesReturnCollection(new ArrayList<ClientArticlesReturn>());
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
            WarehouseArticles warehouseArticles = article.getWarehouseArticles();
            if (warehouseArticles != null) {
                warehouseArticles = em.getReference(warehouseArticles.getClass(), warehouseArticles.getId());
                article.setWarehouseArticles(warehouseArticles);
            }
            AisleArticles aisleArticles = article.getAisleArticles();
            if (aisleArticles != null) {
                aisleArticles = em.getReference(aisleArticles.getClass(), aisleArticles.getId());
                article.setAisleArticles(aisleArticles);
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
            Collection<TransactionArticles> attachedTransactionArticlesCollection = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionTransactionArticlesToAttach : article.getTransactionArticlesCollection()) {
                transactionArticlesCollectionTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionTransactionArticlesToAttach.getClass(), transactionArticlesCollectionTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollection.add(transactionArticlesCollectionTransactionArticlesToAttach);
            }
            article.setTransactionArticlesCollection(attachedTransactionArticlesCollection);
            Collection<ClientArticlesReturn> attachedClientArticlesReturnCollection = new ArrayList<ClientArticlesReturn>();
            for (ClientArticlesReturn clientArticlesReturnCollectionClientArticlesReturnToAttach : article.getClientArticlesReturnCollection()) {
                clientArticlesReturnCollectionClientArticlesReturnToAttach = em.getReference(clientArticlesReturnCollectionClientArticlesReturnToAttach.getClass(), clientArticlesReturnCollectionClientArticlesReturnToAttach.getId());
                attachedClientArticlesReturnCollection.add(clientArticlesReturnCollectionClientArticlesReturnToAttach);
            }
            article.setClientArticlesReturnCollection(attachedClientArticlesReturnCollection);
            em.persist(article);
            if (providerId != null) {
                providerId.getArticleCollection().add(article);
                providerId = em.merge(providerId);
            }
            if (warehouseArticles != null) {
                Article oldArticleIdOfWarehouseArticles = warehouseArticles.getArticleId();
                if (oldArticleIdOfWarehouseArticles != null) {
                    oldArticleIdOfWarehouseArticles.setWarehouseArticles(null);
                    oldArticleIdOfWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticles);
                }
                warehouseArticles.setArticleId(article);
                warehouseArticles = em.merge(warehouseArticles);
            }
            if (aisleArticles != null) {
                Article oldArticleIdOfAisleArticles = aisleArticles.getArticleId();
                if (oldArticleIdOfAisleArticles != null) {
                    oldArticleIdOfAisleArticles.setAisleArticles(null);
                    oldArticleIdOfAisleArticles = em.merge(oldArticleIdOfAisleArticles);
                }
                aisleArticles.setArticleId(article);
                aisleArticles = em.merge(aisleArticles);
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
            for (TransactionArticles transactionArticlesCollectionTransactionArticles : article.getTransactionArticlesCollection()) {
                Article oldArticleIdOfTransactionArticlesCollectionTransactionArticles = transactionArticlesCollectionTransactionArticles.getArticleId();
                transactionArticlesCollectionTransactionArticles.setArticleId(article);
                transactionArticlesCollectionTransactionArticles = em.merge(transactionArticlesCollectionTransactionArticles);
                if (oldArticleIdOfTransactionArticlesCollectionTransactionArticles != null) {
                    oldArticleIdOfTransactionArticlesCollectionTransactionArticles.getTransactionArticlesCollection().remove(transactionArticlesCollectionTransactionArticles);
                    oldArticleIdOfTransactionArticlesCollectionTransactionArticles = em.merge(oldArticleIdOfTransactionArticlesCollectionTransactionArticles);
                }
            }
            for (ClientArticlesReturn clientArticlesReturnCollectionClientArticlesReturn : article.getClientArticlesReturnCollection()) {
                Article oldArticleIdOfClientArticlesReturnCollectionClientArticlesReturn = clientArticlesReturnCollectionClientArticlesReturn.getArticleId();
                clientArticlesReturnCollectionClientArticlesReturn.setArticleId(article);
                clientArticlesReturnCollectionClientArticlesReturn = em.merge(clientArticlesReturnCollectionClientArticlesReturn);
                if (oldArticleIdOfClientArticlesReturnCollectionClientArticlesReturn != null) {
                    oldArticleIdOfClientArticlesReturnCollectionClientArticlesReturn.getClientArticlesReturnCollection().remove(clientArticlesReturnCollectionClientArticlesReturn);
                    oldArticleIdOfClientArticlesReturnCollectionClientArticlesReturn = em.merge(oldArticleIdOfClientArticlesReturnCollectionClientArticlesReturn);
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
            WarehouseArticles warehouseArticlesOld = persistentArticle.getWarehouseArticles();
            WarehouseArticles warehouseArticlesNew = article.getWarehouseArticles();
            AisleArticles aisleArticlesOld = persistentArticle.getAisleArticles();
            AisleArticles aisleArticlesNew = article.getAisleArticles();
            Collection<ClientArticles> clientArticlesCollectionOld = persistentArticle.getClientArticlesCollection();
            Collection<ClientArticles> clientArticlesCollectionNew = article.getClientArticlesCollection();
            Collection<CommandedArticles> commandedArticlesCollectionOld = persistentArticle.getCommandedArticlesCollection();
            Collection<CommandedArticles> commandedArticlesCollectionNew = article.getCommandedArticlesCollection();
            Collection<TransactionArticles> transactionArticlesCollectionOld = persistentArticle.getTransactionArticlesCollection();
            Collection<TransactionArticles> transactionArticlesCollectionNew = article.getTransactionArticlesCollection();
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionOld = persistentArticle.getClientArticlesReturnCollection();
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionNew = article.getClientArticlesReturnCollection();
            List<String> illegalOrphanMessages = null;
            if (warehouseArticlesOld != null && !warehouseArticlesOld.equals(warehouseArticlesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain WarehouseArticles " + warehouseArticlesOld + " since its articleId field is not nullable.");
            }
            if (aisleArticlesOld != null && !aisleArticlesOld.equals(aisleArticlesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain AisleArticles " + aisleArticlesOld + " since its articleId field is not nullable.");
            }
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
            for (TransactionArticles transactionArticlesCollectionOldTransactionArticles : transactionArticlesCollectionOld) {
                if (!transactionArticlesCollectionNew.contains(transactionArticlesCollectionOldTransactionArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionArticles " + transactionArticlesCollectionOldTransactionArticles + " since its articleId field is not nullable.");
                }
            }
            for (ClientArticlesReturn clientArticlesReturnCollectionOldClientArticlesReturn : clientArticlesReturnCollectionOld) {
                if (!clientArticlesReturnCollectionNew.contains(clientArticlesReturnCollectionOldClientArticlesReturn)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientArticlesReturn " + clientArticlesReturnCollectionOldClientArticlesReturn + " since its articleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (providerIdNew != null) {
                providerIdNew = em.getReference(providerIdNew.getClass(), providerIdNew.getId());
                article.setProviderId(providerIdNew);
            }
            if (warehouseArticlesNew != null) {
                warehouseArticlesNew = em.getReference(warehouseArticlesNew.getClass(), warehouseArticlesNew.getId());
                article.setWarehouseArticles(warehouseArticlesNew);
            }
            if (aisleArticlesNew != null) {
                aisleArticlesNew = em.getReference(aisleArticlesNew.getClass(), aisleArticlesNew.getId());
                article.setAisleArticles(aisleArticlesNew);
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
            Collection<TransactionArticles> attachedTransactionArticlesCollectionNew = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionNewTransactionArticlesToAttach : transactionArticlesCollectionNew) {
                transactionArticlesCollectionNewTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionNewTransactionArticlesToAttach.getClass(), transactionArticlesCollectionNewTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollectionNew.add(transactionArticlesCollectionNewTransactionArticlesToAttach);
            }
            transactionArticlesCollectionNew = attachedTransactionArticlesCollectionNew;
            article.setTransactionArticlesCollection(transactionArticlesCollectionNew);
            Collection<ClientArticlesReturn> attachedClientArticlesReturnCollectionNew = new ArrayList<ClientArticlesReturn>();
            for (ClientArticlesReturn clientArticlesReturnCollectionNewClientArticlesReturnToAttach : clientArticlesReturnCollectionNew) {
                clientArticlesReturnCollectionNewClientArticlesReturnToAttach = em.getReference(clientArticlesReturnCollectionNewClientArticlesReturnToAttach.getClass(), clientArticlesReturnCollectionNewClientArticlesReturnToAttach.getId());
                attachedClientArticlesReturnCollectionNew.add(clientArticlesReturnCollectionNewClientArticlesReturnToAttach);
            }
            clientArticlesReturnCollectionNew = attachedClientArticlesReturnCollectionNew;
            article.setClientArticlesReturnCollection(clientArticlesReturnCollectionNew);
            article = em.merge(article);
            if (providerIdOld != null && !providerIdOld.equals(providerIdNew)) {
                providerIdOld.getArticleCollection().remove(article);
                providerIdOld = em.merge(providerIdOld);
            }
            if (providerIdNew != null && !providerIdNew.equals(providerIdOld)) {
                providerIdNew.getArticleCollection().add(article);
                providerIdNew = em.merge(providerIdNew);
            }
            if (warehouseArticlesNew != null && !warehouseArticlesNew.equals(warehouseArticlesOld)) {
                Article oldArticleIdOfWarehouseArticles = warehouseArticlesNew.getArticleId();
                if (oldArticleIdOfWarehouseArticles != null) {
                    oldArticleIdOfWarehouseArticles.setWarehouseArticles(null);
                    oldArticleIdOfWarehouseArticles = em.merge(oldArticleIdOfWarehouseArticles);
                }
                warehouseArticlesNew.setArticleId(article);
                warehouseArticlesNew = em.merge(warehouseArticlesNew);
            }
            if (aisleArticlesNew != null && !aisleArticlesNew.equals(aisleArticlesOld)) {
                Article oldArticleIdOfAisleArticles = aisleArticlesNew.getArticleId();
                if (oldArticleIdOfAisleArticles != null) {
                    oldArticleIdOfAisleArticles.setAisleArticles(null);
                    oldArticleIdOfAisleArticles = em.merge(oldArticleIdOfAisleArticles);
                }
                aisleArticlesNew.setArticleId(article);
                aisleArticlesNew = em.merge(aisleArticlesNew);
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
            for (ClientArticlesReturn clientArticlesReturnCollectionNewClientArticlesReturn : clientArticlesReturnCollectionNew) {
                if (!clientArticlesReturnCollectionOld.contains(clientArticlesReturnCollectionNewClientArticlesReturn)) {
                    Article oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn = clientArticlesReturnCollectionNewClientArticlesReturn.getArticleId();
                    clientArticlesReturnCollectionNewClientArticlesReturn.setArticleId(article);
                    clientArticlesReturnCollectionNewClientArticlesReturn = em.merge(clientArticlesReturnCollectionNewClientArticlesReturn);
                    if (oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn != null && !oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn.equals(article)) {
                        oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn.getClientArticlesReturnCollection().remove(clientArticlesReturnCollectionNewClientArticlesReturn);
                        oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn = em.merge(oldArticleIdOfClientArticlesReturnCollectionNewClientArticlesReturn);
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
            WarehouseArticles warehouseArticlesOrphanCheck = article.getWarehouseArticles();
            if (warehouseArticlesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the WarehouseArticles " + warehouseArticlesOrphanCheck + " in its warehouseArticles field has a non-nullable articleId field.");
            }
            AisleArticles aisleArticlesOrphanCheck = article.getAisleArticles();
            if (aisleArticlesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the AisleArticles " + aisleArticlesOrphanCheck + " in its aisleArticles field has a non-nullable articleId field.");
            }
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
            Collection<TransactionArticles> transactionArticlesCollectionOrphanCheck = article.getTransactionArticlesCollection();
            for (TransactionArticles transactionArticlesCollectionOrphanCheckTransactionArticles : transactionArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the TransactionArticles " + transactionArticlesCollectionOrphanCheckTransactionArticles + " in its transactionArticlesCollection field has a non-nullable articleId field.");
            }
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionOrphanCheck = article.getClientArticlesReturnCollection();
            for (ClientArticlesReturn clientArticlesReturnCollectionOrphanCheckClientArticlesReturn : clientArticlesReturnCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Article (" + article + ") cannot be destroyed since the ClientArticlesReturn " + clientArticlesReturnCollectionOrphanCheckClientArticlesReturn + " in its clientArticlesReturnCollection field has a non-nullable articleId field.");
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
    
    public Article findByName(String name){
        List<Article> l = getEntityManager().createNamedQuery("Article.findByName")
            .setParameter("name", name)
            .getResultList();
        if(l.isEmpty()) return null;
        return l.get(0);
    }
    
    
    public void reloveAll(){
        getEntityManager().createNamedQuery("Article.removeAll").executeUpdate();
    }
}
