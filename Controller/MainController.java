package Controller;

import Model.enums.ReturnStatus;
import Model.*;
import Model.enums.OrderStatus;
import View.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Year;
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

    public MainController(MainFrame mainFrame, SQLConnect sqlConnect, SelectAccount selectAccount,
                          UserPage userPage, SellerPage sellerPage, CourierPage courierPage) {

        mainMenuPage = mainFrame;
        connectPage = sqlConnect;
        selectAccountPage = selectAccount;
        this.userPage = userPage;
        this.sellerPage = sellerPage;
        this.courierPage = courierPage;

        initConnectionListeners();
        initSelectListeners();
        initUserListeners();
        initSellerListeners();
        initCourierListeners();

        mainMenuPage.addToCenterPanel(connectPage, MainFrame.CONNECTPAGE);
        mainMenuPage.addToCenterPanel(selectAccountPage, MainFrame.SELECTACCPAGE);
        mainMenuPage.addToCenterPanel(userPage, MainFrame.USERPAGE);
        mainMenuPage.addToCenterPanel(sellerPage, MainFrame.SELLERPAGE);
        mainMenuPage.addToCenterPanel(courierPage, MainFrame.COURIERPAGE);

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
                conn = DriverManager.getConnection(url, username, password);

                selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
                mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);

            } catch (SQLException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, exitEvent -> {
            try {
                if (conn != null) {
                    conn.close();
                }
                System.exit(0);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }

    private void initSelectListeners() {
        selectAccountPage.initSelectListeners(loginEvent -> {
            selectAccountPage.clearText();
            selectAccountPage.nextPageName(SelectAccount.LOGINPAGE);

            this.account = selectAccountPage.getAccountType();
        }, signUpEvent -> {
            this.account = selectAccountPage.getAccountType();

            (account instanceof User ? userPage :
            account instanceof Seller ? sellerPage :
            courierPage).nextPageName(AccountPage.SIGNUPPAGE);

            // Go to their SIGNUP page
            mainMenuPage.nextPageName(account.toString());

        }, submitLoginEvent -> {
            try {
                if (account.login(Integer.parseInt(selectAccountPage.getID()), conn)) {

                    (account instanceof User ? userPage :
                    account instanceof Seller ? sellerPage :
                    courierPage).nextPageName(AccountPage.MAINPAGE);

                    // Go to their respective MAIN page
                    mainMenuPage.nextPageName(account.toString());

                    if (account instanceof User) {
                        userPage.updateBrowseList(((User) account).browseByShops(conn));
                    } else if (account instanceof Seller){
                        sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Account does not exist.");
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
            
            if (user_name.isEmpty() || user_firstname.isEmpty() || user_lastname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nUsername\nFirst Name\nLast Name");
            } else {

                if (!phoneChecker(user_phone_number)) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                    return;
                }

                Date user_creation_date = new Date(System.currentTimeMillis());
                boolean user_verified_status = !user_phone_number.isEmpty() && !user_address.isEmpty();

                try {
                    String query =
                    """
                    INSERT INTO users (user_id, user_name, user_phone_number, user_address, user_verified_status, user_creation_date, user_firstname, user_lastname)
                    SELECT IFNULL(MAX(user_id), 0) + 1, ?, ?, ?, ?, ?, ?, ?
                    FROM users
                    """;

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, user_name);
                    pstmt.setString(2, user_phone_number);
                    pstmt.setString(3, user_address);
                    pstmt.setBoolean(4, user_verified_status);
                    pstmt.setTimestamp(5, new java.sql.Timestamp(user_creation_date.getTime()));
                    pstmt.setString(6, user_firstname);
                    pstmt.setString(7, user_lastname);

                    pstmt.executeUpdate();

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
            MainFrame.clearInputs(userPage);
            userPage.refreshUserPage(); // Labels of information disappear when clearInputs is called
            initUserListeners();

            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            userPage.nextMainPageName(UserPage.SHOPPAGE);

        }, addToCartEvent -> { // Action: Pressing the add button in the shop page
            User user = (User) account;
            Product selectedProduct = userPage.getSelectedProduct();
            int orderQuantity = userPage.getQuantity();

            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Error: Select a product.");
                return;
            } else if (orderQuantity == 0) {
                return;
            }

            if (selectedProduct.isListed()) {
                // Show a pop-up asking for the order quantity
                if (orderQuantity <= selectedProduct.getQuantity()) {
                    // Add to cart
                    user.addProductToCart(new OrderContent(selectedProduct.getProductID(), selectedProduct.getName(), orderQuantity, selectedProduct.getPrice()));
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Only " + selectedProduct.getQuantity() + " are in stock.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error: Product is not listed");
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
            Set<OrderContent> shoppingCart = ((User) account).getShoppingCart();
            ArrayList<OrderContent> selectedProducts = userPage.getSelectedRecords(new ArrayList<>(shoppingCart));

            if (selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: Select item to checkout.");
                return;
            }

            float totalPrice = 0f;
            for (OrderContent product : selectedProducts) {
                totalPrice += product.getPriceEach() * product.getQuantity();
            }

            int choice = userPage.getCheckoutConfirmPage(selectedProducts);

            try {
                if (choice == JOptionPane.OK_OPTION) {
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
            Set<OrderContent> shoppingCart = ((User) account).getShoppingCart();
            ArrayList<OrderContent> selectedProducts = userPage.getSelectedRecords(new ArrayList<>(shoppingCart));

            if (selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: Select item to checkout.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove items", JOptionPane.YES_NO_OPTION);

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
                User user = (User) account;
                ArrayList<OrderContent> itemsList = user.getOrderItems(conn, user.getID());
                userPage.ordersListToProducts(itemsList);

                int option = JOptionPane.showConfirmDialog(null, userPage.getRequestReturnPanel(), "Request Return", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String orderInp = userPage.getOrderInp();
                    String productInp = userPage.getProdInp();
                    String reason = userPage.getComboBoxVal();
                    String desc = userPage.getReturnDesc();

                    if (orderInp.trim().isEmpty() || productInp.trim().isEmpty()) { // When an input field is not filled up
                        JOptionPane.showMessageDialog(null, "Please fill out the required input fields:\nOrder ID\nProduct ID");
                    } else {
                        try {
                            int order_id = Integer.parseInt(orderInp.trim());
                            int product_id = Integer.parseInt(productInp.trim());

                            if (Return.requestReturn(conn, product_id, order_id, ((User) account).getID(), reason, desc)) {
                                JOptionPane.showMessageDialog(null, "Return request was successful.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Return request was unsuccessful.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid ID input/s.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aborting return...");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e);
            }

        }, rateEvent -> { // Action: Pressing the return item button in the orders page
            try {
                User user = (User) account;

                ArrayList<OrderContent> itemsList = user.getOrderItems(conn, user.getID());
                userPage.ordersListToProducts(itemsList);

                int option = JOptionPane.showConfirmDialog(null, userPage.getRatingPanel(), "Rate a Product", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String orderInp = userPage.getOrderInp();
                    String productInp = userPage.getProdInp();
                    int rating = userPage.getSpinnerVal();

                    if (orderInp.trim().isEmpty() || productInp.trim().isEmpty()) { // When an input field is not filled up
                        JOptionPane.showMessageDialog(null, "Please fill out the required input fields:\nOrder ID\nProduct ID");
                    } else {
                        try {
                            int order_id = Integer.parseInt(orderInp.trim());
                            int product_id = Integer.parseInt(productInp.trim());

                            if (OrderContent.rateProduct(conn, order_id, product_id, user.getID(), rating)) {
                                JOptionPane.showMessageDialog(null, "Rating was successful.");
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Rating was unsuccessful.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid ID input/s.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aborting Rate...");
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
                            } else {
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
                    if (!userPage.getEditedPhone().isEmpty()) {
                        if (!(phoneChecker(userPage.getEditedPhone()))) {
                            JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                            return;
                        }
                    }

                    user.setName(userPage.getEditedName());
                    user.setFirstName(userPage.getEditedFirstName());
                    user.setLastName(userPage.getEditedLastName());
                    user.setAddress(userPage.getEditedAddress());
                    user.setPhoneNumber(userPage.getEditedPhone());

                    user.updateAccount(conn);
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
                    WHERE seller_id = ?
                    AND listed_status = 1;
                    """;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, Integer.parseInt(userPage.getSelectedOption()));

                    products.addAll(((User) account).getSelectedProductList(pstmt));

                } else if (userPage.getBrowseByOption().equals("By product type")) {
                    String query =
                    """
                    SELECT *
                    FROM products
                    WHERE product_type = ?
                    AND listed_status = 1;
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
            "Product info: (" + selectedProduct.getProductID() + ")\n" +
            "Name: " + selectedProduct.getName() + "\n" +
            "Price: ₱" + selectedProduct.getPrice() + "\n" +
            "Type: " + selectedProduct.getType() + "\n" +
            "Rating: " + selectedProduct.getRating() + "\n" +
            "Quantity in Stock: " + selectedProduct.getQuantity() + "\n" +
            "Availability: " + ((selectedProduct.isListed()) ? "Yes" : "No") + "\n" +
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
                            StringBuilder text = new StringBuilder();

                            text.append(
                                String.format(
                                    "<html><b>Order Status:</b> %s<br><b>Courier:</b> %s",

                                    rs.getString("o.order_status"),
                                    rs.getString("c.courier_name")
                                )
                            );
                            text.append(
                                String.format(
                                    "<br><b>Date of Purchase:</b> %s<br><br><b>[ITEMS ORDERED]</b>",
                                    sdf.format(rs.getDate("o.purchase_date"))
                                )
                            );
                            
                            do {
                                text.append(
                                    String.format(
                                        "<br><br>%s from %s" + "<br><b>Quantity:</b> %d",
                                        rs.getString("p.product_name"),
                                        rs.getString("s.seller_name"),
                                        rs.getInt("oc.item_quantity")
                                    )
                                );
                                total += rs.getFloat("oc.subtotal");

                            } while (rs.next());

                            text.append(
                                String.format(
                                    "<br><br><b>TOTAL:</b> ₱ %.2f </html",
                                    total
                                )
                            );
                            
                            userPage.setOrderInfoLbl(text.toString());
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }

                } else if (userPage.getOrdersViewOption().equals("Returns") && val != null) {
                    String[] ids = val.split(" ");

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
            
            if (seller_name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nName");
            } else {
                if (!phoneChecker(seller_phone_number)) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                    return;
                }

                Date seller_creation_date = new Date(System.currentTimeMillis());
                boolean seller_verified_status = !seller_phone_number.isEmpty() && !seller_address.isEmpty();

                try {
                    String query =
                    """
                    INSERT INTO sellers (seller_id, seller_name, seller_address, seller_verified_status, seller_phone_number, seller_creation_date)
                    SELECT IFNULL(MAX(seller_id), 0) + 1, ?, ?, ?, ?, ?
                    FROM sellers
                    """;

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, seller_name);
                    pstmt.setString(2, seller_address);
                    pstmt.setBoolean(3, seller_verified_status);
                    pstmt.setString(4, seller_phone_number);
                    pstmt.setTimestamp(5, new java.sql.Timestamp(seller_creation_date.getTime()));

                    pstmt.executeUpdate();

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

                    account = new Seller(seller_id, seller_name, seller_address, seller_phone_number, seller_creation_date, seller_verified_status);
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
            sellerPage.showGenerate();
            sellerPage.setDisableButtons();
        }, editAccountEvent -> {
            sellerPage.showEditAccount();
            sellerPage.updateEditAccount((Seller) account);
            sellerPage.setDisableButtons();

        }, logoutEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            sellerPage.nextPageName(SellerPage.SIGNUP);

            sellerPage.setBackSellerBox();
            sellerPage.setProductRefundInfo("");

        }, listChangeEvent -> {
            sellerPage.setInvisibleBtns(sellerPage.getSellerCRBox());

            try {
                if (sellerPage.getSellerCRBox().equals("Products")) {
                    sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));
                } else if (sellerPage.getSellerCRBox().equals("Refunds")) {
                    sellerPage.updateSellerRefundList(((Seller) account).refundList(this.conn));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, listSelectEvent -> {
            if (sellerPage.getSellerCRBox().equals("Products")) {
                if (sellerPage.getSelectedOption() != null) {
                    List<Integer> Ids =
                            Arrays.stream(sellerPage.getSelectedOption()
                            .split(" "))
                            .map(Integer::parseInt)
                            .toList();
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

                        Product product = ((Seller) account).getSellerProduct(pstmt);

                        sellerPage.setProductRefundInfo(
                            "<html><b> Product Info </b> <br>\n" +
                            "<b> Product ID: </b>" + product.getProductID() + "<br>" +
                            "<b> Product Name: </b>" + product.getName() + "<br>" +
                            "<b> Product Price: </b>" + product.getPrice() + "<br>" +
                            "<b> Product Quantity: </b>" + product.getQuantity() + "<br>" +
                            "<b> Product Rating: </b>" + product.getRating() + "<br>" +
                            "<b> Product Listed: </b>" + product.isListed() + "<br>" +
                            "<b> Product Type: </b>" + product.getDescription() + "<br>" +
                            "<b> Product Description: </b>" + product.getType() + "</html>"
                        );

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Select a product first.");
                }

            } else if (sellerPage.getSellerCRBox().equals("Refunds")) {
                if (sellerPage.getSelectedOption() != null) {
                    List<Integer> Ids =
                            Arrays.stream(sellerPage.getSelectedOption()
                            .split(" "))
                            .map(Integer::parseInt)
                            .toList();

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
                        Return refund = ((Seller) account).getSellerRefund(pstmt);

                        sellerPage.setProductRefundInfo(
                                "<html><b> Refund Info </b><br>\n" +
                                        "<b> Order ID: </b>" + refund.getOrderID() + "<br>" +
                                        "<b> Product ID: </b>" + refund.getProductID() + "<br>" +
                                        "<b> Courier ID: </b>" + refund.getCourierID() + "<br>" +
                                        "<b> Return Reason: </b>" + refund.getReason() + "<br>" +
                                        "<b> Product Rating: </b>" + refund.getDescription() + "<br>" +
                                        "<b> Product Listed: </b>" + refund.getDate() + "<br>" +
                                        "<b> Product Type: </b>" + refund.getStatus() + "</html>"
                        );

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Select a refund first.");
                }
            }
        }, addEvent -> { // Add product
            sellerPage.showAddProduct();
            sellerPage.setDisableButtons();
            /*
            if (result==JOptionPane.OK_OPTION){
                if (sellerPage.getSelectedOption()!=null) {
                    Product product = new Product();
                    product.setSellerID(((Seller)account).getID());
                    product.setName(sellerPage.getProductName());
                    product.setPrice(Float.parseFloat(sellerPage.getProductPrice()));
                    product.setDescription(sellerPage.getProductDesc());
                    product.setQuantity(Integer.parseInt(sellerPage.getProductQuantity()));
                    product.setType(sellerPage.getProductType());
                    product.isListed();
                    product.sendToDB(conn);
                }
            }
            */
        }, editProdEvent -> {

            if (sellerPage.getSelectedOption()!=null) {

                sellerPage.showEditProduct();
                sellerPage.setDisableButtons();

                try {
                    List<Integer> Ids =
                            Arrays.stream(sellerPage.getSelectedOption()
                            .split(" "))
                            .map(Integer::parseInt)
                            .toList();

                    Product product;

                    String query = """
                            SELECT *
                            FROM products
                            WHERE product_id = ? AND seller_id = ?
                            LIMIT 1;
                            """;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, Ids.get(0));
                    pstmt.setInt(2, Ids.get(1));
                    product = ((Seller) account).getSellerProduct(pstmt);
                    sellerPage.updateEditProduct(product);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Error: No Product Selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }, cancelEvent -> sellerPage.disposeNewWindow(),

        saveProfileEvent -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Prompt", JOptionPane.OK_CANCEL_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
                Seller seller = (Seller) account;

                sellerPage.updateEditAccount(seller);

                if (sellerPage.getSellerName().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nName");
                } else {

                    if (!phoneChecker(sellerPage.getSellerPhone())) {
                        JOptionPane.showMessageDialog(null, "Error: Invalid phone number format.");
                        return;
                    }

                    seller.setName(sellerPage.getSellerName());
                    seller.setAddress(sellerPage.getSellerAddress());
                    seller.setPhoneNumber(sellerPage.getSellerPhone());

                    try {
                        account.updateAccount(conn);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                    }

                    sellerPage.disposeNewWindow();
                }
            }
        }, addProductEvent -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Prompt", JOptionPane.OK_CANCEL_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
                try {
                    if (sellerPage.getSelectedOption() != null) {
                        Product product = new Product();

                        product.setSellerID(((Seller) account).getID());
                        product.setName(sellerPage.getProductName());
                        product.setPrice(sellerPage.getProductPrice());
                        product.setDescription(sellerPage.getProductDesc());
                        product.setQuantity(sellerPage.getProductQuantity());
                        product.setType(sellerPage.getProductType());
                        product.updateListedStatus();

                        product.sendToDB(conn);
                    }

                    sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }

                sellerPage.disposeNewWindow();
            }
        }, saveProductEvent -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Prompt", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION){
                List<Integer> Ids =
                    Arrays.stream(sellerPage.getSelectedOption()
                    .split(" "))
                    .map(Integer::parseInt)
                    .toList();

                try {
                    String query =
                    """
                    UPDATE products
                    SET product_name = ?
                        product_price = ?
                        product_type = ?
                        quantity_stocked = ?
                        description = ?
                        listed_status = ?
                    WHERE product_id = ? AND seller_id = ?
                    LIMIT 1;
                    """;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    ((Product) account).setQuantity(sellerPage.getProductQuantity());

                    pstmt.setString(1, sellerPage.getProductName());
                    pstmt.setFloat(2, sellerPage.getProductPrice());
                    pstmt.setString(3, sellerPage.getProductType());
                    pstmt.setInt(4, sellerPage.getProductQuantity());
                    pstmt.setString(5, sellerPage.getProductDesc());
                    pstmt.setBoolean(6, ((Product) account).isListed());
                    pstmt.setInt(7, Ids.get(0));
                    pstmt.setInt(8, Ids.get(1));

                    pstmt.executeUpdate();
                    sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
                sellerPage.disposeNewWindow();
            }
        }, removeProductEvent -> {
            if (sellerPage.getSelectedOption() != null) {
                try {
                    List<Integer> Ids = Arrays.stream(sellerPage.getSelectedOption()
                                    .split(" "))
                            .map(Integer::parseInt)
                            .toList();

                    String query = """
                            UPDATE products
                            SET listed_status = ?
                            WHERE product_id = ? AND seller_id = ?
                            """;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, 0);
                    pstmt.setInt(2, Ids.get(0));
                    pstmt.setInt(3, Ids.get(1));
                    pstmt.executeUpdate();
                    sellerPage.updateSellerProductList(((Seller) account).productList(this.conn));
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Product Selected!", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        }, approveReturnEvent -> {
            if (sellerPage.getSelectedOption() != null) {
                switch (JOptionPane.showConfirmDialog(null, "Are you sure?", "Prompt", JOptionPane.OK_CANCEL_OPTION)){
                    case JOptionPane.OK_OPTION -> {
                        List<Integer> Ids = Arrays.stream(sellerPage.getSelectedOption()
                                        .split(" "))
                                        .map(Integer::parseInt)
                                        .toList();
                        try {
                            String query = """
                                    UPDATE returns
                                    SET return_status = ?
                                    WHERE order_id = ? AND product_id = ?;
                                    """;
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, ReturnStatus.REFUNDED.toString());
                            pstmt.setInt(2, Ids.get(0));
                            pstmt.setInt(3, Ids.get(1));
                            pstmt.executeUpdate();
                            sellerPage.updateSellerRefundList(((Seller) account).refundList(this.conn));
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Refund Selected!", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        }, rejectReturnEvent -> {
            if (sellerPage.getSelectedOption()!=null) {
                switch (JOptionPane.showConfirmDialog(null, "Are you sure?", "Prompt", JOptionPane.OK_CANCEL_OPTION)){
                    case JOptionPane.OK_OPTION -> {
                        List<Integer> Ids = Arrays.stream(sellerPage.getSelectedOption()
                                        .split(" "))
                                .map(Integer::parseInt)
                                .toList();
                        try {
                            String query = """
                                    UPDATE returns
                                    SET return_status = ?
                                        return_date = '9999-12-31'
                                    WHERE order_id = ? AND product_id = ?;
                                    """;
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, ReturnStatus.REJECTED.toString());
                            pstmt.setInt(2, Ids.get(0));
                            pstmt.setInt(3, Ids.get(1));
                            pstmt.executeUpdate();
                            sellerPage.updateSellerRefundList(((Seller) account).refundList(this.conn));
                        }catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Refund Selected!", "Failure", JOptionPane.ERROR_MESSAGE);
            }
        }, generateSelected -> {
            try {
                if (sellerPage.getSellerReportBox().equals("Sales Report")) {
                    int month = sellerPage.getDateMonth();
                    int year = sellerPage.getDateYear();

                    String query =
                    """
                    SELECT COUNT(DISTINCT o.order_id) AS count, SUM(oc.subtotal) AS total
                    FROM order_contents oc
                    JOIN products p ON oc.product_id = p.product_id
                    JOIN orders o ON o.order_id = oc.order_id
                    LEFT JOIN `returns` r ON r.order_id = oc.order_id AND r.product_id = oc.product_id
                    WHERE p.seller_id = ?
                    AND YEAR(o.purchase_date) = ?
                    AND MONTH(o.purchase_date) = ?
                    AND o.order_status = 'DELIVERED'
                    AND (r.return_status IS NULL OR r.return_status != 'REFUNDED')
                    GROUP BY o.order_id;
                    """;

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, ((Seller)account).getID());
                    ps.setInt(2, year);
                    ps.setInt(3, month);
                    ResultSet rs = ps.executeQuery();

                    int transactions = 0;
                    float sumOfEarnings = 0f;
                    if (rs.next()) {
                        do {
                            transactions += rs.getInt("count");
                            sumOfEarnings += rs.getInt("total");
                        } while(rs.next());
                    }

                    sellerPage.setProductRefundInfo(
                        "<html><b>Sales Report for " + year + "</b><br>" +
                        "<b>Total orders handled: </b>" + transactions +"<br>" +
                        "<b>Total earnings: ₱ </b>" + sumOfEarnings + "</html>"
                    );
                    sellerPage.disposeNewWindow();

                } else if (sellerPage.getSellerReportBox().equals("Credibility Report")) {
                    int year;
                    int refunds = 0;
                    float average = 0;

                    System.out.print("Enter year: ");
                    year = sellerPage.getDateYear();

                    String query =
                            """
                            SELECT AVG(p.average_rating) AS AverageRating
                            FROM products p
                            WHERE seller_id = ?;
                            """;

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, ((Seller)account).getID());
                    ResultSet rstmt = pstmt.executeQuery();

                    while (rstmt.next()) {
                        average = rstmt.getFloat("AverageRating");
                    }

                    query =
                        """
                        SELECT COUNT(product_id) AS numOfRefunds
                        FROM returns r
                        WHERE 	r.return_status = 'REFUNDED' AND
                                YEAR(r.return_date) = ? AND
                                product_id IN (SELECT product_id
                                FROM products p
                                WHERE seller_id = ?);
                        """;

                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, year);
                    pstmt.setInt(2, ((Seller)account).getID());
                    rstmt = pstmt.executeQuery();

                    while (rstmt.next()) {
                        refunds = rstmt.getInt("numOfRefunds");
                    }

                    sellerPage.setProductRefundInfo(
                            "<html><b>Credibility Report for " + ((Seller) account).getName() + " on Year: " + year + "</b><br>" +
                            "<b>Overall Average Rating of Products: </b>" + average + "<br>" +
                            "<b>Total number of Refunds: </b>" + refunds + "</html"
                    );
                    sellerPage.disposeNewWindow();

                } else if (sellerPage.getSellerReportBox().equals("Product Popularity Report")) {
                    int month = sellerPage.getDateMonth();
                    int year = sellerPage.getDateYear();

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
                    ps.setInt(3, ((Seller)account).getID());
                    ResultSet rs = ps.executeQuery();

                    ArrayList<Object[]> data = new ArrayList<>();
                    int rank = 1;
                    while (rs.next()) {
                        Object[] record = new Object[4];
                        record[0] = rank;
                        record[1] = rs.getString("p.product_name");
                        record[2] =  rs.getInt("amt_sold");
                        record[3] = rs.getFloat("p.average_rating");
                        data.add(record);
                        rank++;
                    }

                    sellerPage.nextMainPageName(SellerPage.PRODUCTPOPLIST);
                    sellerPage.updateProductPopTable(data);
                    sellerPage.disposeNewWindow();
                    sellerPage.setDisableTopButtons();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        }, listReportEvent -> {
            if (sellerPage.getSellerReportBox().equals("Sales Report")) {
                sellerPage.enableMonthTextField();
            } else if (sellerPage.getSellerReportBox().equals("Credibility Report")) {
                sellerPage.disableMonthTextField();
            } else if (sellerPage.getSellerReportBox().equals("Product Popularity Report")) {
                sellerPage.enableMonthTextField();
            }

        }, backButtonEvent -> {
            sellerPage.nextMainPageName(SellerPage.PRODUCTLIST);
            sellerPage.setEnableTopButtons();
        });
    }

    private void initCourierListeners() {
        courierPage.initSignUpListeners(submitSignUpEvent -> {
            String courier_name = courierPage.getCourierName();
            String courier_email_address = courierPage.getCourierEmail();
            String courier_address = courierPage.getCourierAddress();

            if (courier_name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out the required fields:\nName");
            } else {

                if (!courier_email_address.isEmpty()) {
                    if(!emailChecker(courier_email_address)) {
                        JOptionPane.showMessageDialog(null, "Error: Invalid email format.");
                        return;
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

                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, courier_name);
                    pstmt.setString(2, courier_email_address);
                    pstmt.setString(3, courier_address);
                    pstmt.setBoolean(4, !(courier_address.isEmpty() || courier_email_address.isEmpty()));
                                                            // ^ verifies the courier
                    pstmt.executeUpdate();

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

                    account = new Courier(courier_id, courier_name, courier_email_address, courier_address, courier_verified_status);
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

        courierPage.initMainListeners(dateLtr -> {
                try {
                    int year = Integer.parseInt(courierPage.getCourierYear());
                    int month = Integer.parseInt(courierPage.getCourierMonth());
                    courierPage.clearCourierDates();

                    if (year > Integer.parseInt(Year.now().toString()) || !(year >= 1000 && year < 9999) || month < 1 || month > 12) {
                        throw new IllegalArgumentException();
                    }

                    courierPage.updateAOTable(((Courier) account).showCompletedOrders(conn, year, month));
                    courierPage.updateARTable(((Courier) account).showCompletedReturns(conn, year, month));
                    courierPage.nextMainPageName(CourierPage.ACTIVITYPAGE);

                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid Arguments");
                }

            }, orderLtr -> {
                courierPage.updateOOTable(((Courier)account).showOngoingOrders(conn));
                courierPage.updateORTable(((Courier)account).showOngoingReturns(conn));
                courierPage.nextMainPageName(CourierPage.ONGOINGORDERSPAGE);

            }, actEvent -> courierPage.nextMainPageName(CourierPage.DATEPAGE)

            , deliverEvent -> {
                int update = courierPage.getRowToUpdate();

                if (update != -1) {
                    ((Courier) account).deliverOrder(conn, update);
                    JOptionPane.showMessageDialog(null, "Delivery Successful");
                    courierPage.updateOOTable(((Courier) account).showOngoingOrders(conn));
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Invalid or no row selected");
                }

            }, editEvent -> {
                courierPage.nextMainPageName(CourierPage.EDITPAGE);
                courierPage.updateProfilePage((Courier) account);

            }, saveEvent -> {
                try {
                    Courier courier = (Courier) account;

                    String updName = courierPage.getProfileCourierName();
                    String updEmail = courierPage.getProfileCourierEmail();

                    if (!updName.isEmpty() && (updEmail.isEmpty() || emailChecker(updEmail))) {
                        courier.setName(courierPage.getProfileCourierName());
                        courier.setEmail(courierPage.getProfileCourierEmail());
                        courier.setAddress(courierPage.getProfileCourierAddress());

                        courier.updateAccount(conn);
                        JOptionPane.showMessageDialog(null, "Profile changed...");

                    } else {
                        throw new IllegalArgumentException("Invalid arguments.");
                    }

                } catch (SQLException | IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }

                }, logOutEvent -> {
                mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
                selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
        });
    }

    private boolean emailChecker(String courier_email_address) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(courier_email_address);

        return matcher.matches();
    }

    private boolean phoneChecker(String user_phone_number) {
        Pattern pattern = Pattern.compile("^\\d{11}$");
        Matcher matcher = pattern.matcher(user_phone_number);

        return user_phone_number.isEmpty() || matcher.matches();
    }
}