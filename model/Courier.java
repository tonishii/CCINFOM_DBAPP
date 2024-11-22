package model;

import enums.OrderStatus;
import enums.ReturnReason;
import enums.ReturnStatus;

import javax.swing.*;
import java.util.ArrayList;
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

    public Courier(int courier_id, String courier_name, String courier_email_address,
                   String courier_address, boolean courier_verified_status) {
        this.courier_id = courier_id;
        this.courier_name = courier_name;
        this.courier_email_address = courier_email_address;
        this.courier_address = courier_address;
        this.courier_verified_status = courier_verified_status;
    }

    public Courier() {}

    @Override
    public boolean login(int id, Connection conn) throws SQLException {
        String query =
        """
        SELECT courier_id, courier_name, courier_email_address, courier_address, courier_verified_status
        FROM couriers
        WHERE courier_id = ?
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);

        ResultSet result = pstmt.executeQuery();
        if (result.next()) {
            this.courier_id = result.getInt("courier_id");
            this.courier_name = result.getString("courier_name");
            this.courier_email_address = result.getString("courier_email_address");
            this.courier_address = result.getString("courier_address");
            this.courier_verified_status = result.getBoolean("courier_verified_status");
            return true;
        }
        return false;
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
                                updateAccount(conn);
                                break;
                            case "2":
                                System.out.print("Enter new email address: ");
                                this.courier_email_address = scn.nextLine();
                                if (!courier_email_address.isEmpty()) {
                                    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                                            "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

                                    Pattern pattern = Pattern.compile(regex);
                                    Matcher matcher = pattern.matcher(courier_email_address);

                                    while (!matcher.matches()) {
                                        System.out.print("Invalid Email!\n Re-Enter email address:  ");
                                        courier_email_address = scn.nextLine();
                                        matcher = pattern.matcher(courier_email_address);
                                    }
                                }
                                updateStatus();
                                updateAccount(conn);
                                break;
                            case "3":
                                System.out.print("Enter new courier address: ");
                                this.courier_address = scn.nextLine();
                                updateStatus();
                                updateAccount(conn);
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

    public ArrayList<Order> showCompletedOrders(Connection conn) {
        ArrayList<Order> orderList = new ArrayList<>();
        try {
            String orderQuery = """
                    SELECT *
                    FROM orders
                    WHERE courier_id = ? AND order_status IN ('Completed', 'Delivered');
                    """;
            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
            ordersStmt.setInt(1, this.courier_id);
            ResultSet rs = ordersStmt.executeQuery();

            while (rs.next()) {
                orderList.add(new Order(rs.getInt("order_id"),
                        this.courier_id,
                        rs.getInt("courier_id"),
                        rs.getDate("purchase_date"),
                        rs.getFloat("total_price"),
                        OrderStatus.valueOf(rs.getString("order_status")),
                        rs.getDate("receive_date")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error.");
        }
        return orderList;
    }

    public ArrayList<Return> showCompletedReturns(Connection conn) {
        ArrayList<Return> returns = new ArrayList<>();
        try {
            String returnQuery =
                    """
                    SELECT *
                    FROM returns r
                    WHERE r.courier_id = ? AND r.return_status = 'REFUNDED';
                    """;

            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
            returnsStmt.setInt(1, this.courier_id);
            ResultSet returnsResultSet = returnsStmt.executeQuery();

            while (returnsResultSet.next()) {
                returns.add(new Return(
                        returnsResultSet.getInt("order_id"),
                        returnsResultSet.getInt("product_id"),
                        returnsResultSet.getInt("courier_id"),
                        ReturnReason.convertVal(returnsResultSet.getString("return_reason")),
                        returnsResultSet.getString("return_description"),
                        returnsResultSet.getDate("return_date"),
                        ReturnStatus.valueOf(returnsResultSet.getString("return_status"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returns;
    }

//    public void generateActivityReportDeprecated(Connection conn) {
//        try {
//            String orderQuery =
//                    """
//                    SELECT order_id, user_id, purchase_date, total_price, order_status, receive_date
//                    FROM orders
//                    WHERE courier_id = ? AND order_status IN ('Completed', 'Delivered');
//                    """;
//
//            String returnQuery =
//                    """
//                    SELECT r.order_id, r.product_id, r.return_reason, r.return_description, r.return_date, r.return_status
//                    FROM returns r
//                    WHERE r.courier_id = ? AND r.return_status = 'REFUNDED';
//                    """;
//
//            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
//            ordersStmt.setInt(1, this.courier_id);
//            ResultSet ordersResultSet = ordersStmt.executeQuery();
//
//            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
//            returnsStmt.setInt(1, this.courier_id);
//            ResultSet returnsResultSet = returnsStmt.executeQuery();
//            System.out.println();
//            if(ordersResultSet.next()) {
//                System.out.println("Completed Orders:");
//                System.out.println("Order ID | User ID | Purchase Date | Total Price | Order Status | Receive Date");
//                do {
//                    int orderId = ordersResultSet.getInt("order_id");
//                    int userId = ordersResultSet.getInt("user_id");
//                    Date purchaseDate = ordersResultSet.getDate("purchase_date");
//                    float totalPrice = ordersResultSet.getFloat("total_price");
//                    String orderStatus = ordersResultSet.getString("order_status");
//                    Date receiveDate = ordersResultSet.getDate("receive_date");
//
//                    System.out.printf("%5d    | %4d    |   %s  | %9.2f   |   %s  |  %s\n",
//                            orderId, userId, purchaseDate, totalPrice, orderStatus, receiveDate);
//                } while (ordersResultSet.next());
//            }
//            else {
//                System.out.println("No completed orders yet.");
//            }
//            System.out.println();
//            if(returnsResultSet.next()) {
//                System.out.println("Completed Returns:");
//                System.out.println("Order ID | Product ID | Return Reason | Return Description | Return Date | Return Status");
//                do {
//                    int orderId = returnsResultSet.getInt("order_id");
//                    int productId = returnsResultSet.getInt("product_id");
//                    String returnReason = returnsResultSet.getString("return_reason");
//                    String returnDescription = returnsResultSet.getString("return_description");
//                    Date returnDate = returnsResultSet.getDate("return_date");
//                    String returnStatus = returnsResultSet.getString("return_status");
//
//                    System.out.printf("%5d    | %6d     | %s | %s | %s | %s\n",
//                            orderId, productId, returnReason, returnDescription, returnDate, returnStatus);
//                } while (returnsResultSet.next());
//            }
//            else {
//                System.out.println("No completed returns yet.");
//            }
//        }
//        catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//    }

    public ArrayList<Order> showOngoingOrders(Connection conn) {
        ArrayList<Order> orderList = new ArrayList<>();
        try {
            String orderQuery = """
                            SELECT *
                            FROM orders
                            WHERE courier_id = ? AND order_status NOT IN ('Completed', 'Delivered');
                    """;
            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
            ordersStmt.setInt(1, this.courier_id);
            ResultSet rs = ordersStmt.executeQuery();

            while (rs.next()) {
                orderList.add(new Order(rs.getInt("order_id"),
                        this.courier_id,
                        rs.getInt("courier_id"),
                        rs.getDate("purchase_date"),
                        rs.getFloat("total_price"),
                        OrderStatus.valueOf(rs.getString("order_status")),
                        rs.getDate("receive_date")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error.");
        }
        return orderList;
    }

    public ArrayList<Return> showOngoingReturns(Connection conn) {
        ArrayList<Return> returns = new ArrayList<>();
        try {
            String returnQuery =
                    """
                    SELECT *
                    FROM returns r
                    WHERE r.courier_id = ?
                    AND r.return_status = 'PROCESSING';
                    """;

            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
            returnsStmt.setInt(1, this.courier_id);
            ResultSet returnsResultSet = returnsStmt.executeQuery();

            while (returnsResultSet.next()) {
                returns.add(new Return(
                    returnsResultSet.getInt("order_id"),
                    returnsResultSet.getInt("product_id"),
                    returnsResultSet.getInt("courier_id"),
                    ReturnReason.convertVal(returnsResultSet.getString("return_reason")),
                    returnsResultSet.getString("return_description"),
                    returnsResultSet.getDate("return_date"),
                    ReturnStatus.valueOf(returnsResultSet.getString("return_status"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returns;
    }

//    public void showOngoingOrdersDeprecated(Connection conn) {
//        try {
//            String orderQuery =
//            """
//            SELECT order_id, user_id, purchase_date, total_price, order_status, receive_date
//            FROM orders
//            WHERE courier_id = ? AND order_status NOT IN ('Completed', 'Delivered');
//            """;
//
//            String returnQuery =
//            """
//            SELECT r.order_id, r.product_id, r.return_reason, r.return_description, r.return_date, r.return_status
//            FROM returns r
//            WHERE r.courier_id = ?;
//            """;
//
//            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
//            ordersStmt.setInt(1, this.courier_id);
//            ResultSet ordersResultSet = ordersStmt.executeQuery();
//
//            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
//            returnsStmt.setInt(1, this.courier_id);
//            ResultSet returnsResultSet = returnsStmt.executeQuery();
//
//            System.out.println("Ongoing Orders:");
//            System.out.println("Order ID | User ID | Purchase Date | Total Price | Order Status | Receive Date");
//            while (ordersResultSet.next()) {
//                int orderId = ordersResultSet.getInt("order_id");
//                int userId = ordersResultSet.getInt("user_id");
//                Date purchaseDate = ordersResultSet.getDate("purchase_date");
//                float totalPrice = ordersResultSet.getFloat("total_price");
//                String orderStatus = ordersResultSet.getString("order_status");
//                Date receiveDate = ordersResultSet.getDate("receive_date");
//
//                System.out.printf("%5d    | %4d    |   %s  | %9.2f  | %s |  %s\n",
//                orderId, userId, purchaseDate, totalPrice, orderStatus, receiveDate);
//            }
//
//            System.out.println("\nOngoing Returns:");
//            System.out.println("Order ID | Product ID | Return Reason | Return Description | Return Date | Return Status");
//            while (returnsResultSet.next()) {
//                int orderId = returnsResultSet.getInt("order_id");
//                int productId = returnsResultSet.getInt("product_id");
//                String returnReason = returnsResultSet.getString("return_reason");
//                String returnDescription = returnsResultSet.getString("return_description");
//                Date returnDate = returnsResultSet.getDate("return_date");
//                String returnStatus = returnsResultSet.getString("return_status");
//
//                System.out.printf("%d | %d | %s | %s | %s | %s\n",
//                orderId, productId, returnReason, returnDescription, returnDate, returnStatus);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//    }

    public void updateStatus() {
        if (this.courier_address != null && this.courier_email_address != null)
            this.courier_verified_status = true;
    }

    public static int assignCourier(Connection conn) {
        try {
            String query = """
                SELECT c.courier_id, IFNULL(SUM(order_count), 0)  AS total_orders
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
                RIGHT JOIN couriers c ON courier_pendings.courier_id = c.courier_id
                WHERE c.courier_verified_status = (1)
                GROUP BY courier_pendings.courier_id, c.courier_id
                ORDER BY total_orders ASC, courier_pendings.courier_id
                LIMIT 1;
                """;

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                return rs.getInt("c.courier_id");
            }

        } catch (Exception e) {
            System.out.println("Error in assigning courier: " + e);
        }
        return -1;
    }

    @Override
    public void updateAccount(Connection conn) {
        try {
            String update =
                    """
                    UPDATE couriers
                    SET courier_name = ?,
                        courier_email_address = ?,
                        courier_address = ?,
                        courier_verified_status = ?
                    WHERE courier_id = ?;
                    """;
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setString(1, this.courier_name);
            pstmt.setString(2, this.courier_email_address);
            pstmt.setString(3, this.courier_address);
            updateStatus();
            pstmt.setBoolean(4, this.courier_verified_status);
            pstmt.setInt(5, this.courier_id);
            pstmt.executeUpdate();
        } catch (Exception e){
            System.out.println("Error updating name: " + e);
        }
    }

    @Override
    public String toString() {
        return "courier";
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
