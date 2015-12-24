package modele;

import controller.jpacontroller.AisleArticlesJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.entity.AisleArticles;
import modele.entity.Article;

/**
 *
 * @author anatole
 */
public class ManagerAisle {

    private AisleArticlesJpaController aAisleController;
    
    private static ManagerAisle managerAisle = null;
    
    public static ManagerAisle getManagerAisle(){
        if(managerAisle == null) managerAisle = new ManagerAisle();
        return managerAisle;
    }
    
    private ManagerAisle()
    {
        this.aAisleController = AisleArticlesJpaController.getController();
    }
    
    /**
     * Ajouter article a rayon
     * @param article 
     */
    public void addArticleToAisle(Article article, int quantity)
    {
        // recherche article dans rayon
        AisleArticles tmp = this.aAisleController.findByArticleId(article.getId());
         
        if(tmp == null)
        {
            tmp = new AisleArticles();
            tmp.setArticleId(article);
            tmp.setQuantity(quantity);
            this.aAisleController.create(tmp);
        }
        else
        {
            try {
                tmp.setQuantity(tmp.getQuantity()+quantity);
                this.aAisleController.edit(tmp);
            } catch (Exception ex) {
                Logger.getLogger(ManagerAisle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Retirer article du rayon
     * @param article 
     * @param quantity 
     */
    public boolean removeArticleFromAisle(Article article, int quantity)
    {
        AisleArticles tmp = this.aAisleController.findByArticleId(article.getId());
         
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
                    this.aAisleController.destroy(tmp.getId());
                }
                else{
                    tmp.setQuantity(tmp.getQuantity()-quantity);
                    this.aAisleController.edit(tmp);
                }
                return true;
            } catch (Exception ex) {
                Logger.getLogger(ManagerAisle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public AisleArticles getArticleFromAisle(Article article)
    {
        AisleArticles tmp = this.aAisleController.findByArticleId(article.getId());
        return tmp;
    }
}
