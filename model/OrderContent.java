package model;

import java.sql.Connection;
import java.sql.*;
import java.util.Objects;

public class OrderContent {
    private int order_id;
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
    
    public OrderContent(int order_id, int product_id, String name) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.productName = name;
    }
    
    public void sendToDB(int order_id, Connection conn) throws SQLException {
        String query =
        """
        INSERT INTO order_contents
        VALUES (?, ?, ?, ?, NULL)
        """;
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, order_id);
        ps.setInt(2, this.product_id);
        ps.setInt(3, this.quantity);
        ps.setFloat(4, (float) this.quantity * this.priceEach);
        ps.executeUpdate();
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getProductID() { return this.product_id; }
    public int getQuantity() { return this.quantity; }
    public float getPriceEach() { return this.priceEach; }
    public String getProductName() { return this.productName; }
    public int getOrderID() { return this.order_id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderContent that = (OrderContent) o;
        return product_id == that.product_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id);
    }
}
