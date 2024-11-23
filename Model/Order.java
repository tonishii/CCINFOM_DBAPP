package Model;
import java.sql.Date;
import java.sql.*;

import Model.enums.OrderStatus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

// Order represents an order made in the E-Commerce app
public class Order {
    private final int        order_id;
    private final int        user_id;
    private final int        courier_id;

    private final Date        purchase_date;
    private final float       total_price;
    private final OrderStatus order_status;
    private final Date        receive_date;
    
    public Order(int order_id, int user_id, int courier_id, Date purchase_date, float total_price, OrderStatus order_status, Date receive_date) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.courier_id = courier_id;
        this.purchase_date = purchase_date;
        this.total_price = total_price;
        this.order_status = order_status;
        this.receive_date = receive_date;
    }

    // Adds a new record of an Order to the orders table with its attributes as values
    // while also adding all the contents of the cart (selected items specifically) to the order_contents table
    public void sendToDB(Connection conn, ArrayList<OrderContent> cart) throws SQLException {
        String query =
        """
        INSERT INTO orders
        VALUES(?, ?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, this.order_id);
        ps.setInt(2, this.user_id);
        ps.setInt(3, this.courier_id);
        ps.setDate(4, this.purchase_date);
        ps.setFloat(5, this.total_price);
        ps.setString(6, this.order_status.toString());
        ps.setDate(7, this.receive_date);

        ps.executeUpdate();

        // Query for checking the current quantity of the current product
        query =
        """
        SELECT quantity_stocked
        FROM products
        WHERE product_id = ?
        """;

        // Query for updating the new quantity and if the product is still listed
        String update =
        """
        UPDATE products
        SET quantity_stocked = ?, listed_status = ?
        WHERE product_id = ?
        """;

        for (OrderContent product : cart) {
            product.sendToDB(order_id, conn);

            ps = conn.prepareStatement(query);
            ps.setInt(1, product.getProductID());
            ResultSet rs = ps.executeQuery();

            int currQty = 0;
            if (rs.next()) {
                currQty = rs.getInt("quantity_stocked");
            }

            if (currQty - product.getQuantity()  < 0) {
                // Throw an error when the most recent quantity minus the users quantity causes an error
                throw new SQLException("Only " + currQty + " are in stock.");
            }

            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setInt(1, (currQty - product.getQuantity()));
            pstmt.setBoolean(2, ((currQty - product.getQuantity()) != 0));
            pstmt.setInt(3, product.getProductID());

            pstmt.executeUpdate();
        }
    }

    // Updates the selected order made by the user to be in delivery
    public static boolean receiveOrder(Connection conn, int order_id, int user_id) {
        try {
            String query = 
                """
                SELECT 1
                FROM orders
                WHERE order_id = ? AND user_id = ?
                AND order_status = 'FOR_DELIVERY'
                """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ps.setInt(2, user_id);
            ResultSet rs = ps.executeQuery();
            
            if (!(rs.next())) return false;
            
            query = 
                """
                UPDATE orders
                SET order_status = 'DELIVERED',
                receive_date = NOW()
                WHERE order_id = ?
                """;
            ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            
            ps.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while receiving order: " + e);
            return false;
        }
        
        return true;
    }

    public String toString() { return order_id + " " + purchase_date.toString() + " " + total_price; }

    public int getOrderID() {
        return this.order_id;
    }
    public int getUserID() {
        return this.user_id;
    }
    public int getCourierID() {
        return this.courier_id;
    }
    public Date getPurchaseDate() {
        return this.purchase_date;
    }
    public float getTotalPrice() {
        return this.total_price;
    }
    public OrderStatus getStatus() {
        return this.order_status;
    }
    public Date getReceiveDate() {
        return this.receive_date;
    }
}
