/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.ArticleJpaController;
import controller.jpacontroller.CommandedArticlesJpaController;
import java.util.HashMap;
import java.util.List;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.Command;
import modele.entity.CommandedArticles;
import modele.entity.Retailer;
import modele.entity.factory.EntityFactory;

/**
 *
 * @author yo
 */
public class RetailerHelper {
    
    private Retailer aRetailer;
    
    public RetailerHelper(Retailer retailer){
        this.aRetailer = retailer;
    }
    
    public boolean orderArticle(HashMap<Article,Integer> map){
        Command command = EntityFactory.create(new Command(), null);
        if(command == null) return false;
        HashMap<String,Object> attibutes = new HashMap<>();
        attibutes.put("command", command);
        for(Article article : map.keySet()){
            attibutes.put("article", article);
            attibutes.put("quantity", map.get(article));
            CommandedArticles commandedArticles = EntityFactory.create(new CommandedArticles(), attibutes);
            if(commandedArticles == null) return false;
        }
        return true;
    }
    
    public boolean handleCommand(Command command){
        List<CommandedArticles> list = CommandedArticlesJpaController.getController().findByCommandId(command.getId());
        for(CommandedArticles commandedArticles : list){
            SystemStock.getSystemStock().addArticleToWarehouse(commandedArticles.getArticleId(), commandedArticles.getQuantity());
        }
        return true;
    }
    
    public List<CommandedArticles> displayCommand(Command command){
        return CommandedArticlesJpaController.getController().findByCommandId(command.getId());
    }
    
    public HashMap<Article,Integer> printStock(){
        return SystemStock.getSystemStock().getStock();
    }
    
    public HashMap<Article,Integer> printInsufficientArticle(){
        return SystemStock.getSystemStock().checkStock();
    }
}
