/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.TransactionArticlesJpaController;
import controller.jpacontroller.TransactionJpaController;
import controller.jpacontroller.exceptions.IllegalOrphanException;
import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.Payment;
import modele.entity.Transaction;
import modele.entity.TransactionArticles;
import modele.entity.factory.EntityFactory;

/**
 *
 * @author yo
 */
public class TransactionHelper {
    
    private Transaction aTransaction = null;
    private SessionHelper aSessionHelper;
    
    public TransactionHelper(SessionHelper sessionHelper){
        this.aSessionHelper = sessionHelper;
    }
    
    public boolean startTransaction(String type){
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        aTransaction = EntityFactory.create(new Transaction(), map);
        if(aTransaction == null) return false;
        return aSessionHelper.addTransaction(aTransaction);
    }
    
    public boolean addArticle(Article article, int quantity){
        
        TransactionArticles transactionArticles = TransactionArticlesJpaController.getController().findByIds(article.getId(), aTransaction.getId());
        if(transactionArticles == null){
            HashMap<String, Object> map = new HashMap<>();
            map.put("article", article);
            map.put("quantity", quantity);
            map.put("transaction", aTransaction);
            transactionArticles = EntityFactory.create(new TransactionArticles(), map);
            if(transactionArticles == null) return false;
        }else{
            try {
                transactionArticles.setQuantity(transactionArticles.getQuantity() + quantity);
                TransactionArticlesJpaController.getController().edit(transactionArticles);
            } catch (Exception ex) {
                Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    public boolean removeArticle(Article article, int quantity){
        
        TransactionArticles transactionArticles = TransactionArticlesJpaController.getController().findByIds(article.getId(), aTransaction.getId());
        if(transactionArticles == null) return false;
        if(transactionArticles.getQuantity()<quantity) return false;
        if(transactionArticles.getQuantity()==quantity){
            try {
                TransactionArticlesJpaController.getController().destroy(transactionArticles.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            try {
                transactionArticles.setQuantity(transactionArticles.getQuantity() - quantity);
                TransactionArticlesJpaController.getController().edit(transactionArticles);
            } catch (Exception ex) {
                Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    public boolean cancelTransaction(){        
        if(aTransaction == null) return true;
        List<TransactionArticles> l = getArticles();
        for(TransactionArticles articles : l){
            try {
                TransactionArticlesJpaController.getController().destroy(articles.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        boolean b = aSessionHelper.cancelTransaction(aTransaction);
        
        if(b == false) return false;
        
        try {
            TransactionJpaController.getController().destroy(aTransaction.getId());
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(TransactionHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        aTransaction = null;
        
        return true;
    }
    
    public boolean payTransaction(String type){
        BigDecimal price = new BigDecimal(0);
        List<TransactionArticles> l = getArticles();
        for(TransactionArticles articles : l){
            price.add(articles.getArticleId().getPrice().multiply(new BigDecimal(articles.getQuantity())));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("price", price);
        map.put("transaction", aTransaction);
        Payment payment = EntityFactory.create(new Payment(), map);
        aTransaction = null;
        return payment != null;
    }
    
    public boolean refundTransaction()
    {
        List<TransactionArticles> l = getArticles();
        for(TransactionArticles articles : l){
            SystemStock.getSystemStock().addArticleToWarehouse(articles.getArticleId(), articles.getQuantity());
        }
        aTransaction = null;
        return true;
    }
    
    public List<TransactionArticles> getArticles(){
        return TransactionArticlesJpaController.getController().findByTransactionId(aTransaction.getId());
    }
    
    private double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
}
