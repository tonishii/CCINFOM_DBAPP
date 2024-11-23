package Model;
import Model.enums.OrderStatus;
import Model.enums.ReturnReason;
import Model.enums.ReturnStatus;

import java.sql.Date;
import java.util.*;
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

    private final Set<OrderContent> shoppingCart = new HashSet<>();

    public User(int user_id, String user_name, String user_firstname, String user_lastname,
                String user_address, String user_phone_number, Date user_creation_date) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_address = user_address;
        this.user_phone_number = user_phone_number;
        this.user_creation_date = user_creation_date;
    }

    public User() {}

    @Override
    public boolean login(int id, Connection conn) throws SQLException {
        String query =
        """
        SELECT user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname
        FROM users
        WHERE user_id = ?
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet result = pstmt.executeQuery();

        if (result.next()) {
            this.user_id = result.getInt("user_id");
            this.user_name = result.getString("user_name");
            this.user_phone_number = result.getString("user_phone_number");
            this.user_address = result.getString("user_address");
            this.user_verified_status = result.getBoolean("user_verified_status");
            this.user_creation_date = result.getDate("user_creation_date");
            this.user_firstname = result.getString("user_firstname");
            this.user_lastname = result.getString("user_lastname");
            return true;
        }
        return false;
    }

    public Map<String, String> browseByShops(Connection conn) throws SQLException {
        Map<String, String> shopOptionList = new LinkedHashMap<>();

        String query = """
        SELECT products.seller_id, sellers.seller_name,
        COUNT(*) AS product_count
        FROM products
        LEFT JOIN sellers ON products.seller_id = sellers.seller_id
        WHERE products.listed_status = 1
        GROUP BY products.seller_id;
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            int sellerId = resultSet.getInt("seller_id");
            String sellerName = resultSet.getString("seller_name");
            int productCount = resultSet.getInt("product_count");
            String displayText = sellerName + " || Number of products: " + productCount;
            shopOptionList.put(displayText, Integer.toString(sellerId));
        }
        return shopOptionList;
    }

    public Map<String, String> browseByProductType(Connection conn) throws SQLException {
        Map<String, String> productTypeOptionList = new LinkedHashMap<>();

        String query =
        """
        SELECT product_type,
        COUNT(*) AS product_count
        FROM products
        WHERE products.listed_status = 1
        GROUP BY product_type;
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            String productType = resultSet.getString("product_type");
            int productCount = resultSet.getInt("product_count");

            String displayText = productType + " || Number of products: " + productCount;
            productTypeOptionList.put(displayText, productType);
        }

        return productTypeOptionList;
    }

    public ArrayList<Product> getSelectedProductList(PreparedStatement pstmt) throws SQLException {
        ArrayList<Product> productList = new ArrayList<>();
        ResultSet resultSet = pstmt.executeQuery();

        if(resultSet.next()) {
            do {
                productList.add(new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getInt("seller_id"),
                        resultSet.getString("product_name"),
                        resultSet.getFloat("product_price"),
                        resultSet.getString("product_type"),
                        resultSet.getFloat("average_rating"),
                        resultSet.getInt("quantity_stocked"),
                        resultSet.getBoolean("listed_status"),
                        resultSet.getString("description")
                ));
            } while (resultSet.next());
        }
        return productList;
    }

    public void updateAccount(Connection conn) throws SQLException {
        String update =
            """
            UPDATE users
            SET user_name = ?,
                user_phone_number = ?,
                user_address = ?,
                user_firstname = ?,
                user_lastname = ?,
                user_verified_status = ?
            WHERE user_id = ?;
            """;
        PreparedStatement pstmt = conn.prepareStatement(update);
        pstmt.setString(1, this.user_name);
        pstmt.setString(2, this.user_phone_number);
        pstmt.setString(3, this.user_address);
        pstmt.setString(4, this.user_firstname);
        pstmt.setString(5, this.user_lastname);
        updateStatus();
        pstmt.setBoolean(6, this.user_verified_status);
        pstmt.setInt(7, this.user_id);

        pstmt.executeUpdate();
    }

    public void updateStatus() {
        this.user_verified_status = this.user_phone_number != null && this.user_address != null;
    }

    public String toString() {
        return "user";
    }

    public void addProductToCart(OrderContent orderContent) {
        shoppingCart.stream()
                    .filter(item -> item.getProductID() == orderContent.getProductID())
                    .findFirst().ifPresent(shoppingCart::remove);

        shoppingCart.add(orderContent);
    }

    public void setName(String user_name) { this.user_name = user_name; }
    public void setFirstName(String user_firstname) { this.user_firstname = user_firstname; }
    public void setLastName(String user_lastname) { this.user_lastname = user_lastname; }
    public void setAddress(String user_address) { this.user_address = user_address; }
    public void setPhoneNumber(String user_phone_number) { this.user_phone_number = user_phone_number; }


    public int getID() { return this.user_id; }
    public Set<OrderContent> getShoppingCart() { return this.shoppingCart; }
    public String getUsername() { return this.user_name; }
    public String getFirstName() { return this.user_firstname; }
    public String getLastName() { return this.user_lastname; }
    public String getAddress() { return this.user_address; }
    public String getPhoneNumber() { return this.user_phone_number; }
    public boolean isVerified() { return this.user_verified_status; }

    public ArrayList<Order> getOrdersView(Connection conn, int user_id) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();

        String query =
                """
                SELECT order_id, courier_id, purchase_date, total_price, order_status, receive_date
                FROM orders
                WHERE user_id = ?
                """;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, user_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            orders.add(new Order(rs.getInt("order_id"), user_id, rs.getInt("courier_id"), rs.getDate("purchase_date"), 
                        rs.getFloat("total_price"), OrderStatus.valueOf(rs.getString("order_status")), rs.getDate("receive_date")));
        }

        return orders;
    }
    
    public ArrayList<Return> getReturnsView(Connection conn, int user_id) throws SQLException {
        ArrayList<Return> returns = new ArrayList<>();

        String query =
                """
                SELECT r.order_id, r.product_id, r.courier_id, r.return_reason, r.return_description, r.return_date, r.return_status
                FROM `returns` r
                JOIN orders o ON r.order_id = o.order_id
                WHERE o.user_id = ?;
                """;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, user_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            returns.add(new Return(rs.getInt("r.order_id"), rs.getInt("r.product_id"), rs.getInt("r.courier_id"), ReturnReason.convertVal(rs.getString("r.return_reason")),
                        rs.getString("r.return_description"), rs.getDate("r.return_date"), ReturnStatus.valueOf(rs.getString("r.return_status"))));
        }

        return returns;
    }
    
    public ArrayList<OrderContent> getOrderItems(Connection conn, int user_id) throws SQLException {
        ArrayList<OrderContent> items = new ArrayList<>();
        
        String query =
                """
                SELECT o.order_id, p.product_id, p.product_name
                FROM orders o
                JOIN order_contents oc ON o.order_id = oc.order_id
                JOIN products p ON oc.product_id = p.product_id
                JOIN sellers s ON s.seller_id = p.seller_id
                WHERE o.user_id = ?;
                """;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, user_id);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            items.add(new OrderContent(rs.getInt("o.order_id"), rs.getInt("p.product_id"), rs.getString("p.product_name")));
        }
        
        return items;
    }
}
