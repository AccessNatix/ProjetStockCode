/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.ControllerCashier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map.Entry;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.CashRegister;
import modele.entity.Cashier;
import modele.entity.Client;
import modele.entity.Provider;
import modele.entity.factory.ObjectFactory;
import modele.entity.handler.CashRegisterHelper;
import modele.entity.handler.CashierHelper;
import modele.entity.handler.ClientHelper;
import vue.CashierLoginView;
import vue.CashierView;

/**
 *
 * @author yo
 */
public class Application {
    
    // Modèle
    private CashierHelper aCashierHelper;
    private ClientHelper aClientHelper;
    
    // Vue
    private CashierLoginView aCashierLoginView;
    private CashierView aCashierView;
    
    // Controleur
    private ControllerCashier aControllerCashier;
    
    // gestionnaire de stock
    
    public Application()
    {        
        this.initArticle();
        this.initApp();
        this.startUp();
    }
    
    private void initArticle()
    {
        Provider providerFirst = ObjectFactory.createProvider("adress", "jeanpaul", "0888", "92000");
        Provider providerSecond = ObjectFactory.createProvider("adress", "pierre", "082", "23000");
        
        Article chaussure = ObjectFactory.createArticle(providerFirst, "barcode", "type", "chaussure", new BigDecimal(10.10), 100, 30);
        Article lait = ObjectFactory.createArticle(providerSecond, "barcode", "type", "lait", new BigDecimal(3.0), 50, 10);
        Article chocolat = ObjectFactory.createArticle(providerSecond, "barcode", "type", "chocolat", new BigDecimal(4.0), 30, 5);
    
        /**
         * Ajout des articles à l'entrepot
         */
        SystemStock.getSystemStock().addArticleToWarehouse(chaussure, 100);
        SystemStock.getSystemStock().addArticleToWarehouse(lait, 50);
        SystemStock.getSystemStock().addArticleToWarehouse(chocolat, 30);
        
        /**
         * restockage des rayons
         */
        SystemStock.getSystemStock().restock(chaussure, 50);
        SystemStock.getSystemStock().restock(lait, 20);
        SystemStock.getSystemStock().restock(chocolat, 10);        
    }
    
    private void initApp()
    {
        // initialiser une session
        ObjectFactory.createSession("plop", "plop");
        
        /**
         * Création de la classe d'intéraction avec le caissier
         * C'est le modèle
         */
        Cashier cashier = ObjectFactory.createCashier("bob", "lebricoleur");
        CashRegister cashRegister = ObjectFactory.createCashRegister();
        CashRegisterHelper cashHelper = new CashRegisterHelper(cashRegister);
        this.aCashierHelper = new CashierHelper(cashier, cashHelper);
        
        HashMap<Article, Integer> articles = SystemStock.getSystemStock().getStockInAisle();
        
        /**
         * Création du client et du helper
         */
        Client client = ObjectFactory.createClient("asterix", "legollois");
        this.aClientHelper = new ClientHelper(client);
        
        for(Entry<Article, Integer> article : articles.entrySet())
        {
            this.aClientHelper.addArticle(article.getKey(), 1);
        }
        
        this.aCashierLoginView = new CashierLoginView();
        this.aCashierView = new CashierView();
        
        // Controller
        this.aControllerCashier = new ControllerCashier(aCashierLoginView, aCashierView, aCashierHelper, aClientHelper);
        
        // set up action
        this.aCashierLoginView.addController(this.aControllerCashier);
        this.aCashierView.addController(aControllerCashier);
    }
        
    private void startUp()
    {  
        // init view
        this.aCashierView.setVisible(false);
        this.aCashierLoginView.setVisible(true);
    }
    
    public static void main(String[] argv){
        /**
         * Set up look and feel
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Application app = new Application();
    }

}