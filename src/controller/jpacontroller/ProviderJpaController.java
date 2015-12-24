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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.Command;
import modele.entity.Provider;

/**
 *
 * @author yo
 */
public class ProviderJpaController implements Serializable {

    private static ProviderJpaController singleton = null;
    
    
    public static ProviderJpaController getController(){
        if(singleton==null) singleton = new ProviderJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private ProviderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provider provider) {
        if (provider.getArticleCollection() == null) {
            provider.setArticleCollection(new ArrayList<Article>());
        }
        if (provider.getCommandCollection() == null) {
            provider.setCommandCollection(new ArrayList<Command>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Article> attachedArticleCollection = new ArrayList<Article>();
            for (Article articleCollectionArticleToAttach : provider.getArticleCollection()) {
                articleCollectionArticleToAttach = em.getReference(articleCollectionArticleToAttach.getClass(), articleCollectionArticleToAttach.getId());
                attachedArticleCollection.add(articleCollectionArticleToAttach);
            }
            provider.setArticleCollection(attachedArticleCollection);
            Collection<Command> attachedCommandCollection = new ArrayList<Command>();
            for (Command commandCollectionCommandToAttach : provider.getCommandCollection()) {
                commandCollectionCommandToAttach = em.getReference(commandCollectionCommandToAttach.getClass(), commandCollectionCommandToAttach.getId());
                attachedCommandCollection.add(commandCollectionCommandToAttach);
            }
            provider.setCommandCollection(attachedCommandCollection);
            em.persist(provider);
            for (Article articleCollectionArticle : provider.getArticleCollection()) {
                Provider oldProviderIdOfArticleCollectionArticle = articleCollectionArticle.getProviderId();
                articleCollectionArticle.setProviderId(provider);
                articleCollectionArticle = em.merge(articleCollectionArticle);
                if (oldProviderIdOfArticleCollectionArticle != null) {
                    oldProviderIdOfArticleCollectionArticle.getArticleCollection().remove(articleCollectionArticle);
                    oldProviderIdOfArticleCollectionArticle = em.merge(oldProviderIdOfArticleCollectionArticle);
                }
            }
            for (Command commandCollectionCommand : provider.getCommandCollection()) {
                Provider oldProviderIdOfCommandCollectionCommand = commandCollectionCommand.getProviderId();
                commandCollectionCommand.setProviderId(provider);
                commandCollectionCommand = em.merge(commandCollectionCommand);
                if (oldProviderIdOfCommandCollectionCommand != null) {
                    oldProviderIdOfCommandCollectionCommand.getCommandCollection().remove(commandCollectionCommand);
                    oldProviderIdOfCommandCollectionCommand = em.merge(oldProviderIdOfCommandCollectionCommand);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provider provider) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider persistentProvider = em.find(Provider.class, provider.getId());
            Collection<Article> articleCollectionOld = persistentProvider.getArticleCollection();
            Collection<Article> articleCollectionNew = provider.getArticleCollection();
            Collection<Command> commandCollectionOld = persistentProvider.getCommandCollection();
            Collection<Command> commandCollectionNew = provider.getCommandCollection();
            List<String> illegalOrphanMessages = null;
            for (Article articleCollectionOldArticle : articleCollectionOld) {
                if (!articleCollectionNew.contains(articleCollectionOldArticle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Article " + articleCollectionOldArticle + " since its providerId field is not nullable.");
                }
            }
            for (Command commandCollectionOldCommand : commandCollectionOld) {
                if (!commandCollectionNew.contains(commandCollectionOldCommand)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Command " + commandCollectionOldCommand + " since its providerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Article> attachedArticleCollectionNew = new ArrayList<Article>();
            for (Article articleCollectionNewArticleToAttach : articleCollectionNew) {
                articleCollectionNewArticleToAttach = em.getReference(articleCollectionNewArticleToAttach.getClass(), articleCollectionNewArticleToAttach.getId());
                attachedArticleCollectionNew.add(articleCollectionNewArticleToAttach);
            }
            articleCollectionNew = attachedArticleCollectionNew;
            provider.setArticleCollection(articleCollectionNew);
            Collection<Command> attachedCommandCollectionNew = new ArrayList<Command>();
            for (Command commandCollectionNewCommandToAttach : commandCollectionNew) {
                commandCollectionNewCommandToAttach = em.getReference(commandCollectionNewCommandToAttach.getClass(), commandCollectionNewCommandToAttach.getId());
                attachedCommandCollectionNew.add(commandCollectionNewCommandToAttach);
            }
            commandCollectionNew = attachedCommandCollectionNew;
            provider.setCommandCollection(commandCollectionNew);
            provider = em.merge(provider);
            for (Article articleCollectionNewArticle : articleCollectionNew) {
                if (!articleCollectionOld.contains(articleCollectionNewArticle)) {
                    Provider oldProviderIdOfArticleCollectionNewArticle = articleCollectionNewArticle.getProviderId();
                    articleCollectionNewArticle.setProviderId(provider);
                    articleCollectionNewArticle = em.merge(articleCollectionNewArticle);
                    if (oldProviderIdOfArticleCollectionNewArticle != null && !oldProviderIdOfArticleCollectionNewArticle.equals(provider)) {
                        oldProviderIdOfArticleCollectionNewArticle.getArticleCollection().remove(articleCollectionNewArticle);
                        oldProviderIdOfArticleCollectionNewArticle = em.merge(oldProviderIdOfArticleCollectionNewArticle);
                    }
                }
            }
            for (Command commandCollectionNewCommand : commandCollectionNew) {
                if (!commandCollectionOld.contains(commandCollectionNewCommand)) {
                    Provider oldProviderIdOfCommandCollectionNewCommand = commandCollectionNewCommand.getProviderId();
                    commandCollectionNewCommand.setProviderId(provider);
                    commandCollectionNewCommand = em.merge(commandCollectionNewCommand);
                    if (oldProviderIdOfCommandCollectionNewCommand != null && !oldProviderIdOfCommandCollectionNewCommand.equals(provider)) {
                        oldProviderIdOfCommandCollectionNewCommand.getCommandCollection().remove(commandCollectionNewCommand);
                        oldProviderIdOfCommandCollectionNewCommand = em.merge(oldProviderIdOfCommandCollectionNewCommand);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = provider.getId();
                if (findProvider(id) == null) {
                    throw new NonexistentEntityException("The provider with id " + id + " no longer exists.");
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
            Provider provider;
            try {
                provider = em.getReference(Provider.class, id);
                provider.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provider with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Article> articleCollectionOrphanCheck = provider.getArticleCollection();
            for (Article articleCollectionOrphanCheckArticle : articleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provider (" + provider + ") cannot be destroyed since the Article " + articleCollectionOrphanCheckArticle + " in its articleCollection field has a non-nullable providerId field.");
            }
            Collection<Command> commandCollectionOrphanCheck = provider.getCommandCollection();
            for (Command commandCollectionOrphanCheckCommand : commandCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provider (" + provider + ") cannot be destroyed since the Command " + commandCollectionOrphanCheckCommand + " in its commandCollection field has a non-nullable providerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(provider);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provider> findProviderEntities() {
        return findProviderEntities(true, -1, -1);
    }

    public List<Provider> findProviderEntities(int maxResults, int firstResult) {
        return findProviderEntities(false, maxResults, firstResult);
    }

    private List<Provider> findProviderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provider.class));
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

    public Provider findProvider(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provider.class, id);
        } finally {
            em.close();
        }
    }

    public int getProviderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provider> rt = cq.from(Provider.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
