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
import modele.entity.SessionTransactions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modele.entity.TransactionArticles;
import modele.entity.Payment;
import modele.entity.Transaction;

/**
 *
 * @author yo
 */
public class TransactionJpaController implements Serializable {

    private static TransactionJpaController singleton = null;
    
    
    public static TransactionJpaController getController(){
        if(singleton==null) singleton = new TransactionJpaController(javax.persistence.Persistence.createEntityManagerFactory("UMLProjectPU"));
        return singleton;
    }

    private TransactionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaction transaction) {
        if (transaction.getSessionTransactionsCollection() == null) {
            transaction.setSessionTransactionsCollection(new ArrayList<SessionTransactions>());
        }
        if (transaction.getTransactionArticlesCollection() == null) {
            transaction.setTransactionArticlesCollection(new ArrayList<TransactionArticles>());
        }
        if (transaction.getPaymentCollection() == null) {
            transaction.setPaymentCollection(new ArrayList<Payment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SessionTransactions> attachedSessionTransactionsCollection = new ArrayList<SessionTransactions>();
            for (SessionTransactions sessionTransactionsCollectionSessionTransactionsToAttach : transaction.getSessionTransactionsCollection()) {
                sessionTransactionsCollectionSessionTransactionsToAttach = em.getReference(sessionTransactionsCollectionSessionTransactionsToAttach.getClass(), sessionTransactionsCollectionSessionTransactionsToAttach.getId());
                attachedSessionTransactionsCollection.add(sessionTransactionsCollectionSessionTransactionsToAttach);
            }
            transaction.setSessionTransactionsCollection(attachedSessionTransactionsCollection);
            Collection<TransactionArticles> attachedTransactionArticlesCollection = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionTransactionArticlesToAttach : transaction.getTransactionArticlesCollection()) {
                transactionArticlesCollectionTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionTransactionArticlesToAttach.getClass(), transactionArticlesCollectionTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollection.add(transactionArticlesCollectionTransactionArticlesToAttach);
            }
            transaction.setTransactionArticlesCollection(attachedTransactionArticlesCollection);
            Collection<Payment> attachedPaymentCollection = new ArrayList<Payment>();
            for (Payment paymentCollectionPaymentToAttach : transaction.getPaymentCollection()) {
                paymentCollectionPaymentToAttach = em.getReference(paymentCollectionPaymentToAttach.getClass(), paymentCollectionPaymentToAttach.getId());
                attachedPaymentCollection.add(paymentCollectionPaymentToAttach);
            }
            transaction.setPaymentCollection(attachedPaymentCollection);
            em.persist(transaction);
            for (SessionTransactions sessionTransactionsCollectionSessionTransactions : transaction.getSessionTransactionsCollection()) {
                Transaction oldTransactionIdOfSessionTransactionsCollectionSessionTransactions = sessionTransactionsCollectionSessionTransactions.getTransactionId();
                sessionTransactionsCollectionSessionTransactions.setTransactionId(transaction);
                sessionTransactionsCollectionSessionTransactions = em.merge(sessionTransactionsCollectionSessionTransactions);
                if (oldTransactionIdOfSessionTransactionsCollectionSessionTransactions != null) {
                    oldTransactionIdOfSessionTransactionsCollectionSessionTransactions.getSessionTransactionsCollection().remove(sessionTransactionsCollectionSessionTransactions);
                    oldTransactionIdOfSessionTransactionsCollectionSessionTransactions = em.merge(oldTransactionIdOfSessionTransactionsCollectionSessionTransactions);
                }
            }
            for (TransactionArticles transactionArticlesCollectionTransactionArticles : transaction.getTransactionArticlesCollection()) {
                Transaction oldTransactionIdOfTransactionArticlesCollectionTransactionArticles = transactionArticlesCollectionTransactionArticles.getTransactionId();
                transactionArticlesCollectionTransactionArticles.setTransactionId(transaction);
                transactionArticlesCollectionTransactionArticles = em.merge(transactionArticlesCollectionTransactionArticles);
                if (oldTransactionIdOfTransactionArticlesCollectionTransactionArticles != null) {
                    oldTransactionIdOfTransactionArticlesCollectionTransactionArticles.getTransactionArticlesCollection().remove(transactionArticlesCollectionTransactionArticles);
                    oldTransactionIdOfTransactionArticlesCollectionTransactionArticles = em.merge(oldTransactionIdOfTransactionArticlesCollectionTransactionArticles);
                }
            }
            for (Payment paymentCollectionPayment : transaction.getPaymentCollection()) {
                Transaction oldTransactionIdOfPaymentCollectionPayment = paymentCollectionPayment.getTransactionId();
                paymentCollectionPayment.setTransactionId(transaction);
                paymentCollectionPayment = em.merge(paymentCollectionPayment);
                if (oldTransactionIdOfPaymentCollectionPayment != null) {
                    oldTransactionIdOfPaymentCollectionPayment.getPaymentCollection().remove(paymentCollectionPayment);
                    oldTransactionIdOfPaymentCollectionPayment = em.merge(oldTransactionIdOfPaymentCollectionPayment);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaction transaction) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction persistentTransaction = em.find(Transaction.class, transaction.getId());
            Collection<SessionTransactions> sessionTransactionsCollectionOld = persistentTransaction.getSessionTransactionsCollection();
            Collection<SessionTransactions> sessionTransactionsCollectionNew = transaction.getSessionTransactionsCollection();
            Collection<TransactionArticles> transactionArticlesCollectionOld = persistentTransaction.getTransactionArticlesCollection();
            Collection<TransactionArticles> transactionArticlesCollectionNew = transaction.getTransactionArticlesCollection();
            Collection<Payment> paymentCollectionOld = persistentTransaction.getPaymentCollection();
            Collection<Payment> paymentCollectionNew = transaction.getPaymentCollection();
            List<String> illegalOrphanMessages = null;
            for (SessionTransactions sessionTransactionsCollectionOldSessionTransactions : sessionTransactionsCollectionOld) {
                if (!sessionTransactionsCollectionNew.contains(sessionTransactionsCollectionOldSessionTransactions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SessionTransactions " + sessionTransactionsCollectionOldSessionTransactions + " since its transactionId field is not nullable.");
                }
            }
            for (TransactionArticles transactionArticlesCollectionOldTransactionArticles : transactionArticlesCollectionOld) {
                if (!transactionArticlesCollectionNew.contains(transactionArticlesCollectionOldTransactionArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TransactionArticles " + transactionArticlesCollectionOldTransactionArticles + " since its transactionId field is not nullable.");
                }
            }
            for (Payment paymentCollectionOldPayment : paymentCollectionOld) {
                if (!paymentCollectionNew.contains(paymentCollectionOldPayment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Payment " + paymentCollectionOldPayment + " since its transactionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SessionTransactions> attachedSessionTransactionsCollectionNew = new ArrayList<SessionTransactions>();
            for (SessionTransactions sessionTransactionsCollectionNewSessionTransactionsToAttach : sessionTransactionsCollectionNew) {
                sessionTransactionsCollectionNewSessionTransactionsToAttach = em.getReference(sessionTransactionsCollectionNewSessionTransactionsToAttach.getClass(), sessionTransactionsCollectionNewSessionTransactionsToAttach.getId());
                attachedSessionTransactionsCollectionNew.add(sessionTransactionsCollectionNewSessionTransactionsToAttach);
            }
            sessionTransactionsCollectionNew = attachedSessionTransactionsCollectionNew;
            transaction.setSessionTransactionsCollection(sessionTransactionsCollectionNew);
            Collection<TransactionArticles> attachedTransactionArticlesCollectionNew = new ArrayList<TransactionArticles>();
            for (TransactionArticles transactionArticlesCollectionNewTransactionArticlesToAttach : transactionArticlesCollectionNew) {
                transactionArticlesCollectionNewTransactionArticlesToAttach = em.getReference(transactionArticlesCollectionNewTransactionArticlesToAttach.getClass(), transactionArticlesCollectionNewTransactionArticlesToAttach.getId());
                attachedTransactionArticlesCollectionNew.add(transactionArticlesCollectionNewTransactionArticlesToAttach);
            }
            transactionArticlesCollectionNew = attachedTransactionArticlesCollectionNew;
            transaction.setTransactionArticlesCollection(transactionArticlesCollectionNew);
            Collection<Payment> attachedPaymentCollectionNew = new ArrayList<Payment>();
            for (Payment paymentCollectionNewPaymentToAttach : paymentCollectionNew) {
                paymentCollectionNewPaymentToAttach = em.getReference(paymentCollectionNewPaymentToAttach.getClass(), paymentCollectionNewPaymentToAttach.getId());
                attachedPaymentCollectionNew.add(paymentCollectionNewPaymentToAttach);
            }
            paymentCollectionNew = attachedPaymentCollectionNew;
            transaction.setPaymentCollection(paymentCollectionNew);
            transaction = em.merge(transaction);
            for (SessionTransactions sessionTransactionsCollectionNewSessionTransactions : sessionTransactionsCollectionNew) {
                if (!sessionTransactionsCollectionOld.contains(sessionTransactionsCollectionNewSessionTransactions)) {
                    Transaction oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions = sessionTransactionsCollectionNewSessionTransactions.getTransactionId();
                    sessionTransactionsCollectionNewSessionTransactions.setTransactionId(transaction);
                    sessionTransactionsCollectionNewSessionTransactions = em.merge(sessionTransactionsCollectionNewSessionTransactions);
                    if (oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions != null && !oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions.equals(transaction)) {
                        oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions.getSessionTransactionsCollection().remove(sessionTransactionsCollectionNewSessionTransactions);
                        oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions = em.merge(oldTransactionIdOfSessionTransactionsCollectionNewSessionTransactions);
                    }
                }
            }
            for (TransactionArticles transactionArticlesCollectionNewTransactionArticles : transactionArticlesCollectionNew) {
                if (!transactionArticlesCollectionOld.contains(transactionArticlesCollectionNewTransactionArticles)) {
                    Transaction oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles = transactionArticlesCollectionNewTransactionArticles.getTransactionId();
                    transactionArticlesCollectionNewTransactionArticles.setTransactionId(transaction);
                    transactionArticlesCollectionNewTransactionArticles = em.merge(transactionArticlesCollectionNewTransactionArticles);
                    if (oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles != null && !oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles.equals(transaction)) {
                        oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles.getTransactionArticlesCollection().remove(transactionArticlesCollectionNewTransactionArticles);
                        oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles = em.merge(oldTransactionIdOfTransactionArticlesCollectionNewTransactionArticles);
                    }
                }
            }
            for (Payment paymentCollectionNewPayment : paymentCollectionNew) {
                if (!paymentCollectionOld.contains(paymentCollectionNewPayment)) {
                    Transaction oldTransactionIdOfPaymentCollectionNewPayment = paymentCollectionNewPayment.getTransactionId();
                    paymentCollectionNewPayment.setTransactionId(transaction);
                    paymentCollectionNewPayment = em.merge(paymentCollectionNewPayment);
                    if (oldTransactionIdOfPaymentCollectionNewPayment != null && !oldTransactionIdOfPaymentCollectionNewPayment.equals(transaction)) {
                        oldTransactionIdOfPaymentCollectionNewPayment.getPaymentCollection().remove(paymentCollectionNewPayment);
                        oldTransactionIdOfPaymentCollectionNewPayment = em.merge(oldTransactionIdOfPaymentCollectionNewPayment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaction.getId();
                if (findTransaction(id) == null) {
                    throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.");
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
            Transaction transaction;
            try {
                transaction = em.getReference(Transaction.class, id);
                transaction.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SessionTransactions> sessionTransactionsCollectionOrphanCheck = transaction.getSessionTransactionsCollection();
            for (SessionTransactions sessionTransactionsCollectionOrphanCheckSessionTransactions : sessionTransactionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaction (" + transaction + ") cannot be destroyed since the SessionTransactions " + sessionTransactionsCollectionOrphanCheckSessionTransactions + " in its sessionTransactionsCollection field has a non-nullable transactionId field.");
            }
            Collection<TransactionArticles> transactionArticlesCollectionOrphanCheck = transaction.getTransactionArticlesCollection();
            for (TransactionArticles transactionArticlesCollectionOrphanCheckTransactionArticles : transactionArticlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaction (" + transaction + ") cannot be destroyed since the TransactionArticles " + transactionArticlesCollectionOrphanCheckTransactionArticles + " in its transactionArticlesCollection field has a non-nullable transactionId field.");
            }
            Collection<Payment> paymentCollectionOrphanCheck = transaction.getPaymentCollection();
            for (Payment paymentCollectionOrphanCheckPayment : paymentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Transaction (" + transaction + ") cannot be destroyed since the Payment " + paymentCollectionOrphanCheckPayment + " in its paymentCollection field has a non-nullable transactionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(transaction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaction> findTransactionEntities() {
        return findTransactionEntities(true, -1, -1);
    }

    public List<Transaction> findTransactionEntities(int maxResults, int firstResult) {
        return findTransactionEntities(false, maxResults, firstResult);
    }

    private List<Transaction> findTransactionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaction.class));
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

    public Transaction findTransaction(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaction.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransactionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaction> rt = cq.from(Transaction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
