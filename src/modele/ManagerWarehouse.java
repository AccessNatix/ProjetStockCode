package modele;

import controleur.jpacontroleur.AisleArticlesJpaController;
import controleur.jpacontroleur.WarehouseArticlesJpaController;
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
