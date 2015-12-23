package modele;

import controleur.jpacontroleur.AisleArticlesJpaController;
import controleur.jpacontroleur.WarehouseArticlesJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.entity.AisleArticles;
import modele.entity.WarehouseArticles;

/**
 *
 * @author anatole
 */
public class ManagerWarehouse {
    
    private WarehouseArticlesJpaController aWarehouseController;
    
    public ManagerWarehouse()
    {
        this.aWarehouseController = new  WarehouseArticlesJpaController(null);
    }
    
    /**
     * Ajouter article a rayon
     * @param article 
     */
    public void addArticleToWarehouse(WarehouseArticles article)
    {
        // recherche article dans rayon
        WarehouseArticles tmp = this.aWarehouseController.findWarehouseArticles(article.getId());
         
        if(tmp == null)
        {
            this.aWarehouseController.create(article);
        }
        else
        {
            try {
                tmp.setQuantity(tmp.getQuantity()+article.getQuantity());
                this.aWarehouseController.edit(article);
            } catch (Exception ex) {
                Logger.getLogger(ManagerAisle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Retirer article du rayon
     * @param article 
     */
    public void removeArticleFromAisle(AisleArticles article)
    {
        
    }
    
    public WarehouseArticles getArticleFromAisle(int id)
    {
        WarehouseArticles tmp = this.aWarehouseController.findWarehouseArticles(id);
        return tmp;
    }
}
