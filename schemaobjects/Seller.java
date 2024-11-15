package schemaobjects;
import java.sql.*;
import java.util.Scanner;

public class Seller implements Account {
    private int    seller_id;
    private String  seller_name;
    private String  seller_address;
    private String  seller_phone_number;
    private Date    seller_creation_date;
    private boolean seller_verified_status;
    
//    public Seller(long seller_id, String seller_name, String seller_address, 
//            String seller_phone_number, Date seller_creation_date, boolean seller_verified_status) {
//        this.seller_id = seller_id;
//        this.seller_name = seller_name;
//        this.seller_address = seller_address;
//        this.seller_phone_number = seller_phone_number;
//        this.seller_creation_date = seller_creation_date;
//        this.seller_verified_status = seller_verified_status;
//    }

    @Override
    public int login(Scanner scn){
        System.out.print("Enter Account ID: ");
        int id = Integer.parseInt(scn.nextLine());

        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = ""; // put ur password here lol
            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);

            Statement statement = conn.createStatement();
            String query = "select user_id from users";

            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                if(id==result.getInt("user_id")){
                    return 1;
                }
            }
            return 0;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public void signUp(Scanner scn) {
        // Auto generate seller ID
        seller_id = 0;

        System.out.print("Enter seller name: ");
        seller_name  = scn.nextLine();

        System.out.print("Enter seller address:  ");
        seller_address = scn.nextLine();

        System.out.print("Enter phone number:  ");
        seller_phone_number = scn.nextLine();

        long ms = System.currentTimeMillis();
        seller_creation_date = new Date(ms);

        seller_verified_status = false;

        try {
            String url= "jdbc:mysql://localhost:3306/mydb"; // table details
            String username = "root"; // MySQL credentials
            String password = ""; // put ur password here lol

            Connection conn;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);

            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(seller_id) AS Id FROM mydb.sellers");
            ResultSet myRs = pstmt.executeQuery();
            while (myRs.next()) {
                seller_id = myRs.getInt("Id");
            }
            if (seller_id==0){
                pstmt = conn.prepareStatement("INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status,seller_phone_number, seller_creation_date) VALUES(?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, 1);
                pstmt.setString(2, seller_name);
                pstmt.setString(3, seller_address);
                pstmt.setBoolean(4,seller_verified_status);
                pstmt.setString(5,seller_phone_number);
                pstmt.setTimestamp(6,new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();
            }
            else{
                pstmt = conn.prepareStatement("INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status,seller_phone_number, seller_creation_date) VALUES(?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, seller_id+1);
                pstmt.setString(2, seller_name);
                pstmt.setString(3, seller_address);
                pstmt.setBoolean(4,seller_verified_status);
                pstmt.setString(5,seller_phone_number);
                pstmt.setTimestamp(6,new java.sql.Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();
            }

            pstmt.close();
            conn.close();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayView(Scanner scn) {
        while (true) {
            System.out.print(
            "[1] Add Product\n" +
            "[2] Edit Product\n" +
            "[3] Generate Report\n" +
            "[4] Edit Account\n" +
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
                default:
                    break;
            }
        }
    }
    
    public void setName(String seller_name) {
        this.seller_name = seller_name;
    }
    
    public void setAddress(String seller_address) {
        this.seller_address = seller_address;
    }
    
    public void setPhoneNumber(String seller_phone_number) {
        this.seller_phone_number = seller_phone_number;
    }
    
    public void updateStatus() {
        // checking fields
        this.seller_verified_status = true;
    }
    
    public int getID() {
        return this.seller_id;
    }
    
    public String getName() {
        return this.seller_name;
    }
    
    public String getAddress() {
        return this.seller_address;
    }
    
    public String getPhoneNumber() {
        return this.seller_phone_number;
    }
    
    public Date getCreationDate() {
        return this.seller_creation_date;
    }
    
    public boolean getStatus() {
        return this.seller_verified_status;
    }
}
