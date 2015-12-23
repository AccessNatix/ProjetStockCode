/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import controleur.AisleArticlesJpaController;
import controleur.WarehouseArticlesJpaController;
import modele.entity.AisleArticles;
import modele.entity.WarehouseArticles;

/**
 *
 * @author anatole
 */
public class ManagerWarehouse {
    
    private WarehouseArticlesJpaController aAisleController;
    
    public ManagerWarehouse()
    {
    }
    
    public void addArticleToWarehouse(AisleArticles article)
    {
    }
    
    public void removeArticleFromWarehouse(AisleArticles article)
    {
        
    }
    
    public WarehouseArticles getArticleFromWarehouse(int id)
    {
        return new WarehouseArticles();
    }    
}
