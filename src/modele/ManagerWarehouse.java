package modele;

import controller.jpacontroller.WarehouseArticlesJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.entity.AisleArticles;
import modele.entity.Article;
import modele.entity.WarehouseArticles;

/**
 *
 * @author anatole
 */
public class ManagerWarehouse {
    
    private WarehouseArticlesJpaController aWarehouseController;
    
    private static ManagerWarehouse managerWarehouse = null;
    
    public static ManagerWarehouse getManagerWarehouse(){
        if(managerWarehouse == null) managerWarehouse = new ManagerWarehouse();
        return managerWarehouse;
    }
    
    private ManagerWarehouse()
    {
        this.aWarehouseController = WarehouseArticlesJpaController.getController();
    }
    
    /**
     * Ajouter article a rayon
     * @param article 
     */
    public void addArticleToWarehouse(Article article, int quantity)
    {
        // recherche article dans rayon
        WarehouseArticles tmp = this.aWarehouseController.findByArticleId(article.getId());
         
        if(tmp == null)
        {
            tmp = new WarehouseArticles();
            tmp.setArticleId(article);
            tmp.setQuantity(quantity);
            this.aWarehouseController.create(tmp);
        }
        else
        {
            try {
                tmp.setQuantity(tmp.getQuantity()+quantity);
                this.aWarehouseController.edit(tmp);
            } catch (Exception ex) {
                Logger.getLogger(ManagerAisle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Retirer article du rayon
     * @param article 
     */
    public boolean removeArticleFromWarehouse(Article article, int quantity)
    {
        WarehouseArticles tmp = this.aWarehouseController.findByArticleId(article.getId());
         
        if(tmp == null)
        {
            return false;
        }
        else
        {
            try {
                if(tmp.getQuantity()<quantity) {
                    return false;
                }
                else if (tmp.getQuantity()==quantity){
                    this.aWarehouseController.destroy(tmp.getId());
                }
                else{
                    tmp.setQuantity(tmp.getQuantity()-quantity);
                    this.aWarehouseController.edit(tmp);
                }
                return true;
                
            } catch (Exception ex) {
                Logger.getLogger(ManagerWarehouse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public WarehouseArticles getArticleFromWarehouse(Article article)
    {
        WarehouseArticles tmp = this.aWarehouseController.findByArticleId(article.getId());
        return tmp;
    }
}
