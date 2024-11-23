package Model;

import Model.enums.OrderStatus;
import Model.enums.ReturnReason;
import Model.enums.ReturnStatus;

import javax.swing.*;
import java.util.ArrayList;
import java.sql.*;

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
    public void updateAccount(Connection conn) throws SQLException {
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
        pstmt.setBoolean(4, !(this.courier_email_address.isEmpty() || this.courier_address.isEmpty()));
        pstmt.setInt(5, this.courier_id);

        pstmt.executeUpdate();
    }

    public ArrayList<Order> showCompletedOrders(Connection conn, int year, int month) {
        ArrayList<Order> orderList = new ArrayList<>();
        try {
            String orderQuery = """
                    SELECT *
                    FROM orders
                    WHERE courier_id = ? AND order_status IN ('Completed', 'Delivered')
                    AND YEAR(receive_date) = ? AND MONTH(receive_date) = ?;
                    """;
            PreparedStatement ordersStmt = conn.prepareStatement(orderQuery);
            ordersStmt.setInt(1, this.courier_id);
            ordersStmt.setInt(2, year);
            ordersStmt.setInt(3, month);
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

    public ArrayList<Return> showCompletedReturns(Connection conn, int year, int month) {
        ArrayList<Return> returns = new ArrayList<>();
        try {
            String returnQuery =
                    """
                    SELECT *
                    FROM returns r
                    WHERE r.courier_id = ? AND r.return_status = 'REFUNDED'
                    AND YEAR(r.return_date) = ? AND MONTH(r.return_date) = ?;
                    """;

            PreparedStatement returnsStmt = conn.prepareStatement(returnQuery);
            returnsStmt.setInt(1, this.courier_id);
            returnsStmt.setInt(2, year);
            returnsStmt.setInt(3, month);
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

    public void deliverOrder (Connection conn, int item) {
        try {
            String query =
            """
            UPDATE orders
            SET order_status = 'FOR_DELIVERY'
            WHERE order_id = ?;
            """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, item);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

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

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in assigning courier: " + e);
        }

        return -1;
    }

    @Override
    public String toString() {
        return "courier";
    }

    public void setName(String courier_name) { this.courier_name = courier_name; }
    public void setEmail(String courier_email_address) { this.courier_email_address = courier_email_address; }
    public void setAddress(String courier_address) { this.courier_address = courier_address; }

    public String getName() { return this.courier_name; }
    public String getEmailAddress() { return this.courier_email_address; }
    public String getAddress() { return this.courier_address; }
}
