package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modele.entity.handler.CashierHelper;
import modele.entity.handler.ClientHelper;
import vue.CashierLoginView;
import vue.CashierView;

/**
 * Cette classe est la classe utilisé pour permettre le login
 * 
 * @author anatole
 */
public class ControllerCashier implements ActionListener, ChangeListener{
    
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
    
    public void initTransactionPaye()
    {
        this.aCashierHandler.startTransaction("paye");
    }
    
    public void initTransactionRefund()
    {
        this.aCashierHandler.startTransaction("refund");
    }
    
    /**
     * Scan for paying
     */
    public void scanPaye()
    {
        this.aCashierHandler.addArticles(this.aClientHelper);
    }
    
    /**
     * Scan for refunding
     */
    public void scanRefund()
    {
        this.aCashierHandler.addArticles(this.aClientHelper);
    }    

    public void cancelTransaction()
    {
        this.aCashierHandler.cancelTranslation();
    }
    
    public void paye()
    {
        this.aCashierHandler.pay("cb");
    }
    
    public void refund()
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
            this.initTransactionPaye();
        }
        else if(this.aCashierView.getScanPayeButton()== ae.getSource())
        {
            System.err.println("Scan paye !");
            this.scanPaye();
        }
        else if(this.aCashierView.getScanRefundButton()== ae.getSource())
        {
            System.err.println("Scan refund !");
            this.scanRefund();
        }        
        else if(this.aCashierView.getDisconnect() == ae.getSource())
        {
            System.err.println("Disconnect");
            this.aCashierLoginView.setVisible(true);
            this.aCashierView.setVisible(false);
        }
        else if(this.aCashierView.getPayeButton() == ae.getSource())
        {
            System.err.println("Payer !");
            this.paye();
            this.aCashierView.cleanInterface();
        }
        else if(this.aCashierView.getRefundButton() == ae.getSource())
        {
            System.err.println("Rembourser");
            this.refund();
            this.aCashierView.cleanInterface();
        }
        else
        {
            
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        switch(sourceTabbedPane.getTitleAt(index))
        {
            case "Achat":
                System.err.println("Achat");
                this.cancelTransaction();
                this.initTransactionPaye();
                break;
            case "Rembourser":
                System.err.println("Rembourser");
                this.cancelTransaction();
                this.initTransactionRefund();
                break;
        }
    }
    
}
