package schemaobjects;

import java.util.Scanner;
import java.sql.*;

public class Courier implements Account {
    private int     courier_id;
    private String   courier_name;
    private String   courier_email_address;
    private String   courier_address;
    private boolean  courier_verified_status;

    @Override
    public boolean login(Scanner scn, Connection conn) {
        System.out.print("Enter Courier Account ID: ");
        int id = Integer.parseInt(scn.nextLine());

        String query =
        "SELECT courier_id, courier_name, courier_email_address, courier_address, courier_verified_status " +
        "FROM couriers WHERE courier_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            // Execute the query
            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
                this.courier_id = result.getInt("courier_id");
                this.courier_name = result.getString("courier_name");
                this.courier_email_address = result.getString("courier_email_address");
                this.courier_address = result.getString("courier_address");
                this.courier_verified_status = result.getBoolean("courier_verified_status");

                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during courier login: " + e.getMessage());
        }

        return false; // Login failed
    }

    @Override
    public void signUp(Scanner scn, Connection conn) {
        // Auto generate courierID
        courier_id = 0;

        System.out.print("Enter courier name: ");
        courier_name = scn.nextLine();

        System.out.print("Enter email address:  ");
        courier_email_address = scn.nextLine();

        System.out.print("Enter courier address: ");
        courier_address = scn.nextLine();

        courier_verified_status = false;

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(courier_id) AS Id FROM mydb.couriers");
            ResultSet myRs = pstmt.executeQuery();
            while (myRs.next()) {
                courier_id = myRs.getInt("Id");
            }
            if (courier_id==0){
                pstmt = conn.prepareStatement("INSERT INTO couriers (courier_id, courier_name, courier_email_address, courier_address, courier_verified_status) VALUES(?, ?, ?, ?, ?)");
                pstmt.setInt(1, 1);
                pstmt.setString(2, courier_name);
                pstmt.setString(3, courier_email_address);
                pstmt.setString(4, courier_address);
                pstmt.setBoolean(5, courier_verified_status);
                pstmt.executeUpdate();
            }
            else{
                pstmt = conn.prepareStatement("INSERT INTO couriers (courier_id, courier_name, courier_email_address, courier_address, courier_verified_status) VALUES(?, ?, ?, ?, ?)");
                pstmt.setInt(1, courier_id+1);
                pstmt.setString(2, courier_name);
                pstmt.setString(3, courier_email_address);
                pstmt.setString(4, courier_address);
                pstmt.setBoolean(5, courier_verified_status);
                pstmt.executeUpdate();
            }

            pstmt.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayView(Scanner scn, Connection conn) {
        while (true) {
            System.out.print(
            "[1] Ongoing Orders\n" +
            "[2] Generate Activity Report\n" +
            "[3] Edit Account\n" +
            "[4] Exit\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    return;
                default:
                    break;
            }
        }
    }
    
    public void setName(String courier_name) {
        this.courier_name = courier_name;
    }
    
    public void setEmailAddress(String courier_email_address) {
        this.courier_email_address = courier_email_address;
    }
    
    public void setAddress(String courier_address) {
        this.courier_address = courier_address;
    }
    
    public void updateStatus() {
        // checking fields
        this.courier_verified_status = true;
    }
    
    public int getID() {
        return this.courier_id;
    }
    
    public String getName() {
        return this.courier_name;
    }
    
    public String getEmailAddress() {
        return this.courier_email_address;
    }
    
    public String getAddress() {
        return this.courier_address;
    }
    
    public boolean getStatus() {
        return this.courier_verified_status;
    }
}
