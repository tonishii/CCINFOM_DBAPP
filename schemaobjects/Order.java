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
    
    public Order(int order_id, int user_id, int courier_id, Date purchase_date, float total_price, OrderStatus order_status, Date receive_date) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.courier_id = courier_id;
        this.purchase_date = purchase_date;
        this.total_price = total_price;
        this.order_status = order_status;
        this.receive_date = receive_date;
    }
    
    public static void generateOrder(int user_id, Connection conn, ArrayList<OrderContent> cart, float total_price) {
        try {
            if (Courier.assignCourier(conn) != -1) {
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
                    Product.updateQuantity(conn, product.getProductID(), product.getQuantity());
                }
            }
            else {
                System.out.println("No available couriers for delivery.");
            }
        } catch (Exception e) {
            System.out.println("Error during order generation: " + e);
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
