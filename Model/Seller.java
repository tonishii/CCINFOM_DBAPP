package Model;

import Model.enums.ReturnReason;
import Model.enums.ReturnStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Seller implements Account {
    private int    seller_id;
    private String  seller_name;
    private String  seller_address;
    private String  seller_phone_number;
    private Date    seller_creation_date;
    private boolean seller_verified_status;

    public Seller(int seller_id, String seller_name, String seller_address,
                  String seller_phone_number, Date seller_creation_date, boolean seller_verified_status) {
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.seller_address = seller_address;
        this.seller_phone_number = seller_phone_number;
        this.seller_creation_date = seller_creation_date;
        this.seller_verified_status = seller_verified_status;
    }

    public Seller() {}

    @Override
    public boolean login(int id, Connection conn) throws SQLException {
        String query =
        """
        SELECT seller_id, seller_name, seller_address, seller_verified_status, seller_phone_number, seller_creation_date
        FROM sellers
        WHERE seller_id = ?
        """;

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

            return true;
        }
        return false;
    }

    public void updateStatus() {
        if (this.seller_phone_number != null && this.seller_address != null)
            this.seller_verified_status = true;
    }

    @Override
    public void updateAccount(Connection conn) throws SQLException {
        String update =
            """
            UPDATE sellers
            SET seller_name = ?,
                seller_address = ?,
                seller_phone_number = ?
                seller_verified_status = ?
            WHERE seller_id = ?;
            """;
        PreparedStatement pstmt = conn.prepareStatement(update);
        pstmt.setString(1, this.seller_name);
        pstmt.setString(2, this.seller_address);
        pstmt.setString(3, this.seller_phone_number);
        pstmt.setInt(5, this.seller_id);
        updateStatus();
        pstmt.setBoolean(4, this.seller_verified_status);

        pstmt.executeUpdate();
    }

    public ArrayList<Product> productList(Connection conn) throws SQLException {
        ArrayList<Product> productLists = new ArrayList<>();

        String query = """
        SELECT *
        FROM products p
        WHERE p.seller_id = ?;
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, this.seller_id);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            int prodId = resultSet.getInt("product_id");
            int sellerId = resultSet.getInt("seller_id");
            String seller = resultSet.getString("product_name");
            float price = resultSet.getFloat("product_price");
            String type = resultSet.getString("product_type");
            float rating = resultSet.getFloat("average_rating");
            int quantity = resultSet.getInt("quantity_stocked");
            boolean status = resultSet.getBoolean("listed_status");
            String desc = resultSet.getString("description");
            productLists.add(new Product(prodId,sellerId,seller,price,type,rating,quantity,status,desc));
        }
        return productLists;
    }

    public Map<String,String> refundList(Connection conn) throws SQLException {
        Map<String,String> refundLists = new LinkedHashMap<>();

        String query = """
        SELECT r.order_id, r.product_id, u.user_id, u.user_name, p.product_name
        FROM returns r
        LEFT JOIN products p ON r.product_id = p.product_id
        LEFT JOIN orders o ON r.order_id = o.order_id
        LEFT JOIN users u ON o.user_id = u.user_id
        WHERE p.seller_id = ? AND r.return_status = 'PROCESSING';
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, this.seller_id);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            int orderId = resultSet.getInt("order_id");
            int prodId = resultSet.getInt("product_id");
            int userId = resultSet.getInt("user_id");
            String uName = resultSet.getString("user_name");
            String prodName = resultSet.getString("product_name");

            refundLists.put("Order Id: " + orderId + " Product Id: " + prodId, orderId + " " + prodId);
        }
        return refundLists;
    }

    public Product getSellerProduct(PreparedStatement pstmt) throws SQLException {
        Product product = null;

        ResultSet rstmt = pstmt.executeQuery();

        if (rstmt.next()){
            product = new Product(rstmt.getInt("product_id"),
                    rstmt.getInt("seller_id"),
                    rstmt.getString("product_name"),
                    rstmt.getFloat("product_price"),
                    rstmt.getString("product_type"),
                    rstmt.getFloat("average_rating"),
                    rstmt.getInt("quantity_stocked"),
                    rstmt.getBoolean("listed_status"),
                    rstmt.getString("description"));
        }

        return product;
    }

    public Return getSellerRefund(PreparedStatement pstmt) throws SQLException {
        Return refund = null;

        ResultSet rstmt = pstmt.executeQuery();

        if (rstmt.next()){
            refund = new Return(rstmt.getInt("order_id"),
                    rstmt.getInt("product_id"),
                    rstmt.getInt("courier_id"),
                    ReturnReason.convertVal(rstmt.getString("return_reason")),
                    rstmt.getString("return_description"),
                    rstmt.getDate("return_date"),
                    ReturnStatus.valueOf(rstmt.getString("return_status")));
        }

        return refund;
    }

    @Override
    public String toString() {
        return "seller";
    }

    public void setName(String seller_name) { this.seller_name = seller_name; }
    public void setAddress(String seller_address) { this.seller_address = seller_address; }
    public void setPhoneNumber(String seller_phone_number) { this.seller_phone_number = seller_phone_number; }
    public int getID() { return this.seller_id; }
    public String getName() { return this.seller_name; }
    public String getAddress() { return this.seller_address; }
    public String getPhoneNumber() { return this.seller_phone_number; }
}
