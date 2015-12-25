package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modele.entity.handler.CashierHelper;
import modele.entity.handler.ClientHelper;
import vue.CashierLoginView;
import vue.CashierView;

/**
 * Cette classe est la classe utilisé pour permettre le login
 * 
 * @author anatole
 */
public class ControllerCashier implements ActionListener{
    
    // Modèle
    private final CashierHelper aCashierHandler;
    
    // Vue pour le login
    private final CashierLoginView aCashierLoginView;
    // Vue pour la caisse
    private final CashierView aCashierView;
    
    // Client object
    private final ClientHelper aClientHelper;
    
    public ControllerCashier(CashierLoginView cashierLoginView, CashierView cashierView,CashierHelper cashierHelper, ClientHelper clientHelper)
    {
        // gérer un caissier
        this.aCashierHandler = cashierHelper;
        // gérer un client
        this.aClientHelper = clientHelper;
        
        // Initialisation des deux vues
        this.aCashierLoginView = cashierLoginView;
        this.aCashierView = cashierView;
    }
    
    /**
     * Utiliser pour identifier le caissier
     * @param login
     * @param password 
     */
    public void login(String login, String password)
    {
        if(this.aCashierHandler.connect(login, password))
        {
            // set visible
            this.aCashierLoginView.setVisible(false);
            this.aCashierView.setVisible(true);
        }
    }
    
    public void initTransaction()
    {
        this.aCashierHandler.startTransaction("type");

    }
    
    /**
     * Scan all object
     */
    public void scan()
    {
        this.aCashierHandler.addArticles(this.aClientHelper);
    }
    
    public void paye()
    {
        this.aCashierHandler.pay("cb");
    }
    
    public void rembourser()
    {
        //TODO
    }

    /**
     * Utiliser pour gérer les actions provenant de l'interface de connexion et de scan des produits
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        if(this.aCashierLoginView.getButtonIdentification()== ae.getSource())
        {
            // Bouton identification activé
            String login = this.aCashierLoginView.getLogin().getText();
            String password = this.aCashierLoginView.getLogin().getText();
            
            // tentative de connection
            System.err.println("tentative de connection");
            this.login(login, password);
            this.initTransaction();
        }
        else if(this.aCashierView.getScanButton() == ae.getSource())
        {
            System.err.println("Scan !");
            this.scan();
        }
        else if(this.aCashierView.getDisconnect() == ae.getSource())
        {
            this.aCashierLoginView.setVisible(true);
            this.aCashierView.setVisible(false);
        }
        else if(this.aCashierView.getPayeButton() == ae.getSource())
        {
            this.paye();
        }
        else if(this.aCashierView.getRembourseButton() == ae.getSource())
        {
            this.rembourser();
        }
        else
        {
            
        }
    }
}
