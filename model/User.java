package model;
import enums.OrderStatus;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Account {
    private int    user_id;
    private String  user_name;
    private String  user_firstname;
    private String  user_lastname;

    private String  user_address;
    private String  user_phone_number;
    private Date    user_creation_date;
    private boolean user_verified_status;

    private ArrayList<OrderContent> shoppingCart;

    public User(int user_id, String user_name, String user_firstname, String user_lastname,
                String user_address, String user_phone_number, Date user_creation_date) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_address = user_address;
        this.user_phone_number = user_phone_number;
        this.user_creation_date = user_creation_date;

        this.shoppingCart = new ArrayList<>();
    }

    public User() {}

    @Override
    public boolean login(int id, Connection conn) {
        String query =
        """
        SELECT user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname
        FROM users
        WHERE user_id = ?
        """;

        try {
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

                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during user login: " + e.getMessage());
        }

        return false; // Login failed
    }

    @Override
    public void displayView(Scanner scn, Connection conn) {
        ArrayList<OrderContent> shoppingCart = new ArrayList<>();

        while (true) {
            System.out.print(
            """
            [1] Shopping
            [2] View Shopping Cart (Jericho)
            [3] Receive Order
            [4] Request Return/Refund (Eara)
            [5] Purchase History
            [6] Rate a Product
            [7] Edit Account
            [8] Exit
            Select option:\s""");

            switch (scn.nextLine().trim()) {
                case "2":
                    viewShoppingCart(scn, shoppingCart, conn);
                    break;
                case "3":
                    receiveOrder(scn, conn);
                    break;
                case "4":
                    if (displayPurchaseHistory(conn)) {
                        System.out.print("Enter Order ID: ");
                        int order_id = scn.nextInt();
                        scn.nextLine();
                        Order.displayOrderContents(conn, order_id);
                        System.out.print("Enter Product ID to refund: ");
                        int product_id = scn.nextInt();
                        scn.nextLine();
                        Return.requestReturn(scn, conn, product_id, order_id);
                    }
                    break;
                case "5":
                    displayPurchaseHistory(conn);
                    break;
                case "6":
                    rateProduct(scn, conn);
                    break;
                case "7":

                    break;
                case "8":
                    return;
                default: System.out.println("Error: Enter valid option.");
            }
        }
    }
    
    public boolean displayPurchaseHistory(Connection conn) {
        ArrayList<Order> ph = this.getPurchaseHistory(conn);
        if (!(ph.isEmpty())) {
            System.out.println("Order ID | Courier ID | Date Purchased | Total | Date Received");
            for (Order order : ph) {
                System.out.printf("%d | %d | %s | %f | %s\n", order.getOrderID(), order.getCourierID(), formatDate(order.getPurchaseDate()),
                        order.getTotalPrice(), formatDate(order.getReceiveDate()));
            }
        }
        else {
            System.out.println("No purchase history.");
            return false;
        }
        return true;
    }
    
    private String formatDate(Date date) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public ArrayList<Order> getPurchaseHistory(Connection conn) {
        ArrayList<Order> ph = new ArrayList<>();
        try {
            String query = 
            """
            SELECT order_id, courier_id, purchase_date, total_price, order_status, receive_date
            FROM orders
            WHERE user_id =  ?
            AND order_status = "DELIVERED"
            """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, this.user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ph.add(new Order(rs.getInt("order_id"), this.user_id, rs.getInt("courier_id"), rs.getDate("purchase_date"), 
                        rs.getFloat("total_price"), OrderStatus.valueOf(rs.getString("order_status")), rs.getDate("receive_date")));
            }
        } catch (Exception e) {
            System.out.println("Error while getting purchase history: " + e);
        }
        return ph;
    }

    public Map<String, String> browseByShops(Connection conn) throws SQLException {
        Map<String, String> shopOptionList = new LinkedHashMap<>();

        String query = """
        SELECT products.seller_id, sellers.seller_name,
        COUNT(*) AS product_count,
        SUM(products.quantity_stocked) AS total_quantity
        FROM products
        LEFT JOIN sellers ON products.seller_id = sellers.seller_id
        GROUP BY products.seller_id;
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            int sellerId = resultSet.getInt("seller_id");
            String sellerName = resultSet.getString("seller_name");
            int productCount = resultSet.getInt("product_count");
            int totalQuantity = resultSet.getInt("total_quantity");

            String displayText = "Shop: " + sellerName + " Number of Products: " + productCount + " Total quantity: " + totalQuantity;
            shopOptionList.put(displayText, Integer.toString(sellerId));
        }
        return shopOptionList;
    }

    public Map<String, String> browseByProductType(Connection conn) throws SQLException {
        Map<String, String> productTypeOptionList = new LinkedHashMap<>();

        String query =
        """
        SELECT product_type,
        COUNT(*) AS product_count,
        SUM(quantity_stocked) AS total_quantity
        FROM products
        GROUP BY product_type;
        """;

        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            String productType = resultSet.getString("product_type");
            int productCount = resultSet.getInt("product_count");
            int totalQuantity = resultSet.getInt("total_quantity");

            String displayText = "Product type: " + productType + " Number of Products: " + productCount + " Total quantity: " + totalQuantity;
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

    public void viewShoppingCart(Scanner scn, ArrayList<OrderContent> cart, Connection conn) {
        boolean isCheckout = false;
        label :
        do {
            if (cart.isEmpty()) {
                System.out.println("Cart Empty. Returning...");
                return;
            }

            System.out.println("Product ID | Product Name | Quantity | Total Price");
            for (OrderContent product : cart) {
                System.out.printf("%d | %s | %d | Php %.2f\n", product.getProductID(), product.getProductName(), product.getQuantity(), (product.getPriceEach()*product.getQuantity()) );
            }

            System.out.print("""
            Options:
            [1] Edit Quantity
            [2] Remove Product
            [3] Checkout
            [4] Exit
            Select Option: \s"""
            );

            try {
                int option = Integer.parseInt(scn.nextLine());

                assert (option > 0 && option < 5);
                switch(option) {
                    case 1 -> {
                        System.out.print("Enter Product ID to edit: ");
                        int productID = Integer.parseInt(scn.nextLine());

                        OrderContent product = cart.stream()
                                .filter(o -> o.getProductID() == productID)
                                .findFirst()
                                .orElse(null);

                        assert product != null;
                        String query = """
                        SELECT quantity_stocked
                        FROM products
                        WHERE product_id = ?
                        """;
                        PreparedStatement ps = conn.prepareStatement(query);
                        ps.setInt(1, product.getProductID());
                        ResultSet rs = ps.executeQuery();
                        int maxQty = 0;

                        if (rs.next()) {
                            maxQty = rs.getInt("quantity_stocked");
                        }

                        System.out.printf("Enter desired quantity (Between 0 and %d): ", maxQty);
                        int qty = Integer.parseInt(scn.nextLine());

                        if (qty >= 0 && qty <= maxQty) {
                            if (qty > 0) {
                                cart.get(cart.indexOf(product)).setQuantity(qty);
                                System.out.println("Product quantity edited.");
                            } else {
                                cart.remove(product);
                                System.out.println("Product removed due to zero quantity.");
                            }
                        }
                        else System.out.println("Error: Invalid quantity.");
                    }
                    case 2 -> {
                        System.out.print("Enter Product ID to remove: ");
                        int productID = Integer.parseInt(scn.nextLine());

                        OrderContent product = cart.stream()
                                .filter(o -> o.getProductID() == productID)
                                .findFirst()
                                .orElse(null);

                        cart.remove(product);
                        System.out.println("Product removed.");
                    }
                    case 3 -> {
                        float totalPrice = 0f;
                        for (OrderContent products : cart) {
                            totalPrice += products.getPriceEach() * products.getQuantity();
                        }
                        System.out.printf("Subtotal: Php %.2f\nProceed with checkout? (y/n): ", totalPrice);
                        if (scn.nextLine().trim().equalsIgnoreCase("y")) {
                            Order.generateOrder(this.user_id, conn, cart, totalPrice);
                            cart.clear();
                            isCheckout = true;
                            break label;
                        }
                        else System.out.println("Checkout aborted. Returning...");
                    }
                    case 4 -> {
                        return;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error: Invalid Input");
            } finally {
                if(isCheckout)
                    System.out.println("Checkout successful. Returning to menu...");
                else
                    System.out.print("Do you wish to continue? (y/n): ");
            }

        } while(scn.nextLine().trim().equalsIgnoreCase("y"));
    }

    public void rateProduct(Scanner scn, Connection conn) {
        try {
            ArrayList<Product> rateList = new ArrayList<>();
            int count = 0;
            String query =
                    """
                    SELECT COUNT(DISTINCT product_id) AS numofProducts
                    FROM products p
                    WHERE p.product_id IN (
                                           SELECT product_id
                                           FROM orders o
                                           LEFT JOIN order_contents od ON o.order_id = od. order_id
                                           WHERE o.user_id = ? AND o.order_status = 'DELIVERED' AND od.product_rating IS NULL);
                    """;

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt("numofProducts");
            }
            if (count>0) {
                query =
                        """
                        SELECT DISTINCT *
                        FROM products p
                        WHERE p.product_id IN (
                                               SELECT product_id
                                               FROM orders o
                                               LEFT JOIN order_contents od ON o.order_id = od. order_id
                                               WHERE o.user_id = 3 AND o.order_status = 'DELIVERED' AND od.product_rating IS NULL);
                        """;

                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, user_id);
                resultSet = pstmt.executeQuery();

                while (resultSet.next()) {
                    rateList.add(new Product(
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
                }

                System.out.println("Product ID | Product Name | Product Type");
                for (Product product : rateList) {
                    System.out.printf("%d | %s | %s\n", product.getProductID(), product.getName(), product.getType());
                }

                System.out.print("Enter product ID: ");
                int product_id  = Integer.parseInt(scn.nextLine());

                if (rateList.stream().anyMatch(p -> p.getProductID() == product_id)) {
                    System.out.print("Enter rating (out of 5.0): ");
                    float product_rating = Float.parseFloat(scn.nextLine());
                    String update =
                            """
                                    UPDATE order_contents
                                    SET product_rating = ?
                                    WHERE product_id = ?;
                            """;
                    pstmt = conn.prepareStatement(update);
                    pstmt.setFloat(1, product_rating);
                    pstmt.setInt(2, product_id);
                    pstmt.executeUpdate();
                    Product product = rateList.stream()
                            .filter(p -> p.getProductID() == product_id)
                            .findFirst()
                            .orElse(null);
                    if (product != null) {
                        product.updateRating(conn, product_id);
                    }
                } else {
                    System.out.println("No products found!!!!!!!!!!!!!!!!");
                }
            }
            else{
                System.out.println("No products found!!!!!!!!!!!!!!!!");
            }
        } catch (Exception e) {
            System.out.println("Error while adding product rating: " + e);
        }
    }

    public void receiveOrder(Scanner scn, Connection conn) {
        try {
            String query = """
                            SELECT *
                            FROM orders o
                            WHERE o.user_id = ? AND o.order_status = 'FOR_DELIVERY'
                            ORDER BY o.order_id
                            """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, this.user_id);
            ResultSet rs = ps.executeQuery();

            ArrayList<Order> orderList = new ArrayList<>();

            if(rs.next()) {
                System.out.println("Order ID | Purchase Date");
                do {
                    orderList.add(new Order(rs.getInt("order_id"), this.user_id, rs.getInt("courier_id"), rs.getDate("purchase_date"),
                            rs.getFloat("total_price"), OrderStatus.valueOf(rs.getString("order_status")), rs.getDate("receive_date")));
                    System.out.printf("%d | %s\n", rs.getInt("order_id"), rs.getTimestamp("purchase_date").toString());
                } while(rs.next());

                System.out.println("Enter Order ID of Order to receive: ");
                int id;
                while(true) {
                    id = Integer.parseInt(scn.nextLine());
                    int checkValid = id;
                    if(orderList.stream().anyMatch(o -> o.getOrderID() == checkValid))
                        break;
                    else System.out.println("Error: Please enter a valid Order ID.");
                }

                PreparedStatement pstmt;
                String update =
                """
                UPDATE orders
                SET order_status = 'DELIVERED', receive_date = ?
                WHERE order_id = ?;
                """;
                pstmt = conn.prepareStatement(update);
                pstmt.setTimestamp(1, Timestamp.from(Instant.now()));
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                // the ff lines are for GUI implementation:
//                int fID = id;
//                orderList.remove(orderList.stream().filter(o -> o.getOrderID() == fID).findFirst().get());
                System.out.println("Order received. Returning to menu...");
            }
            else {
                System.out.println("No orders to receive. Returning...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccount(Connection conn){
        try{
            String update =
                    """
                    UPDATE users
                    SET user_name = ?,
                        user_phone_number = ?,
                        user_address = ?,
                        user_firstname = ?,
                        user_lastname = ?
                    WHERE user_id = ?;
                    """;
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setString(1, this.user_name);
            pstmt.setString(2, this.user_phone_number);
            pstmt.setString(3, this.user_address);
            pstmt.setString(4, this.user_firstname);
            pstmt.setString(5, this.user_lastname);
            pstmt.setInt(7, this.user_id);
            updateStatus();
            pstmt.setBoolean(6, this.user_verified_status);
            pstmt.executeUpdate();
        } catch (Exception e){
            System.out.println("Error updating name: " + e);
        }
    }

    public void updateStatus() {
        if (this.user_phone_number != null && this.user_address != null)
            this.user_verified_status = true;
    }

    public String toString() {
        return "user";
    }

    public void addProductToCart(OrderContent orderContent) {
        shoppingCart.add(orderContent);
    }

    public void updateUser(String user_name, String user_firstname, String user_lastname,
                           String user_address, String user_phone_number, Connection conn) throws SQLException {
        this.user_name = user_name;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_address = user_address;
        this.user_phone_number = user_phone_number;

        String query =
        """
        UPDATE users SET user_name = ?, user_firstname = ?, user_lastname = ?, user_address = ?, user_phone_number = ? 
        WHERE user_id = ?
        """;

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, this.user_name);
        preparedStatement.setString(2, this.user_firstname);
        preparedStatement.setString(3, this.user_lastname);
        preparedStatement.setString(4, this.user_address);
        preparedStatement.setString(5, this.user_phone_number);
        preparedStatement.setInt(6, this.user_id);

        preparedStatement.executeUpdate();
    }

    public void setUsername(String user_name) { this.user_name = user_name; }
    public void setFirstName(String user_firstname) { this.user_firstname = user_firstname; }
    public void setLastName(String user_lastname) { this.user_lastname = user_lastname; }
    public void setAddress(String user_address) { this.user_address = user_address; }
    public void setPhoneNumber(String user_phone_number) { this.user_phone_number = user_phone_number; }
    public int getID() { return this.user_id; }
    public String getUsername() { return this.user_name; }
    public String getFirstName() { return this.user_firstname; }
    public String getLastName() { return this.user_lastname; }
    public String getAddress() { return this.user_address; }
    public String getPhoneNumber() { return this.user_phone_number; }
    public Date getCreationDate() { return this.user_creation_date; }
    public boolean getStatus() { return this.user_verified_status; }
}
