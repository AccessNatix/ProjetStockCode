/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.CommandJpaController;
import controller.jpacontroller.CommandedArticlesJpaController;
import controller.jpacontroller.ProviderJpaController;
import controller.jpacontroller.exceptions.NonexistentEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.Command;
import modele.entity.CommandedArticles;
import modele.entity.Provider;
import modele.entity.Retailer;
import modele.entity.factory.EntityFactory;

/**
 *
 * @author yo
 */
public class RetailerHelper extends Observable{
    
    private Retailer aRetailer;
    
    public RetailerHelper(Retailer retailer){
        this.aRetailer = retailer;
    }
    
    public Command orderArticle(HashMap<Article,Integer> map, Provider provider){
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("provider", provider);
        Command command = EntityFactory.create(new Command(), map2);
        if(command == null) return null;
        HashMap<String,Object> attibutes = new HashMap<>();
        attibutes.put("command", command);
        for(Article article : map.keySet()){
            attibutes.put("article", article);
            attibutes.put("quantity", map.get(article));
            CommandedArticles commandedArticles = EntityFactory.create(new CommandedArticles(), attibutes);
        }
        return command;
    }
    
    public boolean handleCommand(Command command){
        if(command.getDealt() == 0)
        {
            // ok
            command.setDealt();
            try {
                CommandJpaController.getController().edit(command);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(RetailerHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RetailerHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

            List<CommandedArticles> list = CommandedArticlesJpaController.getController().findByCommandId(command.getId());
            for(CommandedArticles commandedArticles : list){
                SystemStock.getSystemStock().addArticleToWarehouse(commandedArticles.getArticleId(), commandedArticles.getQuantity());
            }            
        }

        return true;
    }
    
    public List<CommandedArticles> printCommands()
    {
        List<CommandedArticles> tmp = CommandedArticlesJpaController.getController().findCommandedArticlesEntities(); 
        
        this.setChanged();
        this.notifyObservers(tmp);
        
        return tmp;
    }
    
    public List<CommandedArticles> displayCommand(Command command){
        List<CommandedArticles> tmp = CommandedArticlesJpaController.getController().findByCommandId(command.getId()); 

        this.setChanged();
        this.notifyObservers(tmp);
        
        return tmp;
    }
    
    /**
     * Récupérer la liste des providers
     * @return 
     */
    public List<Provider> getProvider()
    {
        List<Provider> provider = ProviderJpaController.getController().findProviderEntities();
        
        this.setChanged();
        this.notifyObservers(provider);
        
        return provider;
    }
    
    public HashMap<Article,Integer> printStock(){
        HashMap<Article, Integer> tmp = SystemStock.getSystemStock().getStock();
        
        this.setChanged();
        this.notifyObservers(tmp);
        
        return tmp;
    }
    
    public HashMap<Article,Integer> printInsufficientArticle(){
        HashMap<Article, Integer> tmp = SystemStock.getSystemStock().checkStock();
        System.err.println(tmp.size());
        
        this.setChanged();
        this.notifyObservers(tmp);
        
        return tmp;
    }
}
