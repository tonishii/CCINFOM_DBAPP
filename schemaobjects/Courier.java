package schemaobjects;

import java.util.Scanner;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                System.out.println("Welcome! " + courier_name);
                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during courier login: " + e.getMessage());
        }

        return false; // Login failed
    }

    @Override
    public void signUp(Scanner scn, Connection conn) {
        System.out.print("Enter courier name: ");
        courier_name = scn.nextLine();

        System.out.print("Enter email address: ");
        courier_email_address = scn.nextLine();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        // yeah wtf is this
        //
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(courier_email_address);
        while(!matcher.matches()){
            System.out.print("Invalid Email!\n Re-Enter email address:  ");
            courier_email_address = scn.nextLine();
            matcher = pattern.matcher(courier_email_address);
        }

        System.out.print("Enter courier address: ");
        courier_address = scn.nextLine();

        courier_verified_status = false;

        try {
            String query =
            """
            INSERT INTO couriers (courier_id, courier_name, courier_email_address, courier_address, courier_verified_status)
            SELECT IFNULL(MAX(courier_id), 0) + 1, ?, ?, ?, ?
            FROM couriers
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, courier_name);
                pstmt.setString(2, courier_email_address);
                pstmt.setString(3, courier_address);
                pstmt.setBoolean(4, courier_verified_status);

                pstmt.executeUpdate();
            }

            System.out.println("Welcome! " + courier_name);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayView(Scanner scn, Connection conn) {
        while (true) {
            System.out.print(
            """
            [1] Ongoing Orders
            [2] Generate Activity Report
            [3] Edit Account
            [4] Exit
            Select option:\s""");

            switch (scn.nextLine().trim()) {
                case "1" -> showOngoingOrders(conn);

                case "2" -> {

                }

                case "3" -> {
                    boolean goBack = true;
                    while (goBack) {
                        System.out.printf("Courier Details:\n[1] Name: %s\n[2] Email Address: %s\n[3] Address %s\n[4] Go back",
                                courier_name, courier_email_address, courier_email_address);
                        System.out.print("Enter option to edit: ");

                        switch (scn.nextLine()) {
                            case "1":
                                System.out.print("Enter new courier name: ");
                                this.courier_name = scn.nextLine();
                                break;
                            case "2":
                                System.out.print("Enter new email address: ");
                                this.courier_email_address = scn.nextLine();
                                break;
                            case "3":
                                System.out.print("Enter new courier address: ");
                                this.courier_address = scn.nextLine();
                                break;
                            case "4":
                                goBack = false;
                            default:
                                System.out.print("Error: Enter valid option.");
                        }
                    }
                }
                case "4" -> {
                    return;
                }

                default -> System.out.println("Error: Enter valid option.");
            }
        }
    }

    public void showOngoingOrders(Connection conn) {
        try {
            String orderQuery =
            """
            SELECT order_id, user_id, purchase_date, total_price, order_status, receive_date
            FROM orders
            WHERE courier_id = ? AND order_status NOT IN ('Completed', 'Delivered');
            """;

            String returnQuery =
            """
            SELECT r.order_id, r.product_id, r.return_reason, r.return_description, r.return_date, r.return_status
            FROM returns r
            WHERE r.courier_id = ?;
            """;

            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
            ordersStmt.setInt(1, this.courier_id);
            ResultSet ordersResultSet = ordersStmt.executeQuery();

            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
            returnsStmt.setInt(1, this.courier_id);
            ResultSet returnsResultSet = returnsStmt.executeQuery();

            System.out.println("Ongoing Orders:");
            System.out.println("Order ID | User ID | Purchase Date | Total Price | Order Status | Receive Date");
            while (ordersResultSet.next()) {
                int orderId = ordersResultSet.getInt("order_id");
                int userId = ordersResultSet.getInt("user_id");
                Date purchaseDate = ordersResultSet.getDate("purchase_date");
                float totalPrice = ordersResultSet.getFloat("total_price");
                String orderStatus = ordersResultSet.getString("order_status");
                Date receiveDate = ordersResultSet.getDate("receive_date");

                System.out.printf("%d | %d | %s | %f | %s | %s\n",
                orderId, userId, purchaseDate, totalPrice, orderStatus, receiveDate);
            }

            System.out.println("\nOngoing Returns:");
            System.out.println("Order ID | Product ID | Return Reason | Return Description | Return Date | Return Status");
            while (returnsResultSet.next()) {
                int orderId = returnsResultSet.getInt("order_id");
                int productId = returnsResultSet.getInt("product_id");
                String returnReason = returnsResultSet.getString("return_reason");
                String returnDescription = returnsResultSet.getString("return_description");
                Date returnDate = returnsResultSet.getDate("return_date");
                String returnStatus = returnsResultSet.getString("return_status");

                System.out.printf("%d | %d | %s | %s | %s | %s\n",
                orderId, productId, returnReason, returnDescription, returnDate, returnStatus);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void updateStatus() {
        // checking fields
        this.courier_verified_status = true;
    }

    public static int assignCourier(Connection conn) {
        try {
            String query = """
                SELECT courier_pendings.courier_id, SUM(order_count) AS total_orders
                FROM (
                    SELECT o.courier_id, COUNT(o.order_id) AS order_count
                    FROM orders o
                    LEFT JOIN `returns` r ON r.courier_id = o.courier_id
                    GROUP BY o.courier_id
                    UNION
                    SELECT r.courier_id, COUNT(r.order_id) AS order_count
                    FROM `returns` r
                    RIGHT JOIN orders o ON r.courier_id = o.courier_id
                    GROUP BY r.courier_id
                ) courier_pendings
                GROUP BY courier_pendings.courier_id
                ORDER BY total_orders ASC
                LIMIT 1;
                           """;
            // very much not yet tested

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getInt("courier_pendings.courier_id");
        } catch (Exception e) {
            System.out.println("Error in assigning courier: " + e);
        }
        return -1;
    }
    
    public void setName(String courier_name) { this.courier_name = courier_name; }
    public void setEmailAddress(String courier_email_address) { this.courier_email_address = courier_email_address; }
    public void setAddress(String courier_address) { this.courier_address = courier_address; }
    public int getID() { return this.courier_id; }
    public String getName() { return this.courier_name; }
    public String getEmailAddress() { return this.courier_email_address; }
    public String getAddress() { return this.courier_address; }
    public boolean getStatus() { return this.courier_verified_status; }
}
