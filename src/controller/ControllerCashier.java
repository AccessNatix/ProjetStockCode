package controller;

import controller.jpacontroller.ClientJpaController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.ClientArticles;
import modele.entity.handler.CashierHelper;
import modele.entity.handler.ClientHelper;
import vue.CashierLoginView;
import vue.CashierView;

/**
 * Cette classe est la classe utilisé pour permettre le login
 * 
 * @author anatole
 */
public class ControllerCashier extends Observable implements Observer, ActionListener, ChangeListener{
    
    // Modèle
    private final CashierHelper aCashierHandler;
    
    // Vue pour le login
    private final CashierLoginView aCashierLoginView;
    // Vue pour la caisse
    private final CashierView aCashierView;
    
    // Client object
    private final ClientHelper aClientHelperBuy;
    private final ClientHelper aClientHelperRefund;    
    
    private double aTotalPrice;
    
    public ControllerCashier(CashierLoginView cashierLoginView, CashierView cashierView,CashierHelper cashierHelper, ClientHelper clientHelperBuy, ClientHelper clientHelperRefund)
    {
        // gérer un caissier
        this.aCashierHandler = cashierHelper;
        // gérer un client
        this.aClientHelperBuy = clientHelperBuy;
        this.aClientHelperRefund = clientHelperRefund;
        
        // Initialisation des deux vues
        this.aCashierLoginView = cashierLoginView;
        this.aCashierView = cashierView;
        
        this.aTotalPrice = 0.0;
    }
    
    /**
     * Utiliser pour identifier le caissier
     * @param login
     * @param password 
     */
    public boolean login(String login, String password)
    {
        if(this.aCashierHandler.connect(login, password))
        {
            // set visible
            this.aCashierLoginView.setVisible(false);
            this.aCashierView.setVisible(true);
            
            return true;
        }
        
        return false;
    }
    
    public void initTransactionPaye()
    {
        this.aCashierHandler.startTransaction("paye");
        this.aTotalPrice = 0.0;
    }
    
    public void initTransactionRefund()
    {
        this.aCashierHandler.startTransaction("refund");
        this.aTotalPrice = 0.0;
    }
    
    /**
     * Scan for paying
     */
    public void scanPaye()
    {
        for(ClientArticles clientArticle : this.aClientHelperBuy.getArticles())
        {
            double value = Double.valueOf(clientArticle.getQuantity()*clientArticle.getArticleId().getPrice().doubleValue());
            this.aTotalPrice += value;
        }
                        
        this.aCashierHandler.addArticles(this.aClientHelperBuy, true);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Scan for refunding
     */
    public void scanRefund()
    {
        for(ClientArticles clientArticle : this.aClientHelperRefund.getArticles())
        {
            double value = Double.valueOf(clientArticle.getQuantity()*clientArticle.getArticleId().getPrice().doubleValue());
            this.aTotalPrice += value;
        }        
        
        this.aCashierHandler.addArticles(this.aClientHelperRefund, false);
        
        this.setChanged();
        this.notifyObservers();        
    }    

    public void cancelTransaction()
    {
        this.aCashierHandler.cancelTranslation();
        this.aCashierView.cleanInterface();
    }
    
    public void paye()
    {
        this.aCashierHandler.pay(aClientHelperBuy, "cb");
    }
    
    public void refund()
    {
        this.aCashierHandler.refund(aClientHelperRefund);
        this.aCashierView.cleanInterface();
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
            String password = this.aCashierLoginView.getPassword().getText();
            
            if(this.login(login, password))
            {
                this.initTransactionPaye();
            }
        }
        else if(this.aCashierView.getScanPayeButton()== ae.getSource())
        {
            this.scanPaye();
        }
        else if(this.aCashierView.getScanRefundButton()== ae.getSource())
        {
            this.scanRefund();
        }        
        else if(this.aCashierView.getDisconnect() == ae.getSource())
        {
            this.aCashierLoginView.setVisible(true);
            this.aCashierView.setVisible(false);
        }
        else if(this.aCashierView.getPayeButton() == ae.getSource())
        {
            this.paye();
            this.aCashierView.cleanInterface();
        }
        else if(this.aCashierView.getRefundButton() == ae.getSource())
        {
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
                this.cancelTransaction();
                this.initTransactionPaye();
                break;
            case "Rembourser":
                this.cancelTransaction();
                this.initTransactionRefund();
                break;
        }
    }

    @Override
    public void update(Observable o, Object o1) {
    }
    
}
