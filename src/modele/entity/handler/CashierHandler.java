/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import modele.entity.CashRegister;
import modele.entity.Cashier;
import modele.entity.Transaction;

/**
 *
 * @author yo
 */
public class CashierHandler {
    private Cashier aCashier;
    private CashRegisterHelper aCashRegisterHelper;
    private Transaction aTransaction = null;
    
    
    public CashierHandler(Cashier pCachier, CashRegister pCashRegister){
        this.aCashier=pCachier;
        this.aCashRegisterHelper=pCashRegister;
    }
    
    public void startTransaction(){
        aTransaction = createTransaction();
        
    }
    

    
}
