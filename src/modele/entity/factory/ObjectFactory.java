package modele.entity.factory;

import java.math.BigDecimal;
import java.util.HashMap;
import modele.entity.Article;
import modele.entity.CashRegister;
import modele.entity.Cashier;
import modele.entity.Client;
import modele.entity.Key;
import modele.entity.Provider;
import modele.entity.Retailer;
import modele.entity.Session;

/**
 *
 * @author anatole
 */
public class ObjectFactory {
    public static Article createArticle(Provider provider, String barcode, String type, String name, BigDecimal price, int stock, int threshold){
        HashMap<String, Object> map = new HashMap<>();
        map.put("provider",provider);
        map.put("barcode",barcode);
        map.put("type",type);
        map.put("name",name);
        map.put("price",price);
        map.put("stock",stock);
        map.put("threshold",threshold);
        return EntityFactory.create(new Article(), map);
    }
    
    public static Client createClient(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Client(), map);
    }
    
    public static Retailer createRetailer(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Retailer(), map);
    }
    
    public static Provider createProvider(String adress, String name, String phone, String postalCode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("adress",adress);
        map.put("name",name);
        map.put("phone",phone);
        map.put("postalCode",postalCode);
        return EntityFactory.create(new Provider(), map);
    }
    
    public static Cashier createCashier(String firstname, String lastname){
        HashMap<String, Object> map = new HashMap<>();
        map.put("firstname",firstname);
        map.put("lastname",lastname);
        return EntityFactory.create(new Cashier(), map);
    }
    
    public static CashRegister createCashRegister(){
        return EntityFactory.create(new CashRegister(), null);
    }
    
    public static Session createSession(String pseudo, String password){
        HashMap<String, Object> map = new HashMap<>();
        map.put("pseudo",pseudo);
        map.put("password",password);
        return EntityFactory.create(new Session(), map);
    }
    
    public static Key createKey(CashRegister cashRegister){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cashRegister",cashRegister);
        return EntityFactory.create(new Key(), map);
    }
    
}
