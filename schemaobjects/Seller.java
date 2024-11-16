package schemaobjects;
import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seller implements Account {
    private int    seller_id;
    private String  seller_name;
    private String  seller_address;
    private String  seller_phone_number;
    private Date    seller_creation_date;
    private boolean seller_verified_status;

    @Override
    public boolean login(Scanner scn, Connection conn){
        System.out.print("Enter Account ID: ");
        int id = Integer.parseInt(scn.nextLine());

        String query =
        "SELECT seller_id, seller_name, seller_address, seller_verified_status, seller_phone_number, seller_creation_date " +
        "FROM sellers WHERE seller_id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            // Execute the query
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                // Retrieve and set seller details
                this.seller_id = result.getInt("seller_id");
                this.seller_name = result.getString("seller_name");
                this.seller_address = result.getString("seller_address");
                this.seller_verified_status = result.getBoolean("seller_verified_status");
                this.seller_phone_number = result.getString("seller_phone_number");
                this.seller_creation_date = result.getDate("seller_creation_date");

                System.out.println("Welcome! " + seller_name);
                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void signUp(Scanner scn, Connection conn) {
        System.out.print("Enter seller name: ");
        seller_name = scn.nextLine();

        System.out.print("Enter seller address: ");
        seller_address = scn.nextLine();

        System.out.print("Enter phone number: ");
        seller_phone_number = scn.nextLine();
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(seller_phone_number);
        while(!matcher.matches()){
            System.out.print("Invalid Phone Number!\nRe-Enter phone number: ");
            seller_phone_number = scn.nextLine();
            matcher = pattern.matcher(seller_phone_number);
        }

        seller_creation_date = new Date(System.currentTimeMillis());
        seller_verified_status = false;

        try {
            String query =
            """
            INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status, seller_phone_number, seller_creation_date)
            SELECT IFNULL(MAX(seller_id), 0) + 1, ?, ?, ?, ?, ?
            FROM sellers
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, seller_name);
                pstmt.setString(2, seller_address);
                pstmt.setBoolean(3, seller_verified_status);
                pstmt.setString(4, seller_phone_number);
                pstmt.setTimestamp(5, new java.sql.Timestamp(seller_creation_date.getTime()));

                pstmt.executeUpdate();
            }

            System.out.println("Welcome! " + seller_name);

        } catch (Exception e) {
            System.out.println("Error during seller sign-up: " + e.getMessage());
        }
    }


    @Override
    public void displayView(Scanner scn, Connection conn) {
        while (true) {
            System.out.print(
            "[1] Add Product\n" +
            "[2] Edit Product\n" +
            "[3] Generate Report\n" +
            "[4] Edit Account\n" +
            "[5] Exit\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    Product product = new Product();
                    product.setSellerID(this.seller_id);

                    System.out.print("Enter product name: ");
                    product.setName(scn.nextLine());

                    System.out.print("Enter product price: ");
                    product.setPrice(scn.nextFloat());
                    scn.nextLine();

                    System.out.print("Enter product type: ");
                    product.setType(scn.nextLine());

                    System.out.print("Enter product quantity: ");
                    product.setQuantity(scn.nextInt());
                    scn.nextLine();

                    product.updateListedStatus();
                    System.out.print("Enter product description: ");
                    product.setDescription(scn.nextLine());

                    product.sendToDB(conn);
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;

                case "5": // Exit
                    return;
                default: System.out.println("Error: Enter valid option.");
            }
        }
    }

    public void updateStatus() {
        // checking fields
        this.seller_verified_status = true;
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
