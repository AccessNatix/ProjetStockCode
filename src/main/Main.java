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
    
}