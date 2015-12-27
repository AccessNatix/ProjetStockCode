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
import modele.entity.CommandedArticles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import modele.entity.Command;
import modele.entity.RetailerCommands;

/**
 *
 * @author yo
 */
public class CommandJpaController implements Serializable {

    private static CommandJpaController singleton = null;
    
    
    public static CommandJpaController getController(){
        if(singleton==null) singleton = new CommandJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private CommandJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Command command) {
        if (command.getCommandedArticlesCollection() == null) {
            command.setCommandedArticlesCollection(new ArrayList<CommandedArticles>());
        }
        if (command.getRetailerCommandsCollection() == null) {
            command.setRetailerCommandsCollection(new ArrayList<RetailerCommands>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provider providerId = command.getProviderId();
            if (providerId != null) {
                providerId = em.getReference(providerId.getClass(), providerId.getId());
                command.setProviderId(providerId);
            }
            Collection<CommandedArticles> attachedCommandedArticlesCollection = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesCollectionCommandedArticlesToAttach : command.getCommandedArticlesCollection()) {
                commandedArticlesCollectionCommandedArticlesToAttach = em.getReference(commandedArticlesCollectionCommandedArticlesToAttach.getClass(), commandedArticlesCollectionCommandedArticlesToAttach.getId());
                attachedCommandedArticlesCollection.add(commandedArticlesCollectionCommandedArticlesToAttach);
            }
            command.setCommandedArticlesCollection(attachedCommandedArticlesCollection);
            Collection<RetailerCommands> attachedRetailerCommandsCollection = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsCollectionRetailerCommandsToAttach : command.getRetailerCommandsCollection()) {
                retailerCommandsCollectionRetailerCommandsToAttach = em.getReference(retailerCommandsCollectionRetailerCommandsToAttach.getClass(), retailerCommandsCollectionRetailerCommandsToAttach.getId());
                attachedRetailerCommandsCollection.add(retailerCommandsCollectionRetailerCommandsToAttach);
            }
            command.setRetailerCommandsCollection(attachedRetailerCommandsCollection);
            em.persist(command);
            if (providerId != null) {
                providerId.getCommandCollection().add(command);
                providerId = em.merge(providerId);
            }
            for (CommandedArticles commandedArticlesCollectionCommandedArticles : command.getCommandedArticlesCollection()) {
                Command oldCommandIdOfCommandedArticlesCollectionCommandedArticles = commandedArticlesCollectionCommandedArticles.getCommandId();
                commandedArticlesCollectionCommandedArticles.setCommandId(command);
                commandedArticlesCollectionCommandedArticles = em.merge(commandedArticlesCollectionCommandedArticles);
                if (oldCommandIdOfCommandedArticlesCollectionCommandedArticles != null) {
                    oldCommandIdOfCommandedArticlesCollectionCommandedArticles.getCommandedArticlesCollection().remove(commandedArticlesCollectionCommandedArticles);
                    oldCommandIdOfCommandedArticlesCollectionCommandedArticles = em.merge(oldCommandIdOfCommandedArticlesCollectionCommandedArticles);
                }
            }
            for (RetailerCommands retailerCommandsCollectionRetailerCommands : command.getRetailerCommandsCollection()) {
                Command oldCommandIdOfRetailerCommandsCollectionRetailerCommands = retailerCommandsCollectionRetailerCommands.getCommandId();
                retailerCommandsCollectionRetailerCommands.setCommandId(command);
                retailerCommandsCollectionRetailerCommands = em.merge(retailerCommandsCollectionRetailerCommands);
                if (oldCommandIdOfRetailerCommandsCollectionRetailerCommands != null) {
                    oldCommandIdOfRetailerCommandsCollectionRetailerCommands.getRetailerCommandsCollection().remove(retailerCommandsCollectionRetailerCommands);
                    oldCommandIdOfRetailerCommandsCollectionRetailerCommands = em.merge(oldCommandIdOfRetailerCommandsCollectionRetailerCommands);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Command command) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Command persistentCommand = em.find(Command.class, command.getId());
            Provider providerIdOld = persistentCommand.getProviderId();
            Provider providerIdNew = command.getProviderId();
            Collection<CommandedArticles> commandedArticlesCollectionOld = persistentCommand.getCommandedArticlesCollection();
            Collection<CommandedArticles> commandedArticlesCollectionNew = command.getCommandedArticlesCollection();
            Collection<RetailerCommands> retailerCommandsCollectionOld = persistentCommand.getRetailerCommandsCollection();
            Collection<RetailerCommands> retailerCommandsCollectionNew = command.getRetailerCommandsCollection();
            List<String> illegalOrphanMessages = null;
            for (CommandedArticles commandedArticlesCollectionOldCommandedArticles : commandedArticlesCollectionOld) {
                if (!commandedArticlesCollectionNew.contains(commandedArticlesCollectionOldCommandedArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommandedArticles " + commandedArticlesCollectionOldCommandedArticles + " since its commandId field is not nullable.");
                }
            }
            for (RetailerCommands retailerCommandsCollectionOldRetailerCommands : retailerCommandsCollectionOld) {
                if (!retailerCommandsCollectionNew.contains(retailerCommandsCollectionOldRetailerCommands)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RetailerCommands " + retailerCommandsCollectionOldRetailerCommands + " since its commandId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (providerIdNew != null) {
                providerIdNew = em.getReference(providerIdNew.getClass(), providerIdNew.getId());
                command.setProviderId(providerIdNew);
            }
            Collection<CommandedArticles> attachedCommandedArticlesCollectionNew = new ArrayList<CommandedArticles>();
            for (CommandedArticles commandedArticlesCollectionNewCommandedArticlesToAttach : commandedArticlesCollectionNew) {
                commandedArticlesCollectionNewCommandedArticlesToAttach = em.getReference(commandedArticlesCollectionNewCommandedArticlesToAttach.getClass(), commandedArticlesCollectionNewCommandedArticlesToAttach.getId());
                attachedCommandedArticlesCollectionNew.add(commandedArticlesCollectionNewCommandedArticlesToAttach);
            }
            commandedArticlesCollectionNew = attachedCommandedArticlesCollectionNew;
            command.setCommandedArticlesCollection(commandedArticlesCollectionNew);
            Collection<RetailerCommands> attachedRetailerCommandsCollectionNew = new ArrayList<RetailerCommands>();
            for (RetailerCommands retailerCommandsCollectionNewRetailerCommandsToAttach : retailerCommandsCollectionNew) {
                retailerCommandsCollectionNewRetailerCommandsToAttach = em.getReference(retailerCommandsCollectionNewRetailerCommandsToAttach.getClass(), retailerCommandsCollectionNewRetailerCommandsToAttach.getId());
                attachedRetailerCommandsCollectionNew.add(retailerCommandsCollectionNewRetailerCommandsToAttach);
            }
            retailerCommandsCollectionNew = attachedRetailerCommandsCollectionNew;
            command.setRetailerCommandsCollection(retailerCommandsCollectionNew);
            command = em.merge(command);
            if (providerIdOld != null && !providerIdOld.equals(providerIdNew)) {
                providerIdOld.getCommandCollection().remove(command);
                providerIdOld = em.merge(providerIdOld);
            }
            if (providerIdNew != null && !providerIdNew.equals(providerIdOld)) {
                providerIdNew.getCommandCollection().add(command);
                providerIdNew = em.merge(providerIdNew);
            }
            for (CommandedArticles commandedArticlesCollectionNewCommandedArticles : commandedArticlesCollectionNew) {
                if (!commandedArticlesCollectionOld.contains(commandedArticlesCollectionNewCommandedArticles)) {
                    Command oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles = commandedArticlesCollectionNewCommandedArticles.getCommandId();
                    commandedArticlesCollectionNewCommandedArticles.setCommandId(command);
                    commandedArticlesCollectionNewCommandedArticles = em.merge(commandedArticlesCollectionNewCommandedArticles);
                    if (oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles != null && !oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles.equals(command)) {
                        oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles.getCommandedArticlesCollection().remove(commandedArticlesCollectionNewCommandedArticles);
                        oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles = em.merge(oldCommandIdOfCommandedArticlesCollectionNewCommandedArticles);
                    }
                }
            }
            for (RetailerCommands retailerCommandsCollectionNewRetailerCommands : retailerCommandsCollectionNew) {
                if (!retailerCommandsCollectionOld.contains(retailerCommandsCollectionNewRetailerCommands)) {
                    Command oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands = retailerCommandsCollectionNewRetailerCommands.getCommandId();
                    retailerCommandsCollectionNewRetailerCommands.setCommandId(command);
                    retailerCommandsCollectionNewRetailerCommands = em.merge(retailerCommandsCollectionNewRetailerCommands);
                    if (oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands != null && !oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands.equals(command)) {
                        oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands.getRetailerCommandsCollection().remove(retailerCommandsCollectionNewRetailerCommands);
                        oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands = em.merge(oldCommandIdOfRetailerCommandsCollectionNewRetailerCommands);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = command.getId();
                if (findCommand(id) == null) {
                    throw new NonexistentEntityException("The command with id " + id + " no longer exists.");
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
            Command command;
            try {
                command = em.getReference(Command.class, id);
                command.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The command with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CommandedArticles> commandedArticlesCollectionOrphanCheck = command.getCommandedArticlesCollection();
            for (CommandedArticles commandedArticlesCollectionOrphanCheckCommandedArticles : commandedArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Command (" + command + ") cannot be destroyed since the CommandedArticles " + commandedArticlesCollectionOrphanCheckCommandedArticles + " in its commandedArticlesCollection field has a non-nullable commandId field.");
            }
            Collection<RetailerCommands> retailerCommandsCollectionOrphanCheck = command.getRetailerCommandsCollection();
            for (RetailerCommands retailerCommandsCollectionOrphanCheckRetailerCommands : retailerCommandsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Command (" + command + ") cannot be destroyed since the RetailerCommands " + retailerCommandsCollectionOrphanCheckRetailerCommands + " in its retailerCommandsCollection field has a non-nullable commandId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provider providerId = command.getProviderId();
            if (providerId != null) {
                providerId.getCommandCollection().remove(command);
                providerId = em.merge(providerId);
            }
            em.remove(command);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Command> findCommandEntities() {
        return findCommandEntities(true, -1, -1);
    }

    public List<Command> findCommandEntities(int maxResults, int firstResult) {
        return findCommandEntities(false, maxResults, firstResult);
    }

    private List<Command> findCommandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Command.class));
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

    public Command findCommand(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Command.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Command> rt = cq.from(Command.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Command> getNonDealt(){
        return getEntityManager().createNamedQuery("Command.findNotDealt").getResultList();
    }   
    
    public List<Command> getDealt(){
        return getEntityManager().createNamedQuery("Command.findDealt").getResultList();
    }
    
    
    public void reloveAll(){
        EntityManager em = getEntityManager();
        EntityTransaction transac = em.getTransaction();
        transac.begin();
        em.createNamedQuery("Command.removeAll").executeUpdate();
        transac.commit();
        em.close();
    }
}
