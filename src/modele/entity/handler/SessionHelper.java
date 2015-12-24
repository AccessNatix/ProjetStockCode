/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.SessionJpaController;
import modele.entity.Session;

/**
 *
 * @author yo
 */
public class SessionHelper {
    private Session aCurrentSession;
    
    public SessionHelper(){
        
    }
    
    public boolean connect(String pseudo, String password){
        Session session = SessionJpaController.getController().findByConnexion(pseudo, password);
        if(session == null) return false;
        aCurrentSession = session;
        return true;
    }
}
