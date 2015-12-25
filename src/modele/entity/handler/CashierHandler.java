/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import java.util.List;
import modele.entity.Cashier;
import modele.entity.ClientArticles;
import modele.entity.Key;
import modele.entity.TransactionArticles;

/**
 *
 * @author yo
 */
public class CashierHandler {
    private Cashier aCashier;
    private CashRegisterHelper aCashRegisterHelper;
    private TransactionHelper aTransactionHelper = null;
    
    public CashierHandler(Cashier pCachier, CashRegisterHelper pCashRegisterHelper){
        this.aCashier = pCachier;
        this.aCashRegisterHelper = pCashRegisterHelper;
        this.aTransactionHelper = new TransactionHelper(aCashRegisterHelper.getSessionHelper());
    }
    
    public boolean startTransaction(String type){
        return aTransactionHelper.startTransaction(type);
    }
    
    public boolean addArticles(ClientHelper clientHelper){
        List<ClientArticles> list = clientHelper.getArticles();
        for(ClientArticles articles : list){
            if(aTransactionHelper.addArticle(articles.getArticleId(), articles.getQuantity()) == false) return false;
        }
        return true;
        //return aTransactionHelper.addArticle(article, quantity);
    }
    
    public boolean connect(String pseudo, String password){
        return this.aCashRegisterHelper.connect(pseudo, password);
    }
    
    public boolean pay(String type){
        return aTransactionHelper.payTransaction(type);
    }
    
    public boolean open(Key key){
        return aCashRegisterHelper.open(key);
    }
    
    public boolean cancelTranslation(){
        aCashRegisterHelper.close();
        return aTransactionHelper.cancelTransaction();
    }
    
    public List<TransactionArticles> getTransactionArticles(){ // print
        return aTransactionHelper.getArticles();
    }
    
    
}
