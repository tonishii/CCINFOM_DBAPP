package schemaobjects;

public class OrderContent {
    private int product_id;
    private int  quantity;
    
    public OrderContent(int product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getProductID() {
        return this.product_id;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
}
