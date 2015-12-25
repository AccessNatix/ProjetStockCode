/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.math.BigDecimal;
import java.util.HashMap;
import modele.entity.Article;
import modele.entity.CashRegister;
import modele.entity.Cashier;
import modele.entity.Client;
import static modele.entity.Client_.firstname;
import static modele.entity.Client_.lastname;
import modele.entity.Command;
import modele.entity.Key;
import modele.entity.Provider;
import modele.entity.Retailer;
import modele.entity.Session;
import modele.entity.factory.EntityFactory;
import modele.entity.handler.CashRegisterHelper;
import modele.entity.handler.CashierHandler;
import modele.entity.handler.ClientHelper;
import modele.entity.handler.RetailerHelper;

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
        
        Provider provider = createProvider("adress", "name", "phone", "postalCode");
        
        Article p1 = createArticle(provider, "barcode", "type", "name", new BigDecimal(15.90), 5, 3),
            p2 = createArticle(provider, "barcode", "type", "name2", new BigDecimal(15.90), 5, 3);
        
        Client c1 = createClient("firstname", "lastname");
        
        Retailer r1 = createRetailer("firstname", "lastname");
        
        ClientHelper clientHelper = new ClientHelper(c1);
        
        RetailerHelper retailerHelper = new RetailerHelper(r1);
        
        Cashier cashier = createCashier("firstname", "lastname");
        
        CashRegister cashRegister = createCashRegister();
        
        CashRegisterHelper cashRegisterHelper = new CashRegisterHelper(cashRegister);
        
        CashierHandler cashierHandler = new CashierHandler(cashier, cashRegisterHelper);
        
        // create session to log in
        createSession("pseudo", "password");
        
        cashierHandler.connect("pseudo", "password");
        
        Key key = createKey(cashRegister);
        
        cashierHandler.open(key);
        
        clientHelper.addArticle(p1, 2);
        clientHelper.addArticle(p2, 3);
        
        cashierHandler.startTransaction("buy");
        cashierHandler.addArticles(clientHelper);
        //cashierHandler.cancelTranslation();
        cashierHandler.pay("check");
        
        Command command = retailerHelper.orderArticle(retailerHelper.printInsufficientArticle(),provider);
        
        retailerHelper.handleCommand(command);
        
        
    }
    
    public static Article createArticle(Provider provider, String barcode, String type, String name, BigDecimal price, int stock, int threshold){
        HashMap<String, Object> map = new HashMap<>();
        map.put("provider",provider);
        map.put("barcode",barcode);
        map.put("type",type);
        map.put("name",name);
        map.put("price",price);
        map.put("stock",stock);
        map.put("threshold",threshold);
        return EntityFactory.create(new Article(), map);
    }
    
    public static Client createClient(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Client(), map);
    }
    
    public static Retailer createRetailer(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Retailer(), map);
    }
    
    public static Provider createProvider(String adress, String name, String phone, String postalCode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("adress",adress);
        map.put("name",name);
        map.put("phone",phone);
        map.put("postalCode",postalCode);
        return EntityFactory.create(new Provider(), map);
    }
    
    public static Cashier createCashier(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Cashier(), map);
    }
    
    public static CashRegister createCashRegister(){
        return EntityFactory.create(new CashRegister(), null);
    }
    
    public static Session createSession(String pseudo, String password){
        HashMap<String, Object> map = new HashMap<>();
        map.put("pseudo",pseudo);
        map.put("password",password);
        return EntityFactory.create(new Session(), map);
    }
    
    public static Key createKey(CashRegister cashRegister){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cashRegister",cashRegister);
        return EntityFactory.create(new Key(), map);
    }
    
}