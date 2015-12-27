/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.jpacontroller.ArticleJpaController;
import controller.jpacontroller.CommandJpaController;
import controller.jpacontroller.ProviderJpaController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modele.entity.Article;
import modele.entity.Command;
import modele.entity.Provider;
import modele.entity.handler.RetailerHelper;
import vue.RetaillerLoginView;
import vue.RetaillerView;

/**
 *
 * @author anatole
 */
public class ControllerRetailler implements ActionListener, ChangeListener{

    // Modèle
    private final RetailerHelper aRetaillerHandler;
    
    // Vue pour le login
    private final RetaillerLoginView aRetaillerLoginView;
    // Vue pour la caisse
    private final RetaillerView aRetaillerView;
    
    public ControllerRetailler(RetailerHelper retaillerHandler, RetaillerLoginView retaillerLoginView, RetaillerView retaillerView)
    {
        this.aRetaillerHandler = retaillerHandler;
        this.aRetaillerLoginView = retaillerLoginView;
        this.aRetaillerView = retaillerView;
    }
    
    /**
     * Utiliser pour identifier le caissier
     * @param login
     * @param password 
     */
    public void login()
    {
        this.aRetaillerLoginView.setVisible(false);
        this.aRetaillerView.setVisible(true);
        
        this.showStock();
    }
    
    /**
     * Afficher le stock
     */
    public void showStock()
    {
        this.aRetaillerHandler.printStock();
    }
    
    public void updateStock(String id)
    {
        Command command = CommandJpaController.getController().findCommand(Integer.valueOf(id));
        
        System.err.println(command.getId());
        this.aRetaillerHandler.handleCommand(command);
    }
    
    /**
     * Afficher le stock sous seuil
     */
    public void showStockUnderThreshold()
    {
        this.aRetaillerHandler.printInsufficientArticle();
        this.aRetaillerHandler.getProvider();
    }
    
    public void showProviders()
    {
        this.aRetaillerHandler.getProvider();
    }
    
    /**
     * Afficher commande
     */
    public void showCommands()
    {
        this.aRetaillerHandler.printCommands();
    }
    
    /**
     * Cette fonction est utilisé pour faire une commande
     * @param nameArticle
     * @param quantity
     * @param nameProvider 
     */
    public void doCommand(final String nameArticle, int quantity, final String nameProvider)
    {
        Article article = ArticleJpaController.getController().findByName(nameArticle);
        Provider provider = ProviderJpaController.getController().findByName(nameProvider);
        HashMap<Article,Integer> map = new HashMap<>();
        map.put(article, Integer.valueOf(quantity));
        // TODO
        this.aRetaillerHandler.orderArticle(map, provider);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if(ae.getSource() == this.aRetaillerLoginView.getIdentification())
        {
            this.login();
        }
        else if(ae.getSource() == this.aRetaillerView.getCommandButton())
        {
            String nameArticle = this.aRetaillerView.getArticleName().getText();
            int quantiy = Integer.valueOf(this.aRetaillerView.getQuantity().getText());
            String provider = (String)this.aRetaillerView.getProviderInput().getText();
            
            this.doCommand(nameArticle, quantiy, provider);
        }
        else if(ae.getSource() == this.aRetaillerView.getUpdateButton())
        {
            String tmp = (String)this.aRetaillerView.getCommands().getValueAt(this.aRetaillerView.getCommands().getSelectedRow(), 0);
            this.updateStock(tmp);
            this.aRetaillerView.cleanCommands();
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        switch(sourceTabbedPane.getTitleAt(index))
        {
            case "affichage stock":
                System.err.println("Stock");
                this.showStock();
                break;
            case "affichage stock sous seuil":
                System.err.println("Sous seuil");
                this.showStockUnderThreshold();
                break;
            case "Affichage commande":
                System.err.println("Command");
                this.showCommands();
                break;
            case "Afficher fournisseur":
                System.err.println("Fournisseur");
                this.showProviders();
                break;
        }
    }
}
