package model;

import java.sql.*;

public class Product {
    private int     product_id;
    private int     seller_id;

    private String   product_name;
    private float    product_price;
    private String   product_type;

    private float    average_rating;
    private int      quantity_stocked;
    private boolean  listed_status;
    private String   description;

    public Product(int product_id, int seller_id, String product_name, float product_price, String product_type,
                   float average_rating, int quantity_stocked, boolean listed_status, String description) {
        this.product_id = product_id;
        this.seller_id = seller_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_type = product_type;
        this.average_rating = average_rating;
        this.quantity_stocked = quantity_stocked;
        this.listed_status = listed_status;
        this.description = description;
    }

    public Product() {
        average_rating = 0.0f;
    }

    public void sendToDB(Connection conn) {
        try {
            String query =
            """
            INSERT INTO products (product_id, seller_id, product_name, product_price, product_type, average_rating, quantity_stocked, listed_status, description)
            SELECT IFNULL(MAX(product_id), 0) + 1, ?, ?, ?, ?, ?, ?, ?, ?
            FROM products
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, seller_id);
                pstmt.setString(2, product_name);
                pstmt.setFloat(3, product_price);
                pstmt.setString(4, product_type);
                pstmt.setFloat(5, average_rating);
                pstmt.setInt(6, quantity_stocked);
                pstmt.setBoolean(7, listed_status);
                pstmt.setString(8, description);

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error while sending to DB: " + e.getMessage());
        }
    }

    public void printForUser() {
        System.out.printf("%d | %s | %f | %d | %s | %s\n",
        product_id, product_name, product_price, quantity_stocked,
        listed_status ? "Yes" : "No",
        description != null ? description : "N/A");
    }

    public static void updateQuantity(Connection conn, int productId, int qty) {
        try {
            String query = """
                        SELECT quantity_stocked
                        FROM products
                        WHERE product_id = ?
                        """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            int currQty = 0;
            if (rs.next()) {
                currQty = rs.getInt("quantity_stocked");
            }

            String update =
                    """
                    UPDATE products
                    SET quantity_stocked = ?, listed_status = ?
                    WHERE product_id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(update)) {
                pstmt.setInt(1, (currQty - qty));
                pstmt.setBoolean(2, ((currQty - qty) > 0));
                pstmt.setInt(3, productId);

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRating(Connection conn, int productId) throws SQLException {
        String query =
                """
                SELECT AVG(DISTINCT od.product_rating)
                FROM order_contents od
                WHERE od.product_id = 10 AND od.product_rating IS NOT NULL;
                """;
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, productId);
        ResultSet resultSet = pstmt.executeQuery();
        while (resultSet.next()){
            resultSet.getFloat("product_rating");
        }

        query =
                """
                UPDATE products
                SET average_rating = ?
                WHERE product_id = ?;
                """;
        pstmt = conn.prepareStatement(query);
        pstmt.setFloat(1, resultSet.getFloat("product_rating"));
        pstmt.setInt(2, productId);
        pstmt.executeUpdate();
        System.out.println("Rating Success!!");
    }

    @Override
    public String toString() { return product_name + " " + product_price + " " + quantity_stocked; }

    public void updateListedStatus() {
        this.listed_status = this.quantity_stocked != 0;
    }

    public boolean isListed() { return this.listed_status; }
    public void setProductID(int product_id) { this.product_id = product_id; }
    public void setSellerID(int seller_id) { this.seller_id = seller_id; }
    public void setName(String product_name) { this.product_name = product_name; }
    public void setPrice(float product_price) { this.product_price = product_price; }
    public void setType(String product_type) { this.product_type = product_type; }
    public void setQuantity(int quantity_stocked) { this.quantity_stocked = quantity_stocked; }
    public void setDescription(String description) { this.description = description; }
    public int getProductID() { return this.product_id; }
    public int getSellerID() { return this.seller_id; }
    public String getName() { return this.product_name; }
    public float getPrice() { return this.product_price; }
    public String getType() { return this.product_type; }
    public float getRating() { return this.average_rating; }
    public int getQuantity() { return this.quantity_stocked; }
    public String getDescription() { return this.description; }
}
