/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.TransactionArticlesJpaController;
import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.entity.Article;
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
    
}
