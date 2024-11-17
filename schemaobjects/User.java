package schemaobjects;
import enums.OrderStatus;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
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

    @Override
    public boolean login(Scanner scn, Connection conn) {
        System.out.print("Enter User Account ID: ");
        int id = Integer.parseInt(scn.nextLine());

        // SQL query to fetch the user by user_id
        String query =
        """
        SELECT user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname
        FROM users
        WHERE user_id = ?
        """;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);

            // Execute the query
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

                System.out.println("Welcome! " + user_name);
                return true; // Login successful
            }

        } catch (Exception e) {
            System.out.println("Error during user login: " + e.getMessage());
        }

        return false; // Login failed
    }

    @Override
    public void signUp(Scanner scn, Connection conn) {
        System.out.print("Enter user account name: ");
        user_name  = scn.nextLine();

        System.out.print("Enter user first name: ");
        user_firstname = scn.nextLine();

        System.out.print("Enter user last name: ");
        user_lastname = scn.nextLine();

        System.out.print("Enter user address:  ");
        user_address = scn.nextLine();

        System.out.print("Enter phone number:  ");
        user_phone_number = scn.nextLine();
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(user_phone_number);

        while(!matcher.matches()){
            System.out.print("Invalid Phone Number!\nRe-Enter phone number: ");
            user_phone_number = scn.nextLine();
            matcher = pattern.matcher(user_phone_number);
        }

        user_creation_date = new Date(System.currentTimeMillis());
        user_verified_status = false;

        try {
            String query =
            """
            INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname)
            SELECT IFNULL(MAX(user_id), 0) + 1, ?, ?, ?, ?, ?, ?, ?
            FROM users
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, user_name);
                pstmt.setString(2, user_phone_number);
                pstmt.setString(3, user_address);
                pstmt.setBoolean(4, user_verified_status);
                pstmt.setTimestamp(5, new java.sql.Timestamp(user_creation_date.getTime()));
                pstmt.setString(6, user_firstname);
                pstmt.setString(7, user_lastname);

                pstmt.executeUpdate();
            }

            query =
            """
            SELECT MAX(user_id)
            FROM users
            """;

            try (PreparedStatement selectStmt = conn.prepareStatement(query);
                 ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    this.user_id = rs.getInt(1);
                }
            }

            System.out.println("Welcome! " + user_name);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayView(Scanner scn, Connection conn) {
        ArrayList<OrderContent> shoppingCart = new ArrayList<>();

        while (true) {
            System.out.print(
            "[1] Shopping\n" +
            "[2] View Shopping Cart (Jericho)\n" +
            "[3] Receive Order\n" +
            "[4] Request Return/Refund (Eara)\n" +
            "[5] Purchase History\n" +
            "[6] Rate a Product\n" +
            "[7] Edit Account\n" +
            "[8] Exit\n" +
            "Select option: ");

            switch (scn.nextLine().trim()) {
                case "1":
                    shoppingCart.addAll(browseOptions(scn, conn));
                    break;
                case "2":
                    viewShoppingCart(scn, shoppingCart, conn);
                    break;
                case "3":
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

    private ArrayList<OrderContent> browseOptions(Scanner scn, Connection conn) {
        ArrayList<Product> productList = new ArrayList<>();
        ArrayList<OrderContent> shoppingCart = new ArrayList<>();

        try {
            while (true) {
                System.out.print("""
                Browse by:
                [1] Shops
                [2] Product Type
                [3] Go back
                Select option:\s""");

                // Get the list of products to choose from
                switch (scn.nextLine().trim()) {
                    case "1" -> browseByShops(scn, conn, productList);
                    case "2" -> browseByProductType(scn, conn, productList);
                    case "3" -> { return shoppingCart; }
                    default -> System.out.println("Error: Enter a valid option.");
                }

                if (productList.isEmpty()) {
                    System.out.println("No products found for the selected product type.");
                    continue;
                }

                // Select a product to add to the cart
                do {
                    for (Product product : productList) {
                        product.printForUser();
                    }

                    System.out.print("Enter the Product ID to order: ");
                    int selectedProductId = scn.nextInt();
                    scn.nextLine();

                    Product selectedProduct = productList.stream()
                            .filter(product -> product.getProductID() == selectedProductId)
                            .findFirst()
                            .orElse(null);

                    assert selectedProduct != null;
                    if (selectedProduct.isListed()) {
                        while (true) {
                            System.out.print("Enter order quantity: ");
                            int orderQuantity = scn.nextInt();
                            scn.nextLine();

                            if (orderQuantity <= selectedProduct.getQuantity()) {
                                shoppingCart.add(new OrderContent(selectedProductId, selectedProduct.getName(),orderQuantity, selectedProduct.getPrice()));
                                break;
                            } else {
                                System.out.println("Error: Only " + selectedProduct.getQuantity() + " are in stock");
                            }
                        }
                    }

                    // Gives option to go back to the select browse by options
                    System.out.print("Go back? y/n: ");
                } while (!scn.nextLine().trim().equalsIgnoreCase("y"));
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        return shoppingCart;
    }

    private void browseByShops(Scanner scn, Connection conn, ArrayList<Product> productList) throws SQLException {
        productList.clear();
        ArrayList<Integer> sellerIDList = new ArrayList<>();

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

        System.out.println("Seller ID | Seller Name | Product Count | Total Quantity");

        while (resultSet.next()) {
            int sellerId = resultSet.getInt("seller_id");
            String sellerName = resultSet.getString("seller_name");
            int productCount = resultSet.getInt("product_count");
            int totalQuantity = resultSet.getInt("total_quantity");
            System.out.printf("%d | %s | %d | %d\n", sellerId, sellerName, productCount, totalQuantity);
            sellerIDList.add(sellerId);
        }

        int selectedSellerID;
        while (true) {
            System.out.print("Select seller ID: ");
            selectedSellerID = scn.nextInt();
            scn.nextLine();

            int finalSelectedSellerID = selectedSellerID;
            if (sellerIDList.stream().anyMatch(sellerID -> sellerID == finalSelectedSellerID)) {
                break;
            } else {
                System.out.println("Error: Enter valid product type.");
            }
        }

        query =
        """
        SELECT product_id, product_name, product_price, product_type, average_rating, quantity_stocked, listed_status, description
        FROM products
        WHERE seller_id = ?;
        """;

        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, selectedSellerID);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            productList.add(new Product(
            resultSet.getInt("product_id"),
            selectedSellerID,
            resultSet.getString("product_name"),
            resultSet.getFloat("product_price"),
            resultSet.getString("product_type"),
            resultSet.getFloat("average_rating"),
            resultSet.getInt("quantity_stocked"),
            resultSet.getBoolean("listed_status"),
            resultSet.getString("description")
            ));
        }
    }

    private void browseByProductType(Scanner scn, Connection conn, ArrayList<Product> productList) throws SQLException {
        productList.clear();
        ArrayList<String> productTypeList = new ArrayList<>();

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

        System.out.println("Product Type | Product Count | Total Quantity");
        while (resultSet.next()) {
            String productType = resultSet.getString("product_type");
            int productCount = resultSet.getInt("product_count");
            int totalQuantity = resultSet.getInt("total_quantity");
            System.out.println(productType + " | " + productCount + " | " + totalQuantity);

            productTypeList.add(productType);
        }

        String selectedProductType;
        while (true) {
            System.out.print("Select product type: ");
            selectedProductType = scn.nextLine().trim();

            String finalSelectedProductType = selectedProductType;
            if (productTypeList.stream().anyMatch(productType -> Objects.equals(productType, finalSelectedProductType))) {
                break;
            } else {
                System.out.println("Error: Enter valid product type.");
            }
        }

        query =
        """
        SELECT product_id, seller_id, product_name, product_price, average_rating, quantity_stocked, listed_status, description
        FROM products
        WHERE product_type = ?;
        """;

        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, selectedProductType);
        resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            productList.add(new Product(
            resultSet.getInt("product_id"),
            resultSet.getInt("seller_id"),
            resultSet.getString("product_name"),
            resultSet.getFloat("product_price"),
            selectedProductType,
            resultSet.getFloat("average_rating"),
            resultSet.getInt("quantity_stocked"),
            resultSet.getBoolean("listed_status"),
            resultSet.getString("description")
            ));
        }
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
            ArrayList<Product> rateList = new ArrayList<Product>();
            int count = 0;
            String query =
                    """
                    SELECT COUNT(product_id) AS numofProducts
                    FROM products p
                    WHERE p.product_id IN (
                                            SELECT product_id
                            				FROM orders o
                                            LEFT JOIN order_contents od ON o.order_id = od. order_id
                                            WHERE o.user_id = ? AND o.order_status = 'DELIVERED');
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
                                SELECT p.product_id, p.product_name, p.product_type, p.average_rating
                                FROM products p
                                WHERE p.product_id IN (
                                                        SELECT product_id
                                                        FROM orders o
                                                        LEFT JOIN order_contents od ON o.order_id = od. order_id
                                                        WHERE o.user_id = ? AND o.order_status = 'DELIVERED');
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
                    System.out.printf("%d | %s | %s\n", product.getProductID(), product.getType());
                }

                System.out.print("Enter product ID: ");
                int product_id  = Integer.parseInt(scn.nextLine())-1;
                if (product_id>rateList.size()-1){

                }
                Product product = rateList.stream()
                        .filter(p -> p.getProductID() == product_id)
                        .findFirst()
                        .orElse(null);

                System.out.print("Enter rating (out of 5.0): ");
                float product_rating= Float.parseFloat(scn.nextLine());
            }
            else{
                System.out.println("No products found!!!!!!!!!!!!!!!!");
            }
        } catch (Exception e) {
            System.out.println("Error while adding product rating: " + e);
        }
    }

    public void updateStatus() {
        // checking fields
        this.user_verified_status = true;
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
