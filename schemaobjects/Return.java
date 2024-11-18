package schemaobjects;
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
    
    public Return(int order_id, int product_id, ReturnReason return_reason, String return_description) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.return_reason = return_reason;
        this.return_description = return_description;
        long ms = System.currentTimeMillis();
        this.return_date = new Date(ms);
        this.return_status = ReturnStatus.PROCESSING;
        // sql to generate courier id
    }
    
    public static void requestReturn(Scanner scn, Connection conn, int product_id, int order_id) {
        if (Courier.assignCourier(conn) == -1) {
            System.out.println("No available couriers for return requests.");
            return;
        }
        System.out.println("[REASON FOR RETURN]\n[1] Damaged Item\n[2] Wrong Item\n[3] Change of Mind\n[4] Counterfeit Item");
        System.out.print("Choice: ");
        String reason = scn.nextLine().trim();
        System.out.print("Return description: ");
        String desc = scn.nextLine();
        
        try {
            String query = 
                """
                INSERT INTO `returns`
                VALUES(?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, order_id);
            ps.setInt(2, product_id);
            ps.setInt(3, Courier.assignCourier(conn));
            ps.setString(5, desc);
            ps.setDate(6, Date.valueOf("9999-12-31"));
            ps.setString(7, ReturnStatus.PROCESSING.name());

            switch (reason) {
                case "1":
                    ps.setString(4, ReturnReason.DAMAGED.getVal());
                    break;
                case "2":
                    ps.setString(4, ReturnReason.WRONG.getVal());
                    break;
                case "3":
                    ps.setString(4, ReturnReason.CHANGEOFMIND.getVal());
                    break;
                case "4":
                    ps.setString(4, ReturnReason.COUNTERFEIT.getVal());
                    break;
                default: System.out.println("Invalid input.");
            }
            
            ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error in requesting return: " + e);
            }
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
