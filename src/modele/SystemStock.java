package modele;

import controleur.jpacontroleur.ArticleJpaController;
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

	public void restock(Article article, int quantity) {
        WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
        if(warehouseArticle.getQuantity()<quantity){
            throw new UnsupportedOperationException();
        }
        removeArticleFromAisle(article, quantity);
        addArticleToAisle(article, quantity);
	}

	public void addArticleToWarehouse(Article article, int quantity) {
		this.aWarehouse.addArticleToWarehouse(article, quantity);
	}
    
    public void removeArticleFromWarehouse(Article article, int quantity) {
		this.aWarehouse.removeArticleFromWarehouse(article, quantity);
	}
    
    public void addArticleToAisle(Article article, int quantity) {
		this.aAisle.addArticleToAisle(article, quantity);
	}
    
    public void removeArticleFromAisle(Article article, int quantity) {
		this.aAisle.removeArticleFromAisle(article, quantity);
    }

	public HashMap<Article,Integer> checkStock() {
        HashMap<Article,Integer> map = new HashMap<>();
        List<Article> listArticle = ArticleJpaController.getController().findArticleEntities();
        
        for(Article article : listArticle){
            WarehouseArticles warehouseArticle = aWarehouse.getArticleFromWarehouse(article);
            AisleArticles aisleArticle = aAisle.getArticleFromAisle(article);
            
            int warehouseStock = warehouseArticle.getQuantity();
            int aisleStock = aisleArticle.getQuantity();
            
            int totalStock = warehouseStock + aisleStock;
            
            if(totalStock<article.getThreshold()){
                map.put(article, totalStock);
            }
        }
        return map;
}
}