package schemaobjects;
import java.sql.Date;
import java.sql.*;

import enums.OrderStatus;
import java.util.ArrayList;

public class Order {
    private int        order_id;
    private int        user_id;
    private int        courier_id;

    private Date        purchase_date;
    private float       total_price;
    private OrderStatus order_status;
    private Date        receive_date;
    
    public Order(int user_id) {
        this.user_id = user_id;
        // sql to generate order id and calculate for courier id
        long ms = System.currentTimeMillis();
        this.purchase_date = new Date(ms);
        this.order_status = OrderStatus.BEING_PREPARED;
    }
    
    public static void generateOrder(int user_id, Connection conn, ArrayList<OrderContent> cart) {
        try {
            int order_id = -1;
            String query =
                """
                SELECT IFNULL(MAX(order_id), 0) + 1 AS id
                FROM orders
                """;
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                order_id = rs.getInt("id");
            }
            
            float total_price = 0.0f;            
            for (OrderContent product : cart) {
                total_price += (float) product.getPriceEach() * product.getQuantity();
            }
            
            query = 
                """
                INSERT INTO orders
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;
            ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ps.setInt(2, user_id);
            ps.setInt(3, Courier.assignCourier(conn));
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setFloat(5, total_price);
            ps.setString(6, OrderStatus.BEING_PREPARED.name());
            ps.setDate(7, Date.valueOf("9999-12-31"));
            
            ps.executeUpdate();
                       
            for (OrderContent product : cart) {
                product.insertOrderContent(order_id, conn);
            }
        } catch (Exception e) {
            System.out.println("Error during order generation: " + e);
        }
    }
    
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
