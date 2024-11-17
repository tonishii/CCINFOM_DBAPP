package schemaobjects;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;

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
    
    public void insertOrderContent(int order_id, Connection conn) {
        try {
            String query = """
                        SELECT average_rating
                        FROM products
                        WHERE product_id = ?
                           """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, this.product_id);
            ResultSet rs = ps.executeQuery();
            float avg_rating = 0.0f;
            
            if (rs.next()) {
                avg_rating = rs.getFloat("average_rating");
            }
            
            query = """
               INSERT INTO order_contents
               VALUES (?, ?, ?, ?, ?)
               """;
            ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ps.setInt(2, this.product_id);
            ps.setInt(3, this.quantity);
            ps.setFloat(4, (float) this.quantity * this.priceEach);
            ps.setFloat(5, avg_rating);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error while adding order content: " + e);
        }
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
