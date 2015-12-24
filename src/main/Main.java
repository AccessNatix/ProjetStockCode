/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

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
import java.math.BigDecimal;
import java.util.List;
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

/**
 *
 * @author yo
 */
public class Main {
    
    public static void main(String[] argv){
        /*Client client = new Client();
        client.setFirstname("fistname");
        client.setLastname("lastname");
        ClientJpaController.getController().create(client);*/
        Provider p1 = createProvider("1");
        
        Client client = createClient();
        
        Article a1 = createArticle("1", p1);
        Article a2 = createArticle("2", p1);
        
        ClientArticles ca1 = createClientArticles(client, a1, 2);
        ClientArticles ca2 = createClientArticles(client, a2, 1);
        
        List<ClientArticles> clientArticles = ClientArticlesJpaController.getController().findByClientId(client.getId());
        
        Cashier cashier = createCashier();
        
        CashRegister cashRegister = createCashRegister();
        
        Key key = createKey(cashRegister);
        
        
    }
    
    public static Provider createProvider(String name){
        Provider provider = new Provider();
        provider.setAdress("adress");
        provider.setName(name);
        provider.setPhone("phone");
        provider.setPostalCode("postalCode");
        
        ProviderJpaController.getController().create(provider);
        return provider;
    }
    
    public static Article createArticle(String name, Provider provider){
        Article article = new Article();
        article.setBarcode("barcode");
        article.setName(name);
        article.setPrice(new BigDecimal(15.99));
        article.setStock(5);
        article.setThreshold(2);
        article.setType("unit");
        article.setProviderId(provider);
        
        ArticleJpaController.getController().create(article);
        return article;
    }
    
    public static Client createClient(){
        Client client = new Client();
        client.setFirstname("firstname");
        client.setLastname("lastname");
        
        ClientJpaController.getController().create(client);
        return client;
    }
    
    public static ClientArticles createClientArticles(Client client, Article article, int quantity){
        
        SystemStock.getSystemStock().removeArticleFromAisle(article, quantity);
        
        ClientArticles clientArticle = new ClientArticles();
        clientArticle.setArticleId(article);
        clientArticle.setClientId(client);
        clientArticle.setQuantity(quantity);
        
        ClientArticlesJpaController.getController().create(clientArticle);
        return clientArticle;
    }
    
    public static Retailer createRetailer(){
        Employee employee = new Employee();
        employee.setFirstname("firstname");
        employee.setLastname("lastname");
        
        EmployeeJpaController.getController().create(employee);
        
        Retailer retailer = new Retailer();
        retailer.setEmployeeId(employee);
        
        RetailerJpaController.getController().create(retailer);
        return retailer;
    }
    
    public static Cashier createCashier(){
        Employee employee = new Employee();
        employee.setFirstname("firstname");
        employee.setLastname("lastname");
        
        EmployeeJpaController.getController().create(employee);
        
        Cashier cashier = new Cashier();
        cashier.setEmployeeId(employee);
        
        CashierJpaController.getController().create(cashier);
        return cashier;
    }
    
    public static Session createSession(String pseudo, String password){
        Session session = new Session();
        session.setPassword(password);
        session.setPseudo(pseudo);
        
        SessionJpaController.getController().create(session);
        return session;
    }
    
    public static CashRegister createCashRegister(){
        CashRegister cashRegister = new CashRegister();
        
        CashRegisterJpaController.getController().create(cashRegister);
        return cashRegister;
    }
    
    public static Key createKey(CashRegister cashRegister){
        Key key = new Key();
        key.setCashRegisterid(cashRegister);
        
        KeyJpaController.getController().create(key);
        return key;
    }
    
    public static Command createCommand(Provider provider){
        Command command = new Command();
        command.setProviderId(provider);
        
        CommandJpaController.getController().create(command);
        return command;
    }
    
    public static CommandedArticles createCommandedArticle(Command command, Article article, int quantity){
        CommandedArticles commandedArticles = new CommandedArticles();
        commandedArticles.setArticleId(article);
        commandedArticles.setCommandId(command);
        commandedArticles.setQuantity(quantity);
        
        CommandedArticlesJpaController.getController().create(commandedArticles);
        return commandedArticles;
    }
    
    public static RetailerCommands createRetailerCommand(Retailer retailer, Command command){
        RetailerCommands retailerCommand = new RetailerCommands();
        retailerCommand.setCommandId(command);
        retailerCommand.setRetailerId(retailer);
        
        RetailerCommandsJpaController.getController().create(retailerCommand);
        return retailerCommand;
    }
    
    public static WarehouseArticles createWarehouseArticle(Article article, int quantity){
        WarehouseArticles warehouseArticle = new WarehouseArticles();
        warehouseArticle.setArticleId(article);
        warehouseArticle.setQuantity(quantity);
        
        WarehouseArticlesJpaController.getController().create(warehouseArticle);
        return warehouseArticle;
    }
    
    public static AisleArticles createAisleArticle(Article article, int quantity){
        AisleArticles aisleArticle = new AisleArticles();
        aisleArticle.setArticleId(article);
        aisleArticle.setQuantity(quantity);
        
        AisleArticlesJpaController.getController().create(aisleArticle);
        return aisleArticle;
    }
    
    public static Transaction createTransaction(){
        Transaction transaction = new Transaction();
        
        TransactionJpaController.getController().create(transaction);
        return transaction;
    }
    
    public static Payment createPayment(Transaction transaction, String type, BigDecimal price){
        Payment payment = new Payment();
        payment.setType(type);
        payment.setValue(price);
        payment.setTransactionId(transaction);
        
        PaymentJpaController.getController().create(payment);
        return payment;
    }
    
    public static TransactionArticles createTransactionArticle(Article article, Transaction transaction, int quantity){
        TransactionArticles transactionArticles = new TransactionArticles();
        transactionArticles.setArticleId(article);
        transactionArticles.setQuantity(quantity);
        transactionArticles.setTransactionId(transaction);
        
        TransactionArticlesJpaController.getController().create(transactionArticles);
        return transactionArticles;
    }
    
    public static SessionTransactions createSessionTransaction(Session session, Transaction transaction){
        SessionTransactions sessionCommand = new SessionTransactions();
        sessionCommand.setSessionId(session);
        sessionCommand.setTransactionId(transaction);
        
        SessionTransactionsJpaController.getController().create(sessionCommand);
        return sessionCommand;
    }
    
}
