package schemaobjects;

public class OrderContent {
    private int product_id;
    private String productName;
    private int quantity;
    private float priceEach;
    
    public OrderContent(int product_id, String name, int quantity, float price) {
        this.product_id = product_id;
        this.productName = name;
        this.quantity = quantity;
        this.priceEach = price;
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

    public float getPriceEach() {
        return priceEach;
    }

    public String getProductName() {
        return productName;
    }
}
