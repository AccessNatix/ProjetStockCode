/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.factory;

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
import java.math.BigDecimal;
import java.util.HashMap;
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


/**
 *
 * @author yo
 */
public class EntityFactory {
    public static <T> T create(T t, HashMap<String,Object> attributes){
        if(t.getClass() == AisleArticles.class){
            Article article = (Article) attributes.get("article");
            int quantity = (Integer) attributes.get("quantity");
            
            AisleArticles aisleArticle = new AisleArticles();
            aisleArticle.setArticleId(article);
            aisleArticle.setQuantity(quantity);

            try {
                AisleArticlesJpaController.getController().create(aisleArticle);
                t = (T) aisleArticle;
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(EntityFactory.class.getName()).log(Level.SEVERE, null, ex);
                t = null;
            }
            
        }
        else if(t.getClass() == Article.class){
            Provider provider = (Provider) attributes.get("provider");
            String barcode = (String) attributes.get("barcode");
            String type = (String) attributes.get("type");
            String name = (String) attributes.get("name");
            BigDecimal price = (BigDecimal) attributes.get("price");
            int stock = (Integer) attributes.get("stock");
            int threshold = (Integer) attributes.get("threshold");
            
            Article article = new Article();
            article.setBarcode(barcode);
            article.setName(name);
            article.setPrice(price);
            article.setStock(stock);
            article.setThreshold(threshold);
            article.setType(type);
            article.setProviderId(provider);

            ArticleJpaController.getController().create(article);
            t = (T) article;
        }
        else if(t.getClass() == CashRegister.class){
            CashRegister cashRegister = new CashRegister();
            cashRegister.setTruc(0);

            CashRegisterJpaController.getController().create(cashRegister);
            
            t = (T) cashRegister;
        }
        else if(t.getClass() == Cashier.class){
            
            String fistname = (String) attributes.get("firstname");
            String lastname = (String) attributes.get("lastname");
            
            Employee employee = new Employee();
            employee.setFirstname(fistname);
            employee.setLastname(lastname);

            EmployeeJpaController.getController().create(employee);

            Cashier cashier = new Cashier();
            cashier.setEmployeeId(employee);

            CashierJpaController.getController().create(cashier);
            
            t = (T) cashier;
        }
        else if(t.getClass() == Client.class){
            String fistname = (String) attributes.get("firstname");
            String lastname = (String) attributes.get("lastname");
            
            Client client = new Client();
            client.setFirstname(fistname);
            client.setLastname(lastname);

            ClientJpaController.getController().create(client);
            
            t = (T) client;
        }
        else if(t.getClass() == ClientArticles.class){
            Article article = (Article) attributes.get("article");
            Client client = (Client) attributes.get("client");
            int quantity = (Integer) attributes.get("quantity");
            if(SystemStock.getSystemStock().removeArticleFromAisle(article, quantity) == false) return null;
        
            ClientArticles clientArticle = new ClientArticles();
            clientArticle.setArticleId(article);
            clientArticle.setClientId(client);
            clientArticle.setQuantity(quantity);

            ClientArticlesJpaController.getController().create(clientArticle);
            
            t = (T) clientArticle;
        }
        else if(t.getClass() == ClientArticlesReturn.class){
            Article article = (Article) attributes.get("article");
            Client client = (Client) attributes.get("client");
            int quantity = (Integer) attributes.get("quantity");
           
            ClientArticlesReturn clientArticlesReturn = new ClientArticlesReturn();
            clientArticlesReturn.setArticleId(article);
            clientArticlesReturn.setClientId(client);
            clientArticlesReturn.setQuantity(quantity);

            ClientArticlesReturnJpaController.getController().create(clientArticlesReturn);
            
            t = (T) clientArticlesReturn;
        }
        else if(t.getClass() == Command.class){
            Provider provider = (Provider) attributes.get("provider");
            Command command = new Command();
            command.setProviderId(provider);

            CommandJpaController.getController().create(command);
            
            t = (T) command;
        }
        else if(t.getClass() == CommandedArticles.class){
            Article article = (Article) attributes.get("article");
            Command command = (Command) attributes.get("command");
            int quantity = (Integer) attributes.get("quantity");
            
            CommandedArticles commandedArticles = new CommandedArticles();
            commandedArticles.setArticleId(article);
            commandedArticles.setCommandId(command);
            commandedArticles.setQuantity(quantity);

            CommandedArticlesJpaController.getController().create(commandedArticles);
            
            t = (T) commandedArticles;
        }
        else if(t.getClass() == Key.class){
            CashRegister cashRegister = (CashRegister) attributes.get("cashRegister");
            Key key = new Key();
            key.setCashRegisterid(cashRegister);

            KeyJpaController.getController().create(key);
            
            t = (T) key;
        }
        else if(t.getClass() == Payment.class){
            String type = (String) attributes.get("type");
            BigDecimal price = (BigDecimal) attributes.get("price");
            Transaction transaction = (Transaction) attributes.get("transaction");
            
            Payment payment = new Payment();
            payment.setType(type);
            payment.setValue(price);
            payment.setTransactionId(transaction);

            PaymentJpaController.getController().create(payment);
            
            t = (T) payment;
        }
        else if(t.getClass() == Provider.class){
            String adress = (String) attributes.get("adress");
            String name = (String) attributes.get("name");
            String phone = (String) attributes.get("phone");
            String postalCode = (String) attributes.get("postalCode");
            
            Provider provider = new Provider();
            provider.setAdress(adress);
            provider.setName(name);
            provider.setPhone(phone);
            provider.setPostalCode(postalCode);

            ProviderJpaController.getController().create(provider);
            t = (T) provider;
        }
        else if(t.getClass() == Retailer.class){
            
            String fistname = (String) attributes.get("firstname");
            String lastname = (String) attributes.get("lastname");
            
            Employee employee = new Employee();
            employee.setFirstname(fistname);
            employee.setLastname(lastname);

            EmployeeJpaController.getController().create(employee);

            Retailer retailer = new Retailer();
            retailer.setEmployeeId(employee);

            RetailerJpaController.getController().create(retailer);
            
            t = (T) retailer;
        }
        else if(t.getClass() == RetailerCommands.class){
            Command command = (Command) attributes.get("command");
            Retailer retailer = (Retailer) attributes.get("retailer");
            
            RetailerCommands retailerCommand = new RetailerCommands();
            retailerCommand.setCommandId(command);
            retailerCommand.setRetailerId(retailer);

            RetailerCommandsJpaController.getController().create(retailerCommand);
            
            t = (T) retailerCommand;
        }
        else if(t.getClass() == Session.class){
            String password = (String) attributes.get("password");
            String pseudo = (String) attributes.get("pseudo");
            
            Session session = new Session();
            session.setPassword(password);
            session.setPseudo(pseudo);

            SessionJpaController.getController().create(session);
            
        }
        else if(t.getClass() == SessionTransactions.class){
            Session session = (Session) attributes.get("session");
            Transaction transaction = (Transaction) attributes.get("transaction");
                
            SessionTransactions sessionCommand = new SessionTransactions();
            sessionCommand.setSessionId(session);
            sessionCommand.setTransactionId(transaction);

            SessionTransactionsJpaController.getController().create(sessionCommand);
            
            t = (T) sessionCommand;
        }
        else if(t.getClass() == Transaction.class){
            String type = (String) attributes.get("type");
            
            Transaction transaction = new Transaction();
            transaction.setType(type);

            TransactionJpaController.getController().create(transaction);
            
            t = (T) transaction;
        }
        else if(t.getClass() == TransactionArticles.class){
            
            Article article = (Article) attributes.get("article");
            int quantity = (Integer) attributes.get("quantity");
            Transaction transaction = (Transaction) attributes.get("transaction");
            
            TransactionArticles transactionArticles = new TransactionArticles();
            transactionArticles.setArticleId(article);
            transactionArticles.setQuantity(quantity);
            transactionArticles.setTransactionId(transaction);

            TransactionArticlesJpaController.getController().create(transactionArticles);
            
            t = (T) transactionArticles;
        }
        else if(t.getClass() == WarehouseArticles.class){
            Article article = (Article) attributes.get("article");
            int quantity = (Integer) attributes.get("quantity");
            
            WarehouseArticles warehouseArticle = new WarehouseArticles();
            warehouseArticle.setArticleId(article);
            warehouseArticle.setQuantity(quantity);

            try {
                WarehouseArticlesJpaController.getController().create(warehouseArticle);
                t = (T) warehouseArticle;
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(EntityFactory.class.getName()).log(Level.SEVERE, null, ex);
                t = null;
            }
            
        }
        else{
            t = null;
        }
        return t;
    }
}
