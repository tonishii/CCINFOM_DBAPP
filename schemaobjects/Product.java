package schemaobjects;
import java.sql.*;

public class Product {
    private long     product_id;
    private long     seller_id;

    private String   product_name;
    private float    product_price;
    private String   product_type;

    private float    average_rating;
    private int      quantity_stocked;
    private boolean  listed_status;
    private String   description;
    
    public Product(long seller_id, String product_name, float product_price, String product_type) {
        this.seller_id = seller_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_type = product_type;
        // sql to initialize product id
    }
    
    // idk if need setters for the IDs pero i placed them jic
    public void setProductID(long product_id) {
        this.product_id = product_id;
    }
    
    public void setSellerID(long seller_id) {
        this.seller_id = seller_id;
    }
    
    public void setName(String product_name) {
        this.product_name = product_name;
    }
    
    public void setPrice(float product_price) {
        this.product_price = product_price;
    }
    
    public void setType(String product_type) {
        this.product_type = product_type; 
    }
    
    public void updateRating() {
        
    }
    
    public void setQuantity(int quantity_stocked) {
        this.quantity_stocked = quantity_stocked;
    }
    
    public void updateListedStatus() {
        if (this.quantity_stocked == 0)
            this.listed_status = false;
        else
            this.listed_status = true;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public long getProductID() {
        return this.product_id;
    }
    
    public long getSellerID() {
        return this.seller_id;
    }
    
    public String getName() {
        return this.product_name;
    }
    
    public float getPrice() {
        return this.product_price;
    }
    
    public String getType() {
        return this.product_type;
    }
    
    public float getRating() {
        return this.average_rating;
    }
    
    public int getQuantity() {
        return this.quantity_stocked;
    }
    
    public boolean isListed() {
        return this.listed_status;
    }
    
    public String getDescription() {
        return this.description;
    }
}
