package modele;

public class Article {
    private String aName;
    private String aCodebar;
    private String aStock;
    private String aThreshold;
    private String aPrice;
    private String aWeight;
    private String aProvider;

    public Article(String aName, String aCodebar, String aStock, String aThreshold, String aPrice, String aWeight, String aProvider) {
        this.aName = aName;
        this.aCodebar = aCodebar;
        this.aStock = aStock;
        this.aThreshold = aThreshold;
        this.aPrice = aPrice;
        this.aWeight = aWeight;
        this.aProvider = aProvider;
    }

    public String getName() {
        return aName;
    }

    public void setName(String aName) {
        this.aName = aName;
    }

    public String getCodebar() {
        return aCodebar;
    }

    public void setCodebar(String aCodebar) {
        this.aCodebar = aCodebar;
    }

    public String getStock() {
        return aStock;
    }

    public void setStock(String aStock) {
        this.aStock = aStock;
    }

    public String getThreshold() {
        return aThreshold;
    }

    public void setThreshold(String aThreshold) {
        this.aThreshold = aThreshold;
    }

    public String getPrice() {
        return aPrice;
    }

    public void setPrice(String aPrice) {
        this.aPrice = aPrice;
    }

    public String getWeight() {
        return aWeight;
    }

    public void setWeight(String aWeight) {
        this.aWeight = aWeight;
    }

    public String getProvider() {
        return aProvider;
    }

    public void setProvider(String aProvider) {
        this.aProvider = aProvider;
    }
}