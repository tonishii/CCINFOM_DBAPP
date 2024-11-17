package schemaobjects;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        """
        SELECT seller_id, seller_name, seller_address, seller_verified_status, seller_phone_number, seller_creation_date
        FROM sellers
        WHERE seller_id = ?
        """;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            ResultSet result = pstmt.executeQuery();
            if (result.next()) {
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

            query =
            """
            SELECT MAX(seller_id)
            FROM sellers
            """;

            try (PreparedStatement selectStmt = conn.prepareStatement(query);
                 ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    this.seller_id = rs.getInt(1);
                }
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
                case "1" -> {
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
                }
                case "2" -> {
                    try {
                        String query =
                                "SELECT product_id, product_name, product_price, product_type, average_rating, quantity_stocked, listed_status, description " +
                                        "FROM products " +
                                        "WHERE seller_id = ?";

                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, this.seller_id);
                        ResultSet resultSet = pstmt.executeQuery();

                        System.out.println("Product ID | Product Name | Product Price | Product Type | Avg. Rating | Quantity | Listed? | Description");

                        while (resultSet.next()) {
                            System.out.printf("%d | %s | %f | %s | %f | %d | %b | %s\n", resultSet.getInt("product_id"), resultSet.getString("product_name"),
                                    resultSet.getFloat("product_price"), resultSet.getString("product_type"), resultSet.getFloat("average_rating"),
                                    resultSet.getInt("quantity_stocked"), resultSet.getBoolean("listed_status"), resultSet.getString("description"));

                        }

                        System.out.print("Enter product ID: ");
                        int id = scn.nextInt();
                        scn.nextLine();

                        query = "SELECT product_name, product_price, product_type, average_rating, quantity_stocked, listed_status, description " +
                                "FROM products " +
                                "WHERE product_id = ?";

                        pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, id);
                        resultSet = pstmt.executeQuery();

                        resultSet.next();
                        Product product = new Product(id, this.seller_id, resultSet.getString("product_name"),
                                resultSet.getFloat("product_price"), resultSet.getString("product_type"), resultSet.getFloat("average_rating"),
                                resultSet.getInt("quantity_stocked"), resultSet.getBoolean("listed_status"), resultSet.getString("description"));

                        System.out.println("Which field to edit?\n[1] Product Name\n[2] Product Price\n[3] Product Type");
                        System.out.print("[4] Quantity\n[5] Description\nChoice: ");

                        switch (scn.nextLine().trim()) {
                            case "1":
                                System.out.print("Enter product name: ");
                                product.setName(scn.nextLine());
                                break;
                            case "2":
                                System.out.print("Enter product price: ");
                                product.setPrice(scn.nextFloat());
                                scn.nextLine();
                                break;
                            case "3":
                                System.out.print("Enter product type: ");
                                product.setType(scn.nextLine());
                                break;
                            case "4":
                                System.out.print("Enter product quantity: ");
                                product.setQuantity(scn.nextInt());
                                scn.nextLine();
                                product.updateListedStatus();
                                break;
                            case "5":
                                System.out.print("Enter product description: ");
                                product.setDescription(scn.nextLine());
                                break;
                            default:
                                System.out.println("Invalid input.");
                        }

                        query = "REPLACE INTO products(product_id, seller_id, product_name, product_price, product_type, average_rating, quantity_stocked, listed_status, description) "
                                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, product.getProductID());
                        pstmt.setInt(2, product.getSellerID());
                        pstmt.setString(3, product.getName());
                        pstmt.setFloat(4, product.getPrice());
                        pstmt.setString(5, product.getType());
                        pstmt.setFloat(6, product.getRating());
                        pstmt.setInt(7, product.getQuantity());
                        pstmt.setBoolean(8, product.isListed());
                        pstmt.setString(9, product.getDescription());

                        pstmt.executeUpdate();

                    } catch (Exception e) {
                        System.out.println("Error during product editing: " + e.getMessage());
                    }
                }
                case "3" -> {
                    System.out.print("\n[GENERATE REPORT]\n[1] Sales Report\n[2] Credibility Report\n[3] Product Popularity Report\nChoice: ");
                    switch (scn.nextLine().trim()) {
                        case "1":
                            break;
                        case "2":
                            break;
                        case "3":
                            try {
                                int month, year;

                                System.out.print("Enter month: ");
                                month = scn.nextInt();
                                scn.nextLine();
                                System.out.print("Enter year: ");
                                year = scn.nextInt();
                                scn.nextLine();

                                String query = "SELECT p.product_name, SUM(oc.item_quantity) AS amt_sold, p.average_rating " +
                                        "FROM order_contents oc " +
                                        "JOIN orders o ON oc.order_id = o.order_id " +
                                        "JOIN products p ON oc.product_id = p.product_id " +
                                        "WHERE YEAR(o.purchase_date) = ? " +
                                        "AND MONTH(o.purchase_date) = ? " +
                                        "AND p.seller_id = ? " +
                                        "GROUP BY p.product_name, p.average_rating " +
                                        "ORDER BY p.average_rating DESC, p.product_name";

                                PreparedStatement ps = conn.prepareStatement(query);
                                ps.setInt(1, year);
                                ps.setInt(2, month);
                                ps.setInt(3, this.seller_id);
                                ResultSet rs = ps.executeQuery();

                                System.out.println("Product Name | Units Sold | Average Rating");
                                int rank = 1;
                                while (rs.next()) {
                                    System.out.println("[RANK " + rank + "]\t" + rs.getString("p.product_name") + " | " + rs.getInt("amt_sold") + " | " + rs.getFloat("p.average_rating"));
                                }
                            } catch (Exception e) {
                                System.out.println("Error during report generation: " + e);
                            }
                            break;
                        default:
                            System.out.println("Invalid input.");
                    }
                }
                case "4" -> {
                    break;
                }
                case "5" -> {
                    return;
                } // Exit
                default -> System.out.println("Error: Enter valid option.");
            }
        }
    }

    public void updateStatus() {
        // checking fields
        this.seller_verified_status = true;
    }

    public void setName(String seller_name) { this.seller_name = seller_name; }
    public void setAddress(String seller_address) { this.seller_address = seller_address; }
    public void setPhoneNumber(String seller_phone_number) { this.seller_phone_number = seller_phone_number; }
    public int getID() { return this.seller_id; }
    public String getName() { return this.seller_name; }
    public String getAddress() { return this.seller_address; }
    public String getPhoneNumber() { return this.seller_phone_number; }
    public Date getCreationDate() { return this.seller_creation_date; }
    public boolean getStatus() { return this.seller_verified_status; }
}
