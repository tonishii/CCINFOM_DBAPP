package schemaobjects;
public class OrderContent {
    private long product_id;
    private int  quantity;
    
    public OrderContent(long product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public long getProductID() {
        return this.product_id;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
}
