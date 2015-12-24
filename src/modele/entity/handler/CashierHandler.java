/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import modele.entity.Article;
import modele.entity.CashRegister;
import modele.entity.Cashier;

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
    
    public boolean addArticle(Article article, int quantity){
        return aTransactionHelper.addArticle(article, quantity);
    }
    
    public boolean connect(String pseudo, String password){
        return this.aCashRegisterHelper.connect(pseudo, password);
    }
    
}
