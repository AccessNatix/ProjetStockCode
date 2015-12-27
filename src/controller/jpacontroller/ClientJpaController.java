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
import modele.entity.ClientArticles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.Client;
import modele.entity.ClientArticlesReturn;

/**
 *
 * @author yo
 */
public class ClientJpaController implements Serializable {

    private static ClientJpaController singleton = null;
    
    
    public static ClientJpaController getController(){
        if(singleton==null) singleton = new ClientJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) {
        if (client.getClientArticlesCollection() == null) {
            client.setClientArticlesCollection(new ArrayList<ClientArticles>());
        }
        if (client.getClientArticlesReturnCollection() == null) {
            client.setClientArticlesReturnCollection(new ArrayList<ClientArticlesReturn>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ClientArticles> attachedClientArticlesCollection = new ArrayList<ClientArticles>();
            for (ClientArticles clientArticlesCollectionClientArticlesToAttach : client.getClientArticlesCollection()) {
                clientArticlesCollectionClientArticlesToAttach = em.getReference(clientArticlesCollectionClientArticlesToAttach.getClass(), clientArticlesCollectionClientArticlesToAttach.getId());
                attachedClientArticlesCollection.add(clientArticlesCollectionClientArticlesToAttach);
            }
            client.setClientArticlesCollection(attachedClientArticlesCollection);
            Collection<ClientArticlesReturn> attachedClientArticlesReturnCollection = new ArrayList<ClientArticlesReturn>();
            for (ClientArticlesReturn clientArticlesReturnCollectionClientArticlesReturnToAttach : client.getClientArticlesReturnCollection()) {
                clientArticlesReturnCollectionClientArticlesReturnToAttach = em.getReference(clientArticlesReturnCollectionClientArticlesReturnToAttach.getClass(), clientArticlesReturnCollectionClientArticlesReturnToAttach.getId());
                attachedClientArticlesReturnCollection.add(clientArticlesReturnCollectionClientArticlesReturnToAttach);
            }
            client.setClientArticlesReturnCollection(attachedClientArticlesReturnCollection);
            em.persist(client);
            for (ClientArticles clientArticlesCollectionClientArticles : client.getClientArticlesCollection()) {
                Client oldClientIdOfClientArticlesCollectionClientArticles = clientArticlesCollectionClientArticles.getClientId();
                clientArticlesCollectionClientArticles.setClientId(client);
                clientArticlesCollectionClientArticles = em.merge(clientArticlesCollectionClientArticles);
                if (oldClientIdOfClientArticlesCollectionClientArticles != null) {
                    oldClientIdOfClientArticlesCollectionClientArticles.getClientArticlesCollection().remove(clientArticlesCollectionClientArticles);
                    oldClientIdOfClientArticlesCollectionClientArticles = em.merge(oldClientIdOfClientArticlesCollectionClientArticles);
                }
            }
            for (ClientArticlesReturn clientArticlesReturnCollectionClientArticlesReturn : client.getClientArticlesReturnCollection()) {
                Client oldClientIdOfClientArticlesReturnCollectionClientArticlesReturn = clientArticlesReturnCollectionClientArticlesReturn.getClientId();
                clientArticlesReturnCollectionClientArticlesReturn.setClientId(client);
                clientArticlesReturnCollectionClientArticlesReturn = em.merge(clientArticlesReturnCollectionClientArticlesReturn);
                if (oldClientIdOfClientArticlesReturnCollectionClientArticlesReturn != null) {
                    oldClientIdOfClientArticlesReturnCollectionClientArticlesReturn.getClientArticlesReturnCollection().remove(clientArticlesReturnCollectionClientArticlesReturn);
                    oldClientIdOfClientArticlesReturnCollectionClientArticlesReturn = em.merge(oldClientIdOfClientArticlesReturnCollectionClientArticlesReturn);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getId());
            Collection<ClientArticles> clientArticlesCollectionOld = persistentClient.getClientArticlesCollection();
            Collection<ClientArticles> clientArticlesCollectionNew = client.getClientArticlesCollection();
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionOld = persistentClient.getClientArticlesReturnCollection();
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionNew = client.getClientArticlesReturnCollection();
            List<String> illegalOrphanMessages = null;
            for (ClientArticles clientArticlesCollectionOldClientArticles : clientArticlesCollectionOld) {
                if (!clientArticlesCollectionNew.contains(clientArticlesCollectionOldClientArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientArticles " + clientArticlesCollectionOldClientArticles + " since its clientId field is not nullable.");
                }
            }
            for (ClientArticlesReturn clientArticlesReturnCollectionOldClientArticlesReturn : clientArticlesReturnCollectionOld) {
                if (!clientArticlesReturnCollectionNew.contains(clientArticlesReturnCollectionOldClientArticlesReturn)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClientArticlesReturn " + clientArticlesReturnCollectionOldClientArticlesReturn + " since its clientId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ClientArticles> attachedClientArticlesCollectionNew = new ArrayList<ClientArticles>();
            for (ClientArticles clientArticlesCollectionNewClientArticlesToAttach : clientArticlesCollectionNew) {
                clientArticlesCollectionNewClientArticlesToAttach = em.getReference(clientArticlesCollectionNewClientArticlesToAttach.getClass(), clientArticlesCollectionNewClientArticlesToAttach.getId());
                attachedClientArticlesCollectionNew.add(clientArticlesCollectionNewClientArticlesToAttach);
            }
            clientArticlesCollectionNew = attachedClientArticlesCollectionNew;
            client.setClientArticlesCollection(clientArticlesCollectionNew);
            Collection<ClientArticlesReturn> attachedClientArticlesReturnCollectionNew = new ArrayList<ClientArticlesReturn>();
            for (ClientArticlesReturn clientArticlesReturnCollectionNewClientArticlesReturnToAttach : clientArticlesReturnCollectionNew) {
                clientArticlesReturnCollectionNewClientArticlesReturnToAttach = em.getReference(clientArticlesReturnCollectionNewClientArticlesReturnToAttach.getClass(), clientArticlesReturnCollectionNewClientArticlesReturnToAttach.getId());
                attachedClientArticlesReturnCollectionNew.add(clientArticlesReturnCollectionNewClientArticlesReturnToAttach);
            }
            clientArticlesReturnCollectionNew = attachedClientArticlesReturnCollectionNew;
            client.setClientArticlesReturnCollection(clientArticlesReturnCollectionNew);
            client = em.merge(client);
            for (ClientArticles clientArticlesCollectionNewClientArticles : clientArticlesCollectionNew) {
                if (!clientArticlesCollectionOld.contains(clientArticlesCollectionNewClientArticles)) {
                    Client oldClientIdOfClientArticlesCollectionNewClientArticles = clientArticlesCollectionNewClientArticles.getClientId();
                    clientArticlesCollectionNewClientArticles.setClientId(client);
                    clientArticlesCollectionNewClientArticles = em.merge(clientArticlesCollectionNewClientArticles);
                    if (oldClientIdOfClientArticlesCollectionNewClientArticles != null && !oldClientIdOfClientArticlesCollectionNewClientArticles.equals(client)) {
                        oldClientIdOfClientArticlesCollectionNewClientArticles.getClientArticlesCollection().remove(clientArticlesCollectionNewClientArticles);
                        oldClientIdOfClientArticlesCollectionNewClientArticles = em.merge(oldClientIdOfClientArticlesCollectionNewClientArticles);
                    }
                }
            }
            for (ClientArticlesReturn clientArticlesReturnCollectionNewClientArticlesReturn : clientArticlesReturnCollectionNew) {
                if (!clientArticlesReturnCollectionOld.contains(clientArticlesReturnCollectionNewClientArticlesReturn)) {
                    Client oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn = clientArticlesReturnCollectionNewClientArticlesReturn.getClientId();
                    clientArticlesReturnCollectionNewClientArticlesReturn.setClientId(client);
                    clientArticlesReturnCollectionNewClientArticlesReturn = em.merge(clientArticlesReturnCollectionNewClientArticlesReturn);
                    if (oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn != null && !oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn.equals(client)) {
                        oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn.getClientArticlesReturnCollection().remove(clientArticlesReturnCollectionNewClientArticlesReturn);
                        oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn = em.merge(oldClientIdOfClientArticlesReturnCollectionNewClientArticlesReturn);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
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
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ClientArticles> clientArticlesCollectionOrphanCheck = client.getClientArticlesCollection();
            for (ClientArticles clientArticlesCollectionOrphanCheckClientArticles : clientArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the ClientArticles " + clientArticlesCollectionOrphanCheckClientArticles + " in its clientArticlesCollection field has a non-nullable clientId field.");
            }
            Collection<ClientArticlesReturn> clientArticlesReturnCollectionOrphanCheck = client.getClientArticlesReturnCollection();
            for (ClientArticlesReturn clientArticlesReturnCollectionOrphanCheckClientArticlesReturn : clientArticlesReturnCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the ClientArticlesReturn " + clientArticlesReturnCollectionOrphanCheckClientArticlesReturn + " in its clientArticlesReturnCollection field has a non-nullable clientId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public void reloveAll(){
        getEntityManager().createNamedQuery("Client.removeAll").executeUpdate();
    }
}
