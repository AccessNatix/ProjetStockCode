package modele;

import java.util.LinkedList;

public class Aisle {
	public LinkedList<Article> aArticle;

    public Aisle()
    {
        this.aArticle = new LinkedList<>();
    }
    
    /**
     * 
     * @param pArticle 
     */
	public void add(Article pArticle) {
        // vérification de la précense de l'article
        for(Article article : aArticle)
        {
            // si article présent on ajoute au stock
            if(article.getName().equals(pArticle.getName()))
            {
                article.setStock(pArticle.getStock()+article.getStock());
                return;
            }
        }
        
        this.aArticle.add(pArticle);
	}

	public void remove(Article pArticle) {

	}
}
