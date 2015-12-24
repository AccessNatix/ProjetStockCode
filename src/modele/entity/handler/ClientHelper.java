/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.entity.handler;

import controller.jpacontroller.ClientArticlesJpaController;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.SystemStock;
import modele.entity.Article;
import modele.entity.Client;
import modele.entity.ClientArticles;
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
    
    public boolean addArticle(Article article, int quantity){
        SystemStock systemStock = SystemStock.getSystemStock();
        boolean b = systemStock.removeArticleFromAisle(article, quantity);
        if(b == false) return false;
        
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
}
