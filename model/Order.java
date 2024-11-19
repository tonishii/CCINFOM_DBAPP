package model;
import java.sql.Date;
import java.sql.*;

import enums.OrderStatus;
import java.util.ArrayList;
import java.util.Set;

public class Order {
    private int        order_id;
    private int        user_id;
    private int        courier_id;

    private Date        purchase_date;
    private float       total_price;
    private OrderStatus order_status;
    private Date        receive_date;
    
    public Order(int order_id, int user_id, int courier_id, Date purchase_date, float total_price, OrderStatus order_status, Date receive_date) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.courier_id = courier_id;
        this.purchase_date = purchase_date;
        this.total_price = total_price;
        this.order_status = order_status;
        this.receive_date = receive_date;
    }
    
    public void sendToDB(Connection conn, Set<OrderContent> cart) throws SQLException {
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

        query =
        """
        SELECT quantity_stocked
        FROM products
        WHERE product_id = ?
        """;

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
                throw new SQLException("Only " + currQty + " are in stock.");
            }

            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setInt(1, (currQty - product.getQuantity()));
            pstmt.setBoolean(2, ((currQty - product.getQuantity()) != 0));
            pstmt.setInt(3, product.getProductID());

            pstmt.executeUpdate();
        }
    }
    
    public static void displayOrderContents(Connection conn, int order_id) {
        ArrayList<OrderContent> oc = getOrderContents(conn, order_id);
        System.out.println("Product ID | Product Name | Quantity Purchased | Subtotal");
        for (OrderContent item : oc) {
            System.out.printf("%d | %s | %d | %f\n", item.getProductID(), item.getProductName(), item.getQuantity(), (float) item.getPriceEach() * item.getQuantity());
        }
    }
    
    public static ArrayList<OrderContent> getOrderContents(Connection conn, int order_id) {
        ArrayList<OrderContent> oc = new ArrayList<>();
        try {
            String query = 
                """
                SELECT p.product_id, p.product_name, oc.item_quantity, oc.subtotal
                FROM order_contents oc
                JOIN products p ON oc.product_id = p.product_id
                WHERE oc.order_id = ?
                """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                oc.add(new OrderContent(rs.getInt("p.product_id"), rs.getString("p.product_name"), rs.getInt("oc.item_quantity"), rs.getFloat("oc.subtotal")));
            }
        } catch (Exception e) {
            System.out.println("Error in fetching order contents: " + e);
        }      
        return oc;
    }

    public String toString() { return order_id + " " + purchase_date.toString() + " " + total_price; }

    public void setReceiveDate(Date date) {
        this.receive_date = date;
    }
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
