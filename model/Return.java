package model;
import java.sql.Date;
import java.sql.*;
import java.util.Scanner;

import enums.ReturnReason;
import enums.ReturnStatus;

public class Return {
    private int          order_id;
    private int          product_id;
    private int          courier_id;

    private ReturnReason  return_reason;
    private String        return_description;
    private Date          return_date;
    private ReturnStatus  return_status;
    
    public Return(int order_id, int product_id, int courier_id, ReturnReason return_reason, String return_description, Date return_date, ReturnStatus return_status) {
        this.order_id = order_id;
        this.courier_id = courier_id;
        this.product_id = product_id;
        this.return_reason = return_reason;
        this.return_description = return_description;
        this.return_date = return_date;
        this.return_status = return_status;
    }
    
    public static boolean requestReturn(Connection conn, int product_id, int order_id, int user_id, String reason, String description) {
        if (Courier.assignCourier(conn) == -1) {
            return false;
        }
        
        try {
            String query = 
                    """
                    SELECT 1
                    FROM orders o
                    JOIN order_contents oc ON o.order_id = oc.order_id
                    WHERE o.user_id = ?
                    """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user_id);
            ResultSet rs =  ps.executeQuery();
            
            if (!(rs.next())) return false;
            
            query = 
                """
                INSERT INTO `returns`
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;
            ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ps.setInt(2, product_id);
            ps.setInt(3, Courier.assignCourier(conn));
            ps.setString(4, reason);
            ps.setString(5, description);
            ps.setDate(6, Date.valueOf("9999-12-31"));
            ps.setString(7, ReturnStatus.PROCESSING.name());
            
            ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error in requesting return: " + e);
                return false;
            }
        return true;
    }
    
    public void approveReturn(Connection conn, int product_id, int order_id) {
        try {
            String query = 
                    """
                    UPDATE `returns`
                    SET return_status = 'REFUNDED'
                    WHERE product_id = ?
                    AND order_id = ?
                    """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, product_id);
            ps.setInt(2, order_id);
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    public void rejectReturn(Connection conn, int product_id, int order_id) {
        try {
            String query = 
                    """
                    UPDATE `returns`
                    SET return_status = 'REJECTED'
                    WHERE product_id = ?
                    AND order_id = ?
                    """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, product_id);
            ps.setInt(2, order_id);
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
    public int getOrderID() {
        return this.order_id;
    }
    public int getProductID() {
        return this.product_id;
    }
    public int getCourierID() {
        return this.courier_id;
    }
    public ReturnReason getReason() {
        return this.return_reason;
    }
    public String getDescription() {
        return this.return_description;
    }
    public Date getDate() {
        return this.return_date;
    }
    public ReturnStatus getStatus() {
        return this.return_status;
    }
}
