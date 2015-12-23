package modele;

import controleur.jpacontroleur.AisleArticlesJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.entity.AisleArticles;

/**
 *
 * @author anatole
 */
public class ManagerAisle {

    private AisleArticlesJpaController aAisleController;
    
    public ManagerAisle()
    {
        this.aAisleController = new  AisleArticlesJpaController(null);
    }
    
    /**
     * Ajouter article a rayon
     * @param article 
     */
    public void addArticleToAisle(AisleArticles article)
    {
        // recherche article dans rayon
        AisleArticles tmp = this.aAisleController.findAisleArticles(article.getId());
         
        if(tmp == null)
        {
            this.aAisleController.create(article);
        }
        else
        {
            try {
                tmp.setQuantity(tmp.getQuantity()+article.getQuantity());
                this.aAisleController.edit(article);
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
    
    public AisleArticles getArticleFromAisle(int id)
    {
        AisleArticles tmp = this.aAisleController.findAisleArticles(id);
        return tmp;
    }
}
