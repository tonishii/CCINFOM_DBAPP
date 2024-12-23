package Model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.*;
import java.util.Objects;

// OrderContent represents the many contents of an order including some of the products important information
public class OrderContent {
    private int order_id;
    private final int product_id;
    private final String productName;
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

    // Adds a record of the OrderContent in its respective table using its attributes
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

    // Sets the rating of a product in the orders table given the rating and the order
    // it also updates the average rating that a product has in the database
    public static boolean rateProduct(Connection conn, int order_id, int product_id, int user_id, int rating) {
        try {
            String query = 
                """
                UPDATE order_contents oc
                JOIN orders o ON oc.order_id = o.order_id
                SET oc.product_rating = ?
                WHERE oc.order_id = ?
                AND oc.product_id = ?
                AND o.user_id = ?
                AND o.order_status = 'DELIVERED'
                AND oc.product_rating IS NULL
                """;
        
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, rating);
            ps.setInt(2, order_id);
            ps.setInt(3, product_id);
            ps.setInt(4, user_id);
            int rowcount = ps.executeUpdate();
            
            if (rowcount == 0) {
                return false;
            }

            Product.updateRating(conn, product_id);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while rating product: " + e);
            return false;
        }
        return true;
    }

    public OrderContent setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
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
