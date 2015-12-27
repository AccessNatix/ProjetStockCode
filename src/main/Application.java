/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.ControllerCashier;
import controller.ControllerRetailler;
import controller.jpacontroller.AisleArticlesJpaController;
import controller.jpacontroller.ArticleJpaController;
import controller.jpacontroller.CashRegisterJpaController;
import controller.jpacontroller.CashierJpaController;
import controller.jpacontroller.ClientArticlesJpaController;
import controller.jpacontroller.ClientJpaController;
import controller.jpacontroller.CommandJpaController;
import controller.jpacontroller.CommandedArticlesJpaController;
import controller.jpacontroller.EmployeeJpaController;
import controller.jpacontroller.KeyJpaController;
import controller.jpacontroller.PaymentJpaController;
import controller.jpacontroller.ProviderJpaController;
import controller.jpacontroller.RetailerCommandsJpaController;
import controller.jpacontroller.RetailerJpaController;
import controller.jpacontroller.SessionJpaController;
import controller.jpacontroller.SessionTransactionsJpaController;
import controller.jpacontroller.TransactionArticlesJpaController;
import controller.jpacontroller.TransactionJpaController;
import controller.jpacontroller.WarehouseArticlesJpaController;
import controller.jpacontroller.exceptions.IllegalOrphanException;
import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.SystemStock;
import modele.entity.AisleArticles;
import modele.entity.Article;
import modele.entity.CashRegister;
import modele.entity.Cashier;
import modele.entity.Client;
import modele.entity.ClientArticles;
import modele.entity.Command;
import modele.entity.CommandedArticles;
import modele.entity.Employee;
import modele.entity.Key;
import modele.entity.Payment;
import modele.entity.Provider;
import modele.entity.Retailer;
import modele.entity.RetailerCommands;
import modele.entity.Session;
import modele.entity.SessionTransactions;
import modele.entity.Transaction;
import modele.entity.TransactionArticles;
import modele.entity.WarehouseArticles;
import modele.entity.factory.ObjectFactory;
import modele.entity.handler.CashRegisterHelper;
import modele.entity.handler.CashierHelper;
import modele.entity.handler.ClientHelper;
import modele.entity.handler.RetailerHelper;
import vue.CashierLoginView;
import vue.CashierView;
import vue.RetaillerLoginView;
import vue.RetaillerView;

/**
 *
 * @author yo
 */
public class Application {
    
    /**
     * Pour la caisse
     */
    // Modèle
    private CashierHelper aCashierHelper;
    private ClientHelper aClientHelper;
    
    // Vue
    private CashierLoginView aCashierLoginView;
    private CashierView aCashierView;
    
    // Controleur
    private ControllerCashier aControllerCashier;
    
    /**
     * Pour le détaillant
     */
    // Modèle
    private RetailerHelper aRetailerHelper;
    
    // Vue
    private RetaillerView aRetailerView;
    private RetaillerLoginView aRetailerLoginView;
    
    // Contrôleur
    private ControllerRetailler aControllerRetailer;
    
    public Application()
    {        
        this.clearBDD();
        this.initArticle();
        this.initApp();
        this.startUp();
    }
    
    private void clearBDD(){
        AisleArticlesJpaController aisleArticlesJpaController = AisleArticlesJpaController.getController();
        ArticleJpaController articleJpaController = ArticleJpaController.getController();
        CashRegisterJpaController cashRegisterJpaController = CashRegisterJpaController.getController();
        CashierJpaController cashierJpaController = CashierJpaController.getController();
        ClientArticlesJpaController clientArticlesJpaController = ClientArticlesJpaController.getController();
        ClientJpaController clientJpaController = ClientJpaController.getController();
        CommandJpaController commandJpaController = CommandJpaController.getController();
        CommandedArticlesJpaController commandedArticlesJpaController = CommandedArticlesJpaController.getController();
        EmployeeJpaController employeeJpaController = EmployeeJpaController.getController();
        KeyJpaController keyJpaController = KeyJpaController.getController();
        PaymentJpaController paymentJpaController = PaymentJpaController.getController();
        ProviderJpaController providerJpaController = ProviderJpaController.getController();
        RetailerCommandsJpaController retailerCommandsJpaController = RetailerCommandsJpaController.getController();
        RetailerJpaController retailerJpaController = RetailerJpaController.getController();
        SessionJpaController sessionJpaController = SessionJpaController.getController();
        SessionTransactionsJpaController sessionTransactionsJpaController = SessionTransactionsJpaController.getController();
        TransactionArticlesJpaController transactionArticlesJpaController = TransactionArticlesJpaController.getController();
        TransactionJpaController transactionJpaController = TransactionJpaController.getController();
        WarehouseArticlesJpaController warehouseArticlesJpaController = WarehouseArticlesJpaController.getController();
        
        for(AisleArticles aa : aisleArticlesJpaController.findAisleArticlesEntities()) try {
            aisleArticlesJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(ClientArticles aa : clientArticlesJpaController.findClientArticlesEntities()) try {
            clientArticlesJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(CommandedArticles aa : commandedArticlesJpaController.findCommandedArticlesEntities()) try {
            commandedArticlesJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(TransactionArticles aa : transactionArticlesJpaController.findTransactionArticlesEntities()) try {
            transactionArticlesJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(WarehouseArticles aa : warehouseArticlesJpaController.findWarehouseArticlesEntities()) try {
            warehouseArticlesJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
        
        for(RetailerCommands aa : retailerCommandsJpaController.findRetailerCommandsEntities()) try {
            retailerCommandsJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(SessionTransactions aa : sessionTransactionsJpaController.findSessionTransactionsEntities()) try {
            sessionTransactionsJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
        for(Cashier aa : cashierJpaController.findCashierEntities()) try {
            cashierJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }    
        for(Retailer aa : retailerJpaController.findRetailerEntities()) try {
            try {
                retailerJpaController.destroy(aa.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Employee aa : employeeJpaController.findEmployeeEntities()) try {
            try {
                employeeJpaController.destroy(aa.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Command aa : commandJpaController.findCommandEntities()) try {
            try {
                commandJpaController.destroy(aa.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Payment aa : paymentJpaController.findPaymentEntities()) try {
            paymentJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Transaction aa : transactionJpaController.findTransactionEntities()) try {
            transactionJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Session aa : sessionJpaController.findSessionEntities()) try {
            sessionJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Key aa : keyJpaController.findKeyEntities()) try {
            keyJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(CashRegister aa : cashRegisterJpaController.findCashRegisterEntities()) try {
            try {
                cashRegisterJpaController.destroy(aa.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Client aa : clientJpaController.findClientEntities()) try {
            try {
                clientJpaController.destroy(aa.getId());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Article aa : articleJpaController.findArticleEntities()) try {
            articleJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException | IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }        
        for(Provider aa : providerJpaController.findProviderEntities()) try {
            providerJpaController.destroy(aa.getId());
            } catch (NonexistentEntityException | IllegalOrphanException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    private void initArticle()
    {
        Provider providerFirst = ObjectFactory.createProvider("adress", "jeanpaul", "0888", "92000");
        Provider providerSecond = ObjectFactory.createProvider("adress", "pierre", "082", "23000");
        
        Article chaussure = ObjectFactory.createArticle(providerFirst, "barcode", "type", "chaussure", new BigDecimal(10.4), 60, 70);
        Article lait = ObjectFactory.createArticle(providerSecond, "barcode", "type", "lait", new BigDecimal(3.0), 50, 10);
        Article chocolat = ObjectFactory.createArticle(providerSecond, "barcode", "type", "chocolat", new BigDecimal(4.0), 30, 5);
    
        /**
         * Ajout des articles à l'entrepot
         */
        SystemStock.getSystemStock().addArticleToWarehouse(chaussure, 60);
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
            this.aClientHelper.addArticle(article.getKey(), 1, true);
        }
        
        this.aCashierLoginView = new CashierLoginView();
        this.aCashierView = new CashierView();
        
        // Controller
        this.aControllerCashier = new ControllerCashier(aCashierLoginView, aCashierView, aCashierHelper, aClientHelper);
        
        // set up action
        this.aCashierLoginView.addController(this.aControllerCashier);
        this.aCashierView.addController(aControllerCashier,aControllerCashier);
        
        // la vue observe le cashierHelper
        this.aCashierHelper.addObserver(this.aCashierView);
       
        /**
         * Partie pour le détaillant
         */
        
        Retailer retailer = ObjectFactory.createRetailer("jean", "luc");
        this.aRetailerHelper = new RetailerHelper(retailer);
        
        this.aRetailerView = new RetaillerView();
        this.aRetailerLoginView = new RetaillerLoginView();
        
        this.aControllerRetailer = new ControllerRetailler(aRetailerHelper, aRetailerLoginView, aRetailerView);
    
        // ajouter le contrôleur
        this.aRetailerLoginView.addController(this.aControllerRetailer);
        this.aRetailerView.addController(this.aControllerRetailer,this.aControllerRetailer);
        
        this.aRetailerHelper.addObserver(this.aRetailerView);
    }
        
    private void startUp()
    {  
        // init view
        this.aCashierView.setVisible(false);
        this.aCashierLoginView.setVisible(true);
        
        this.aRetailerView.setVisible(false);
        this.aRetailerLoginView.setVisible(true);
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