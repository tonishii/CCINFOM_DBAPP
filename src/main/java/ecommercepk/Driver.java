package ecommercepk;


import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemaobjects.Account;
import schemaobjects.Courier;
import schemaobjects.Seller;
import schemaobjects.User;

public class Driver {
    // don't mind for now
    public static int register_seller(Scanner scn){
        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = "guycool123";
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status,seller_phone_number, seller_creation_date) VALUES(?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, 1);
            pstmt.setString(2, "john");
            pstmt.setString(3, "john");
            pstmt.setInt(4,1);
            pstmt.setString(5,"john");
            pstmt.setString(6,"2004-2-2");
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return 1;
            
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
     
        return 0;
        
    }
    public static void main(String[] args) {
        Seller scn = new Seller();
        scn.signUp();
    }
}