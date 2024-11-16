package schemaobjects;
import java.util.Scanner;
import java.sql.*;

public class User implements Account {
    private int    user_id;
    private String  user_name;
    private String  user_firstname;
    private String  user_lastname;

    private String  user_address;
    private String  user_phone_number;
    private Date    user_creation_date;
    private boolean user_verified_status;

    @Override
    public boolean login(Scanner scn, Connection conn) {
        System.out.print("Enter User Account ID: ");
        int id = Integer.parseInt(scn.nextLine());

        // SQL query to fetch the user by user_id
        String query =
        "SELECT user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname " +
        "FROM users WHERE user_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            // Execute the query
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                // Retrieve user details (if needed, you can populate class attributes here)
                this.user_id = result.getInt("user_id");
                this.user_name = result.getString("user_name");
                this.user_phone_number = result.getString("user_phone_number");
                this.user_address = result.getString("user_address");
                this.user_verified_status = result.getBoolean("user_verified_status");
                this.user_creation_date = result.getDate("user_creation_date");
                this.user_firstname = result.getString("user_firstname");
                this.user_lastname = result.getString("user_lastname");

                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during user login: " + e.getMessage());
        }

        return false; // Login failed
    }

    @Override
    public void signUp(Scanner scn, Connection conn) {
        // Auto generate user ID
        user_id = 0;

        System.out.print("Enter user account name: ");
        user_name  = scn.nextLine();

        System.out.print("Enter user first name: ");
        user_firstname = scn.nextLine();

        System.out.print("Enter user last  name: ");
        user_lastname = scn.nextLine();

        System.out.print("Enter user address:  ");
        user_address = scn.nextLine();

        System.out.print("Enter phone number:  ");
        user_phone_number = scn.nextLine();

        long ms = System.currentTimeMillis();
        user_creation_date = new Date(ms);
        
        user_verified_status = false;

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(user_id) AS Id FROM mydb.users");
            ResultSet myRs = pstmt.executeQuery();
            while (myRs.next()) {
                user_id = myRs.getInt("Id");
            }
            if (user_id==0){
                pstmt = conn.prepareStatement("INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, 1);
                pstmt.setString(2, user_name);
                pstmt.setString(3, user_phone_number);
                pstmt.setString(4, user_address);
                pstmt.setBoolean(5, user_verified_status);
                pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.setString(7, user_firstname);
                pstmt.setString(8, user_lastname);
                pstmt.executeUpdate();
            } else {
                pstmt = conn.prepareStatement("INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, user_id+1);
                pstmt.setString(2, user_name);
                pstmt.setString(3, user_phone_number);
                pstmt.setString(4, user_address);
                pstmt.setBoolean(5, user_verified_status);
                pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.setString(7, user_firstname);
                pstmt.setString(8, user_lastname);
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
            "[1] Shopping (Anthony)\n" +
            "[2] View Shopping Cart (Jericho)\n" +
            "[3] Receive Order\n" +
            "[4] Request Return/Refund (Eara)\n" +
            "[5] Purchase History\n" +
            "[6] Rate a Product\n" +
            "[7] Edit Account\n" +
            "[8] Exit\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    break;
                case "8":
                    return;
                default:
                    break;
            }
        }
    }
    
    public void setUsername(String user_name) {
        this.user_name = user_name;
    }
    
    public void setFirstName(String user_firstname) {
        this.user_firstname = user_firstname;
    }
    
    public void setLastName(String user_lastname) {
        this.user_lastname = user_lastname;
    }
    
    public void setAddress(String user_address) {
        this.user_address = user_address;
    }
    
    public void setPhoneNumber(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }
    
    public void updateStatus() {
        // checking fields
        this.user_verified_status = true;
    }

    public int getID() {
        return this.user_id;
    }
    
    public String getUsername() {
        return this.user_name;
    }
    
    public String getFirstName() {
        return this.user_firstname;
    }
    
    public String getLastName() {
        return this.user_lastname;
    }
    
    public String getAddress() {
        return this.user_address;
    }
    
    public String getPhoneNumber() {
        return this.user_phone_number;
    }
    
    public Date getCreationDate() {
        return this.user_creation_date;
    }
    
    public boolean getStatus() {
        return this.user_verified_status;
    }
}
