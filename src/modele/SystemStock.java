package modele;

import controller.jpacontroller.ArticleJpaController;
import java.util.HashMap;
import java.util.List;
import modele.entity.AisleArticles;
import modele.entity.Article;
import modele.entity.WarehouseArticles;

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

	public boolean restock(Article article, int quantity) {
        WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
        if(warehouseArticle.getQuantity()<quantity){
            return false;
        }
        removeArticleFromAisle(article, quantity);
        addArticleToAisle(article, quantity);
        return true;
	}

	public void addArticleToWarehouse(Article article, int quantity) {
		this.aWarehouse.addArticleToWarehouse(article, quantity);
	}
    
    public boolean removeArticleFromWarehouse(Article article, int quantity) {
		return this.aWarehouse.removeArticleFromWarehouse(article, quantity);
	}
    
    public void addArticleToAisle(Article article, int quantity) {
		this.aAisle.addArticleToAisle(article, quantity);
	}
    
    public boolean removeArticleFromAisle(Article article, int quantity) {
		return this.aAisle.removeArticleFromAisle(article, quantity);
    }

	public HashMap<Article,Integer> checkStock() {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();
        
        for(Article article : listArticle){
            WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);
            
            int warehouseStock = 0;
            if(warehouseArticle != null) warehouseArticle.getQuantity();
            int aisleStock = 0;
            if(aisleArticle != null) aisleArticle.getQuantity();
            
            int totalStock = warehouseStock + aisleStock;
            
            if(totalStock<article.getThreshold()){
                map.put(article, totalStock);
            }
        }
        return map;
    }

	public HashMap<Article,Integer> getStock() {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();
        
        for(Article article : listArticle){
            WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);
            
            int warehouseStock = 0;
            if(warehouseArticle != null) warehouseArticle.getQuantity();
            int aisleStock = 0;
            if(aisleArticle != null) aisleArticle.getQuantity();
            
            int totalStock = warehouseStock + aisleStock;
            
            map.put(article, totalStock);
        }
        return map;
    }
}