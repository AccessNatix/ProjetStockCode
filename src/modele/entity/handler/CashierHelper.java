/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import modele.entity.Article;
import modele.entity.Cashier;
import modele.entity.ClientArticles;
import modele.entity.Key;
import modele.entity.TransactionArticles;

/**
 *
 * 
 */
public class CashierHelper extends Observable{
    private Cashier aCashier;
    private CashRegisterHelper aCashRegisterHelper;
    private TransactionHelper aTransactionHelper = null;
    
    public CashierHelper(Cashier pCachier, CashRegisterHelper pCashRegisterHelper){
        this.aCashier = pCachier;
        this.aCashRegisterHelper = pCashRegisterHelper;
        this.aTransactionHelper = new TransactionHelper(aCashRegisterHelper.getSessionHelper());
    }
    
    public boolean startTransaction(String type){
        return aTransactionHelper.startTransaction(type);
    }
    
    public boolean addArticlesRefund(ClientHelper clientHelper)
    {
        List<ClientArticles> list = clientHelper.getArticles();
        
        return true;
    }
    
    public boolean addArticles(ClientHelper clientHelper){
        List<ClientArticles> list = clientHelper.getArticles();
        
        for(ClientArticles articles : list){
            if(aTransactionHelper.addArticle(articles.getArticleId(), articles.getQuantity()) == false) return false;
        }
        
        List<TransactionArticles> transArticles = aTransactionHelper.getArticles();
        
        /**
         * On récupéere l'ensemble des articles
         */
        List<Article> articles = new ArrayList<>();
        for(TransactionArticles article : transArticles)
        {
            articles.add(article.getArticleId());
        }
        
        this.setChanged();
        this.notifyObservers(articles);
        
        return true;
    }
    
    public boolean connect(String pseudo, String password){
        if(this.aCashRegisterHelper.connect(pseudo, password))
        {
            return true;
        }
        else
        {
            this.setChanged();
            this.notifyObservers("Accès refusé");
            return false;
        }
    }
    
    public boolean pay(String type){
        return aTransactionHelper.payTransaction(type);
    }
    
    public boolean refund()
    {
        return aTransactionHelper.refundTransaction();
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
