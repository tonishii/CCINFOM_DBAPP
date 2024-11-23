package Model;
import java.sql.Date;
import java.sql.*;

import Model.enums.ReturnReason;
import Model.enums.ReturnStatus;

import javax.swing.*;

// Return represents a Request for Return made by a user
public class Return {
    private final int          order_id;
    private final int          product_id;
    private final int          courier_id;

    private final ReturnReason  return_reason;
    private final String        return_description;
    private final Date          return_date;
    private final ReturnStatus  return_status;
    
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
                WHERE o.user_id = ? AND o.order_status = 'DELIVERED'
                AND oc.product_id = ?
                """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user_id);
            ps.setInt(2, product_id);
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

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in requesting return: " + e);
            return false;
        }
        return true;
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
