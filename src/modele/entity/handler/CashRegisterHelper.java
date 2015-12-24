/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import java.util.Objects;
import modele.entity.CashRegister;
import modele.entity.Key;

/**
 *
 * @author yo
 */
public class CashRegisterHelper {
    private SessionHelper aSessionHelper;
    private CashRegister aCashRegister;
    private boolean open;
    
    public CashRegisterHelper(CashRegister pCashRegister){
        aSessionHelper = new SessionHelper();
        aCashRegister = pCashRegister;
        open = false;
    }
    
    public boolean connect(String pseudo, String password){
        return aSessionHelper.connect(pseudo, password);
    }
    
    public boolean open(Key key){
        if(Objects.equals(key.getCashRegisterid().getId(), aCashRegister.getId())){
            open = true;
            return open;
        }
        return false;
    }
}
