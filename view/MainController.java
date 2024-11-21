package view;

import model.*;
import enums.OrderStatus;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    private final MainFrame mainMenuPage;
    private final SQLConnect connectPage;
    private final SelectAccount selectAccountPage;

    private final UserPage userPage;
    private final SellerPage sellerPage;
    private final CourierPage courierPage;

    private Connection conn;
    private Account account;

    public MainController() {
        mainMenuPage = new MainFrame();

        connectPage = new SQLConnect();
        mainMenuPage.addToCenterPanel(connectPage, MainFrame.CONNECTPAGE);
        initConnectionListeners();

        selectAccountPage = new SelectAccount();
        mainMenuPage.addToCenterPanel(selectAccountPage, MainFrame.SELECTACCPAGE);
        initSelectListeners();

        userPage = new UserPage();
        mainMenuPage.addToCenterPanel(userPage, MainFrame.USERPAGE);
        initUserListeners();

        sellerPage = new SellerPage();
        mainMenuPage.addToCenterPanel(sellerPage, MainFrame.SELLERPAGE);
        initSellerListeners();

        courierPage = new CourierPage();
        mainMenuPage.addToCenterPanel(courierPage, MainFrame.COURIERPAGE);
        initCourierListeners();

        mainMenuPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    e.getWindow().dispose();
                }
                e.getWindow().dispose();
            }
        });
    }

    private void initConnectionListeners() {
        connectPage.initConnListeners(submitEvent -> {
            try {
                String url = "jdbc:mysql://localhost:3306/mydb";
                String username = connectPage.getUsername();
                String password = connectPage.getPassword();

                Class.forName("com.mysql.cj.jdbc.Driver");
                this.conn = DriverManager.getConnection(url, username, password);

                this.selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
                this.mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, exitEvent -> {
            try {
                if (conn != null) {
                    this.conn.close();
                }
                System.exit(0);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }

    private void initSelectListeners() {
        selectAccountPage.initSelectListeners(loginEvent -> {
            this.account = selectAccountPage.getAccountType();
            selectAccountPage.nextPageName(SelectAccount.LOGINPAGE);

        }, signUpEvent -> {
            this.account = selectAccountPage.getAccountType();

            (account instanceof User ? userPage :
            account instanceof Seller ? sellerPage :
            courierPage).nextPageName(AccountPage.SIGNUPPAGE);

            mainMenuPage.nextPageName(account.toString());

        }, submitLoginEvent -> {

            try {
                account.login(Integer.parseInt(selectAccountPage.getID()), conn);
                (account instanceof User ? userPage :
                account instanceof Seller ? sellerPage :
                courierPage).nextPageName(AccountPage.MAINPAGE);
                mainMenuPage.nextPageName(account.toString());

                if (account instanceof User) {
                    userPage.updateBrowseList(((User) account).browseByShops(conn));
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Enter valid ID.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, backLoginEvent -> selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE)
        , backEvent -> {
            try {
                connectPage.clearTextFields();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error:");
            }
            mainMenuPage.nextPageName(MainFrame.CONNECTPAGE);
        });
    }

    private void initUserListeners() {
        userPage.initSignUpListeners(submitSignUpEvent -> { // Action: Pressing the submit button in the sign-up page
            String user_name = userPage.getUserName();
            String user_firstname = userPage.getUserFirstName();
            String user_lastname = userPage.getUserLastName();
            String user_address = userPage.getUserAddress();

            String user_phone_number = userPage.getUserPhone();
            
            if (user_name.isEmpty() || user_firstname.isEmpty() || user_lastname.isEmpty())
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nUsername\nFirst Name\nLast Name");
            else {
                if (!user_phone_number.isEmpty()) {
                    Pattern pattern = Pattern.compile("^\\d{11}$");
                    Matcher matcher = pattern.matcher(user_phone_number);

                    while (!matcher.matches()) {
                        JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                        user_phone_number = userPage.getUserPhone();
                        matcher = pattern.matcher(user_phone_number);
                    }
                }

                Date user_creation_date = new Date(System.currentTimeMillis());
                boolean user_verified_status = false;

                if (!user_phone_number.isEmpty() && !user_address.isEmpty())
                    user_verified_status = true;

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

                    int user_id = 1;
                    try (PreparedStatement selectStmt = conn.prepareStatement(query);
                         ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            user_id = rs.getInt(1);
                        }
                    }

                    this.account = new User(user_id, user_name, user_firstname, user_lastname, user_address, user_phone_number, user_creation_date);
                    userPage.nextPageName(AccountPage.MAINPAGE);

                } catch (SQLException e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            }
        }, backSignUpEvent -> { // Action: Pressing the back button in the sign-up page
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            userPage.nextPageName(AccountPage.SIGNUPPAGE);
        });

        userPage.initMainListeners(shopEvent -> { // Action: Pressing shop button
            userPage.nextMainPageName(UserPage.SHOPPAGE);

            // Contains the mapping of the displayed text and
            // the actual primary key used to identify the selected option
            Map<String, String> options = new LinkedHashMap<>();

            try {
                // Check which browse by option was selected and
                // Get all the options that the user can pick from
                if (userPage.getBrowseByOption().equals("By shop")) {
                    options.putAll(((User) account).browseByShops(conn));
                } else if (userPage.getBrowseByOption().equals("By product type")) {
                    options.putAll(((User) account).browseByProductType(conn));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

            // Update the JList for with the options
            userPage.updateBrowseList(options);

        }, cartEvent -> { // Action: Pressing cart button
            userPage.nextMainPageName(UserPage.CARTPAGE);
            userPage.updateCartTable(((User) account).getShoppingCart());

        }, ordersEvent -> { // Action: Pressing orders button
            userPage.nextMainPageName(UserPage.ORDERSPAGE);

            try {
                // Check which category was selected and
                // Get all the options that the user can pick from
                if (userPage.getOrdersViewOption().equals("Orders")) {
                    ArrayList<Order> orderList = ((User) account).getOrdersView(conn, ((User) account).getID());
                    userPage.updateOrdersList(orderList);
                } else if (userPage.getOrdersViewOption().equals("Returns")) {
                    ArrayList<Return> returnList = ((User) account).getReturnsView(conn, ((User) account).getID());
                    userPage.updateReturnsList(returnList);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }, ordersViewChangeEvent -> { // Action: Changing the list view in orders
            try {
                // Check which category was selected and
                // Get all the options that the user can pick from
                if (userPage.getOrdersViewOption().equals("Orders")) {
                    ArrayList<Order> orderList = ((User) account).getOrdersView(conn, ((User) account).getID());
                    userPage.updateOrdersList(orderList);
                } else if (userPage.getOrdersViewOption().equals("Returns")) {
                    ArrayList<Return> returnList = ((User) account).getReturnsView(conn, ((User) account).getID());
                    userPage.updateReturnsList(returnList);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
            
        }, profileEvent -> { // Action: Pressing profile button
            userPage.nextMainPageName(UserPage.PROFILEPAGE);
            userPage.updateProfilePage((User) account);

        }, logOutEvent -> { // Action: Pressing log out button
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            userPage.nextPageName(UserPage.SHOPPAGE);

        }, addToCartEvent -> { // Action: Pressing the add button in the shop page
            User user = (User) account;
            Product selectedProduct = userPage.getSelectedProduct();

            if (selectedProduct.isListed()) {
                // Show a pop-up asking for the order quantity
                int orderQuantity = userPage.getQuantity();

                if (orderQuantity == 0) {
                    return;
                }

                if (orderQuantity <= selectedProduct.getQuantity()) {
                    // Add to cart
                    user.addProductToCart(new OrderContent(selectedProduct.getProductID(), selectedProduct.getName(),orderQuantity, selectedProduct.getPrice()));
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Only " + selectedProduct.getQuantity() + " are in stock.");
                }
            }

        }, browseChangeEvent -> { // Action: Changing the browse option in the shop page

            // See shopEvent for explanation
            Map<String, String> options = new LinkedHashMap<>();

            try {
                if (userPage.getBrowseByOption().equals("By shop")) {
                    options.putAll(((User) account).browseByShops(conn));
                } else if (userPage.getBrowseByOption().equals("By product type")) {
                    options.putAll(((User) account).browseByProductType(conn));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
            userPage.updateBrowseList(options);
        }, checkOutEvent -> { // Action: Pressing check out button in the shopping cart page
            ArrayList<OrderContent> selectedProducts = userPage.getSelectedRecords();

            float totalPrice = 0f;

            for (OrderContent product : selectedProducts) {
                totalPrice += product.getPriceEach() * product.getQuantity();
            }

            int choice = JOptionPane.showConfirmDialog(null,
    "Total: " + totalPrice + " PHP. Proceed? ", "Checkout", JOptionPane.YES_NO_OPTION);

            try {
                if (choice == JOptionPane.OK_OPTION) {
                    Set<OrderContent> shoppingCart = ((User) account).getShoppingCart();

                    String query =
                    """
                    SELECT IFNULL(MAX(order_id), 0) + 1 AS id
                    FROM orders
                    """;

                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    int order_id = 1;
                    if (rs.next()) {
                        order_id = rs.getInt("id");
                    }

                    Order order = new Order(
                        order_id,
                        ((User) account).getID(),
                        Courier.assignCourier(conn),
                        new Date(System.currentTimeMillis()),
                        totalPrice,
                        OrderStatus.BEING_PREPARED,
                        Date.valueOf("9999-12-31"));

                    order.sendToDB(conn, shoppingCart);

                    // Remove all selected products in the shopping cart
                    shoppingCart.removeIf(cartProduct ->
                    selectedProducts.stream()
                    .anyMatch(selectedProduct -> selectedProduct.getProductID() == cartProduct.getProductID()));

                    // Update in the page
                    userPage.updateCartTable(shoppingCart);

                } else {
                    JOptionPane.showMessageDialog(null, "Aborting Checkout...");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, removeItemEvent -> { // Action: Pressing the remove item button in the shopping cart page
            ArrayList<OrderContent> selectedProducts = userPage.getSelectedRecords();

            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove items", JOptionPane.YES_NO_OPTION);

            Set<OrderContent> shoppingCart = ((User) account).getShoppingCart();
            if (choice == JOptionPane.OK_OPTION) {
                ((User) account).getShoppingCart().removeIf(cartProduct ->
                selectedProducts.stream()
                .anyMatch(selectedProduct -> selectedProduct.getProductID() == cartProduct.getProductID()));
            } else {
                JOptionPane.showMessageDialog(null, "Aborting remove...");
            }

            // Update in the page
            userPage.updateCartTable(shoppingCart);
            
        }, returnEvent -> { // Action: Pressing the return item button in the orders page
            try {
                ArrayList<OrderContent> itemsList = ((User) account).getOrderItems(conn, ((User) account).getID());
                userPage.ordersListToProducts(itemsList);
                int option = JOptionPane.showConfirmDialog(null, userPage.getRequestReturnPanel(), "Request Return", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String orderInp = userPage.getOrderInp();
                    String productInp = userPage.getProdInp();
                    String reason = userPage.getComboBoxVal();
                    String desc = userPage.getReturnDesc();
                    
                    if (orderInp.trim().isEmpty() || productInp.trim().isEmpty()) { // When an input field is not filled up
                        JOptionPane.showMessageDialog(null, "Please fill out the required input fields:\nOrder ID\nProduct ID");
                    }
                    else {
                        try {
                            int order_id = Integer.parseInt(orderInp.trim());
                            int product_id = Integer.parseInt(productInp.trim());
                            
                            if (Return.requestReturn(conn, product_id, order_id, ((User) account).getID(), reason, desc)) {
                                JOptionPane.showMessageDialog(null, "Return request was successful.");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Return request was unsuccessful.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid ID input/s.");
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }

        }, rateEvent -> { // Action: Pressing the return item button in the orders page
            try {
                ArrayList<OrderContent> itemsList = ((User) account).getOrderItems(conn, ((User) account).getID());
                userPage.ordersListToProducts(itemsList);
                int option = JOptionPane.showConfirmDialog(null, userPage.getRatingPanel(), "Rate a Product", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String orderInp = userPage.getOrderInp();
                    String productInp = userPage.getProdInp();
                    int rating = userPage.getSpinnerVal();
                    if (orderInp.trim().isEmpty() || productInp.trim().isEmpty()) { // When an input field is not filled up
                        JOptionPane.showMessageDialog(null, "Please fill out the required input fields:\nOrder ID\nProduct ID");
                    }
                    else {
                        try {
                            int order_id = Integer.parseInt(orderInp.trim());
                            int product_id = Integer.parseInt(productInp.trim());

                            if (OrderContent.rateProduct(conn, order_id, product_id, ((User) account).getID(), rating)) {
                                JOptionPane.showMessageDialog(null, "Rating was successful.");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Rating was unsuccessful.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid ID input/s.");
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
            
        }, receiveEvent -> { // Action: Pressing the receive item button in the orders page
            try {
                ArrayList<OrderContent> itemsList = ((User) account).getOrderItems(conn, ((User) account).getID());
                userPage.ordersListToProducts(itemsList);
                int option = JOptionPane.showConfirmDialog(null, userPage.getReceiveOrderPanel(), "Receive Order", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String orderInp = userPage.getOrderInp();

                    if (orderInp.trim().isEmpty()) { // When an input field is not filled up
                        JOptionPane.showMessageDialog(null, "Please fill out the required input fields:\nOrder ID\nProduct ID");
                    }
                    else {
                        try {
                            int order_id = Integer.parseInt(orderInp.trim());

                            if (Order.receiveOrder(conn, order_id, ((User) account).getID())) {
                                JOptionPane.showMessageDialog(null, "Order successfully received.");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Failed to receive order.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid ID input.");
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }
        }, saveChangeEvent -> { // Action: Pressing the save changes button in the profile page
            User user = (User) account;

            int option = JOptionPane.showConfirmDialog(null, "Save changes?", "Edit Profile", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    // Retrieve the edited fields and update in the database
                    user.updateUser(
                        userPage.getEditedName(),
                        userPage.getEditedFirstName(),
                        userPage.getEditedLastName(),
                        userPage.getEditedAddress(),
                        userPage.getEditedPhone(),
                        conn
                    );
                    JOptionPane.showMessageDialog(null, "Profile changed...");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Edit cancelled...");
            }

        }, browseSelectEvent -> { // Action: Pressing an option in the browse by options list in the shop page
            if (userPage.getSelectedOption() == null) {
                return;
            }

            ArrayList<Product> products = new ArrayList<>();

            try {
                // Retrieve the list of products for the user to shop from
                if (userPage.getBrowseByOption().equals("By shop")) {
                    String query =
                    """
                    SELECT *
                    FROM products
                    WHERE seller_id = ?;
                    """;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, Integer.parseInt(userPage.getSelectedOption()));

                    products.addAll(((User) account).getSelectedProductList(pstmt));

                } else if (userPage.getBrowseByOption().equals("By product type")) {
                    String query =
                    """
                    SELECT *
                    FROM products
                    WHERE product_type = ?;
                    """;

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, userPage.getSelectedOption());

                    products.addAll(((User) account).getSelectedProductList(pstmt));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

            // Update the list of products using the resulting list
            userPage.updateProductsList(products);

        }, productSelectEvent -> { // Action: Pressing a product in the product list in the shop page
            Product selectedProduct = userPage.getSelectedProduct();

            if (selectedProduct == null) {
                return;
            }

            userPage.setProductInfo(
            "<html>Product info<br/>" +
            "Name: " + selectedProduct.getName() + "<br/>" +
            "Price: " + selectedProduct.getPrice() + "<br/>" +
            "Type: " + selectedProduct.getType() + "<br/>" +
            "Rating: " + selectedProduct.getRating() + "<br/>" +
            "Qty: " + selectedProduct.getQuantity() + "<br/>" +
            "Listed: " + selectedProduct.isListed() + "<br/>" +
            "Description: " + selectedProduct.getDescription());

        }, orderSelectEvent -> { // Action: Pressing an order in the orders list in the orders page
            if (!orderSelectEvent.getValueIsAdjusting()) {
                String val = userPage.getMappedValue(userPage.getSelectedOrder());
 
                if (userPage.getOrdersViewOption().equals("Orders") && val != null) {
                    int order_id = Integer.parseInt(val);
                    try {
                        String query = 
                            """
                            SELECT o.order_status, c.courier_name, p.product_name, o.purchase_date, s.seller_name, oc.item_quantity, oc.subtotal
                            FROM orders o
                            JOIN order_contents oc ON o.order_id = oc.order_id
                            JOIN couriers c ON c.courier_id = o.courier_id
                            JOIN products p ON oc.product_id = p.product_id
                            JOIN sellers s ON s.seller_id = p.seller_id
                            WHERE o.order_id = ?
                            """;
                        PreparedStatement ps = conn.prepareStatement(query);
                        ps.setInt(1, order_id);
                        ResultSet rs = ps.executeQuery();
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
                        
                        if (rs.next()) {
                            float total = 0.0f;
                            String text = "<html><b>Order Status:</b> " + rs.getString("o.order_status") + "<br><b>Courier:</b> " + rs.getString("c.courier_name");
                            text += "<br><b>Date of Purchase:</b> " + sdf.format(rs.getDate("o.purchase_date"));
                            text += "<br><br><b>[ITEMS ORDERED]</b>";
                            
                            do {
                                text += "<br><br>" + rs.getString("p.product_name") + " from " + rs.getString("s.seller_name") + "<br>Quantity: " + Integer.toString(rs.getInt("oc.item_quantity"));
                                total += rs.getFloat("oc.subtotal");
                            } while (rs.next());
                            
                            text += "<br><br><b>TOTAL:</b> PHP " + String.format("%.2f", total) + "</html>";
                            
                            userPage.setOrderInfoLbl(text);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                    
                    
                }
                else if (userPage.getOrdersViewOption().equals("Returns") && val != null) {
                    String ids[] = val.split(" ");
                    int order_id = Integer.parseInt(ids[0]);
                    int product_id = Integer.parseInt(ids[1]);
                    try {
                        String query = 
                            """
                            SELECT p.product_name, s.seller_name, c.courier_name, r.return_status
                            FROM `returns` r
                            JOIN orders o ON o.order_id = r.order_id
                            JOIN products p  ON r.product_id = p.product_id
                            JOIN couriers c ON r.courier_id = c.courier_id
                            JOIN sellers s ON p.seller_id = s.seller_id
                            WHERE p.product_id = ?
                            AND o.order_id = ?
                            """;
                        PreparedStatement ps = conn.prepareStatement(query);
                        ps.setInt(1, product_id);
                        ps.setInt(2, order_id);
                        ResultSet rs = ps.executeQuery();
                        
                        if (rs.next())  {
                            String text = "<html><b>Item:</b> " + rs.getString("p.product_name") + "<br><b>Seller:</b> " + rs.getString("s.seller_name");
                            text  += "<br><b>Courier:</b> " + rs.getString("c.courier_name") + "<br><b>Status:</b> " + rs.getString("r.return_status") +  "</html>";
                            
                            userPage.setOrderInfoLbl(text);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void initSellerListeners() {
        sellerPage.initSignUpListeners(submitSignUpEvent -> {
            String seller_name = sellerPage.getSellerName();
            String seller_address = sellerPage.getSellerAddress();
            String seller_phone_number = sellerPage.getSellerPhone();
            
            if (seller_name.isEmpty())
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nName");
            else {
                if (!seller_phone_number.isEmpty()) {
                    Pattern pattern = Pattern.compile("^\\d{11}$");
                    Matcher matcher = pattern.matcher(seller_phone_number);

                    while (!matcher.matches()) {
                        JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                        seller_phone_number = sellerPage.getSellerPhone();
                        matcher = pattern.matcher(seller_phone_number);
                    }
                }

                Date seller_creation_date = new Date(System.currentTimeMillis());
                boolean seller_verified_status = false;

                if (!seller_phone_number.isEmpty() && !seller_address.isEmpty())
                    seller_verified_status = true;

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

                    int seller_id = 1;
                    try (PreparedStatement selectStmt = conn.prepareStatement(query);
                         ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            seller_id = rs.getInt(1);
                        }
                    }
                    this.account = new Seller(seller_id, seller_name, seller_address, seller_phone_number, seller_creation_date, seller_verified_status);
                    sellerPage.nextPageName(AccountPage.MAINPAGE);

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            }
        }, backSignUpEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);

            sellerPage.nextPageName(AccountPage.SIGNUPPAGE);
        });
        sellerPage.initMainListeners(generateEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            sellerPage.nextPageName(SellerPage.SIGNUP);
        }, editAccountEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            sellerPage.nextPageName(SellerPage.SIGNUP);
        }, logoutEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            sellerPage.nextPageName(SellerPage.SIGNUP);
        }, listChangeEvent -> {
            sellerPage.setInvisibleBtns(sellerPage.getSellerCRBox());
            try {
                if (sellerPage.getSellerCRBox().equals("Products")) {
                    sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));
                }else if (sellerPage.getSellerCRBox().equals("Refunds")){
                    sellerPage.updateSellerRefundList(((Seller) account).refundList(this.conn));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }, listSelectEvent ->{
            if (sellerPage.getSellerCRBox().equals("Products")) {
                if (sellerPage.getSelectedOption()!=null) {
                    List<Integer> Ids = Arrays.stream(sellerPage.getSelectedOption()
                                    .split(" "))
                            .map(Integer::parseInt)
                            .toList();

                    Product product;

                    try {
                        String query = """
                                       SELECT *
                                       FROM products
                                       WHERE product_id = ? AND seller_id = ?
                                       LIMIT 1;
                                       """;
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, Ids.get(0));
                        pstmt.setInt(2, Ids.get(1));
                        product = ((Seller)account).getSellerProduct(pstmt);

                        sellerPage.setProductRefundInfo(
                                " Product Info \n" +
                                "Product ID: " + product.getProductID() + "\n" +
                                "Product Name: " + product.getName() + "\n" +
                                "Product Price: " + product.getPrice() + "\n" +
                                "Product Quantity: " + product.getQuantity() + "\n" +
                                "Product Rating: " + product.getRating() + "\n" +
                                "Product Listed: " + product.isListed() + "\n" +
                                "Product Type: " + product.getDescription() + "\n" +
                                "Product Description: " + product.getType()
                        );

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                }
            }else if (sellerPage.getSellerCRBox().equals("Refunds")){
                if (sellerPage.getSelectedOption()!=null) {
                    List<Integer> Ids = Arrays.stream(sellerPage.getSelectedOption()
                                    .split(" "))
                            .map(Integer::parseInt)
                            .toList();

                    Return refund;

                    try {
                        String query = """
                                       SELECT *
                                       FROM returns
                                       WHERE order_id = ? AND product_id = ?
                                       LIMIT 1;
                                       """;
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, Ids.get(0));
                        pstmt.setInt(2, Ids.get(1));
                        refund = ((Seller)account).getSellerRefund(pstmt);

                        sellerPage.setProductRefundInfo(
                                "Refund Info \n" +
                                "Order ID: " + refund.getOrderID() + "\n" +
                                "Product ID: " + refund.getProductID() + "\n" +
                                "Courier ID: " + refund.getCourierID() + "\n" +
                                "Return Reason: " + refund.getReason() + "\n" +
                                "Product Rating: " + refund.getDescription() + "\n" +
                                "Product Listed: " + refund.getDate() + "\n" +
                                "Product Type: " + refund.getStatus() + "\n"
                        );

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void initCourierListeners() {
        courierPage.initSignUpListeners(submitSignUpEvent -> {
            String courier_name = courierPage.getCourierName();
            String courier_email_address = courierPage.getCourierEmail();
            String courier_address = courierPage.getCourierAddress();
            
            if (courier_name.isEmpty())
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nName");
            else {
                if (!courier_email_address.isEmpty()) {
                    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                            "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(courier_email_address);

                    while (!matcher.matches()) {
                        JOptionPane.showMessageDialog(null, "Error: Invalid email format.");
                        courier_email_address = courierPage.getCourierEmail();
                        matcher = pattern.matcher(courier_email_address);
                    }
                }

                boolean courier_verified_status = !courier_email_address.isEmpty() && !courier_address.isEmpty();


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
                        pstmt.setBoolean(4, !(courier_address.isEmpty() || courier_email_address.isEmpty()));
                                                                // ^ verifies the courier
                        pstmt.executeUpdate();
                    }

                    query =
                    """
                    SELECT MAX(courier_id)
                    FROM couriers
                    """;

                    int courier_id = 1;
                    try (PreparedStatement selectStmt = conn.prepareStatement(query);
                         ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            courier_id = rs.getInt(1);
                        }
                    }

                    this.account = new Courier(courier_id, courier_name, courier_email_address, courier_address, courier_verified_status);
                    courierPage.nextPageName(AccountPage.MAINPAGE);

                } catch (SQLException e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            }
        }, backSignUpEvent -> {
            courierPage.clearTextFields();
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            courierPage.nextPageName(AccountPage.SIGNUPPAGE);
        });

        courierPage.initMainListeners(orderLtr -> {
                courierPage.updateOOTable(((Courier)account).showOngoingOrders(conn));
                courierPage.updateORTable(((Courier)account).showOngoingReturns(conn));
                courierPage.nextMainPageName(CourierPage.ONGOINGORDERSPAGE);
            }, actEvent -> {
                courierPage.updateAOTable(((Courier)account).showCompletedOrders(conn));
                courierPage.updateARTable(((Courier)account).showCompletedReturns(conn));
                courierPage.nextMainPageName(CourierPage.ACTIVITYPAGE);
            }, editEvent -> {
                courierPage.nextMainPageName(CourierPage.EDITPAGE);
                courierPage.updateProfilePage((Courier) account);
            }, logOutEvent -> {
                mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
                selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
        });
    }
}