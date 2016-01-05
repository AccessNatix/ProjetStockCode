package modele;

import controller.jpacontroller.AisleArticlesJpaController;
import controller.jpacontroller.ArticleJpaController;
import java.util.HashMap;
import java.util.List;
import modele.entity.AisleArticles;
import modele.entity.Article;
import modele.entity.WarehouseArticles;

/**
* Cette classe est utilisé pour gérer l'ensemble du stock
*
*/
public class SystemStock {
	private final ManagerAisle aAisle;
	private final ManagerWarehouse aWarehouse;
    private static SystemStock systemStock = null;

    public static SystemStock getSystemStock(){
        if(systemStock == null) systemStock = new SystemStock();
        return systemStock;
    }

    private SystemStock(){
        this.aAisle = ManagerAisle.getManagerAisle();
        this.aWarehouse = ManagerWarehouse.getManagerWarehouse();
    }

	/**
	* Remettre du stock dans les rayons depuis l'entrepôt
	*/
	public boolean restock(Article article, int quantity) {
        WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
        if(warehouseArticle.getQuantity()<quantity){
            return false;
        }
        removeArticleFromWarehouse(article, quantity);
        addArticleToAisle(article, quantity);
        return true;
	}

	/**
	* Ajout article à l'entrepôt
	*/
	public void addArticleToWarehouse(Article article, int quantity) {
		this.aWarehouse.addArticleToWarehouse(article, quantity);
	}

	/**
	* Retrait d'un article de l'entrepôt
	*/
  public boolean removeArticleFromWarehouse(Article article, int quantity) {
		return this.aWarehouse.removeArticleFromWarehouse(article, quantity);
	}

	/**
	* Ajout d'un article au rayon
	*/
  public void addArticleToAisle(Article article, int quantity) {
		this.aAisle.addArticleToAisle(article, quantity);
	}

	/**
	* Enlever un article depuis le rayon
	*/
  public boolean removeArticleFromAisle(Article article, int quantity) {
		return this.aAisle.removeArticleFromAisle(article, quantity);
  }

	/**
	* Vérifier le stock et si besoin retourner les articles sous le seuil
	*
	*/
	public HashMap<Article,Integer> checkStock() {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();

        for(Article article : listArticle){
            WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);

            int warehouseStock = 0;
            if(warehouseArticle != null) warehouseStock = warehouseArticle.getQuantity();
            int aisleStock = 0;
            if(aisleArticle != null) aisleStock = aisleArticle.getQuantity();

            int totalStock = warehouseStock + aisleStock;

            if(totalStock<article.getThreshold()){
                map.put(article, totalStock);
            }
        }
        return map;
    }

    /**
     * Cette méthode est utilisé pour récupérer les articles en stock
     * @return
     */
    public HashMap<Article, Integer> getStockInAisle()
    {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();

        for(Article article : listArticle){
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);

            int aisleStock = 0;
            if(aisleArticle != null) aisleStock = aisleArticle.getQuantity();

            int totalStock = aisleStock;

            map.put(article, totalStock);
        }

        return map;
    }

	/**
	* Récupérer l'ensemble du stock présent dans le rayon et dans l'entrepôt
	*
	*/
	public HashMap<Article,Integer> getStock() {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();

        for(Article article : listArticle){
            WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);

            int warehouseStock = 0;
            if(warehouseArticle != null) warehouseStock = warehouseArticle.getQuantity();
            int aisleStock = 0;
            if(aisleArticle != null) aisleStock = aisleArticle.getQuantity();

            int totalStock = warehouseStock + aisleStock;

            map.put(article, totalStock);
        }
        return map;
    }
}
