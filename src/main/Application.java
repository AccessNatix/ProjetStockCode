package main;

import controller.ControllerCashier;
import controller.ControllerRetailler;
import controller.jpacontroller.AisleArticlesJpaController;
import controller.jpacontroller.ArticleJpaController;
import controller.jpacontroller.CashRegisterJpaController;
import controller.jpacontroller.CashierJpaController;
import controller.jpacontroller.ClientArticlesJpaController;
import controller.jpacontroller.ClientArticlesReturnJpaController;
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
import modele.entity.ClientArticlesReturn;
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
    private ClientHelper aClientHelperBuy;
    private ClientHelper aClientHelperRefund;
    
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
        
        AisleArticlesJpaController.getController().reloveAll();
        ClientArticlesJpaController.getController().reloveAll();
        ClientArticlesReturnJpaController.getController().reloveAll();
        CommandedArticlesJpaController.getController().reloveAll();
        TransactionArticlesJpaController.getController().reloveAll();
        WarehouseArticlesJpaController.getController().reloveAll();
        RetailerCommandsJpaController.getController().reloveAll();
        CashierJpaController.getController().reloveAll();
        RetailerJpaController.getController().reloveAll();
        EmployeeJpaController.getController().reloveAll();
        CommandJpaController.getController().reloveAll();
        PaymentJpaController.getController().reloveAll();
        SessionTransactionsJpaController.getController().reloveAll();
        TransactionJpaController.getController().reloveAll();
        SessionJpaController.getController().reloveAll();
        KeyJpaController.getController().reloveAll();
        CashRegisterJpaController.getController().reloveAll();
        ClientJpaController.getController().reloveAll();
        ArticleJpaController.getController().reloveAll();
        ProviderJpaController.getController().reloveAll();
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
        Client clientBuy = ObjectFactory.createClient("asterix", "legollois");
        this.aClientHelperBuy = new ClientHelper(clientBuy);
        
        for(Entry<Article, Integer> article : articles.entrySet())
        {
            this.aClientHelperBuy.addArticle(article.getKey(), 1, true);
        }
        
        Client clientRefund = ObjectFactory.createClient("obelix", "legollois");
        this.aClientHelperRefund = new ClientHelper(clientRefund);
        
        for(Entry<Article, Integer> article : articles.entrySet())
        {
            this.aClientHelperRefund.addArticle(article.getKey(), 2, false);
        }
        
        this.aCashierLoginView = new CashierLoginView();
        this.aCashierView = new CashierView();
        
        // Controller
        this.aControllerCashier = new ControllerCashier(aCashierLoginView, aCashierView, aCashierHelper, aClientHelperBuy,aClientHelperRefund);
        
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
        
        this.aControllerCashier.addObserver(this.aControllerRetailer);
        this.aControllerRetailer.addObserver(this.aControllerCashier);
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