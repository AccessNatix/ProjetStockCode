package modele;

import controleur.AisleArticlesJpaController;
import modele.entity.AisleArticles;

/**
 *
 * @author anatole
 */
public class ManagerAisle {

    private AisleArticlesJpaController aAisleController;
    
    public ManagerAisle()
    {
    }
    
    public void addArticleToAisle(AisleArticles article)
    {
    }
    
    public void removeArticleFromAisle(AisleArticles article)
    {
        
    }
    
    public AisleArticles getArticleFromAisle(int id)
    {
        return new AisleArticles();
    }
}
