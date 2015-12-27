/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.ClientArticlesJpaController;
import controller.jpacontroller.ClientArticlesReturnJpaController;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.Client;
import modele.entity.ClientArticles;
import modele.entity.ClientArticlesReturn;
import modele.entity.factory.EntityFactory;

/**
 *
 * @author yo
 */
public class ClientHelper {
    private Client aClient;
    
    public ClientHelper(Client client){
        this.aClient = client;
    }
    
    public boolean addArticle(Article article, int quantity, boolean buy){
        if(buy == true) return addArticleBuy(article, quantity);
        return addArticleReturn(article, quantity);
    }
    
    public List<ClientArticles> getArticles(){
        return ClientArticlesJpaController.getController().findByClientId(aClient.getId());
    }
    
    public List<ClientArticlesReturn> getArticlesReturn(){
        return ClientArticlesReturnJpaController.getController().findByClientId(aClient.getId());
    }
    
    private boolean addArticleBuy(Article article, int quantity){
        SystemStock systemStock = SystemStock.getSystemStock();

        ClientArticlesJpaController clientArticlesJpaController = ClientArticlesJpaController.getController();
        ClientArticles clientArticle = clientArticlesJpaController.findByIds(aClient.getId(), article.getId());
        if(clientArticle == null){
            HashMap<String,Object> map = new HashMap<>();
            map.put("article", article);
            map.put("client", aClient);
            map.put("quantity", quantity);
            clientArticle = EntityFactory.create(new ClientArticles(),map);
            if(clientArticle == null) return false;
        }
        else{
            clientArticle.setQuantity(clientArticle.getQuantity()+quantity);
            try {
                clientArticlesJpaController.edit(clientArticle);
            } catch (Exception ex) {
                Logger.getLogger(ClientHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    private boolean addArticleReturn(Article article, int quantity){
        ClientArticlesReturnJpaController clientArticlesReturnJpaController = ClientArticlesReturnJpaController.getController();
        ClientArticlesReturn clientArticlesReturn = clientArticlesReturnJpaController.findByIds(aClient.getId(), article.getId());
        if(clientArticlesReturn == null){
            HashMap<String,Object> map = new HashMap<>();
            map.put("article", article);
            map.put("client", aClient);
            map.put("quantity", quantity);
            clientArticlesReturn = EntityFactory.create(new ClientArticlesReturn(),map);
            if(clientArticlesReturn == null) return false;
        }
        else{
            clientArticlesReturn.setQuantity(clientArticlesReturn.getQuantity()+quantity);
            try {
                clientArticlesReturnJpaController.edit(clientArticlesReturn);
            } catch (Exception ex) {
                Logger.getLogger(ClientHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    public boolean removeArticles(boolean buy){
        if(buy == true) return removePayArticles();
        return removeReturnArticles();
    }
    
    private boolean removeReturnArticles(){
        ClientArticlesReturnJpaController.getController().removeByClientId(aClient.getId());
        return true;
    }
    
    private boolean removePayArticles(){
        ClientArticlesJpaController.getController().removeByClientId(aClient.getId());
        return true;
    }
}
