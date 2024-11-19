package view;

import model.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
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

        mainMenuPage.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
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
            mainMenuPage.nextPageName(account.toString());

        }, submitLoginEvent -> {
            try {
                if (account.login(Integer.parseInt(selectAccountPage.getID()), conn)) {
                    (account instanceof User ? userPage :
                    account instanceof Seller ? sellerPage :
                    courierPage).nextPageName(AccountPage.MAINPAGE);

                    mainMenuPage.nextPageName(account.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Account was not found.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Enter valid ID.");
            }

        }, backLoginEvent -> selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE)
        , backEvent -> mainMenuPage.nextPageName(MainFrame.CONNECTPAGE));
    }

    private void initUserListeners() {
        userPage.initSignUpListeners(submitSignUpEvent -> { // Action: Pressing the submit button in the sign-up page
            String user_name = userPage.getUserName();
            String user_firstname = userPage.getUserFirstName();
            String user_lastname = userPage.getUserLastName();
            String user_address = userPage.getUserAddress();

            String user_phone_number = userPage.getUserPhone();

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

            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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

            // update
        }, ordersEvent -> { // Action: Pressing orders button
            userPage.nextMainPageName(UserPage.ORDERSPAGE);

            // update
        }, profileEvent -> { // Action: Pressing profile button
            userPage.nextMainPageName(UserPage.PROFILEPAGE);
            userPage.updateProfilePage((User) account);

        }, logOutEvent -> { // Action: Pressing log out button
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            userPage.nextPageName(UserPage.SHOPPAGE);

        }, addToCartEvent -> { // Action: Pressing the add button in the shop page
            Product selectedProduct = userPage.getSelectedProduct();

            if (selectedProduct.isListed()) {

                // Show a pop-up asking for the order quantity
                int orderQuantity = userPage.getQuantity();

                if (orderQuantity == 0) {
                    return;
                }

                if (orderQuantity <= selectedProduct.getQuantity()) {
                    // Add to cart
                    ((User) account).addProductToCart(
                    new OrderContent(selectedProduct.getProductID(), selectedProduct.getName(),orderQuantity, selectedProduct.getPrice()));
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Only " + selectedProduct.getQuantity() + " are in stock");
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

        }, removeItemEvent -> { // Action: Pressing the remove item button in the shopping cart page

        }, returnEvent -> { // Action: Pressing the return item button in the orders page

        }, rateEvent -> { // Action: Pressing the return item button in the orders page

        }, receiveEvent -> { // Action: Pressing the receive item button in the orders page

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

        }, productSelectEvent -> { // Action: Pressing an product in the product list in the shop page
            Product selectedProduct = userPage.getSelectedProduct();

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
            System.out.println("HI3");
            // update orderLbl
        });
    }

    private void initSellerListeners() {
        sellerPage.initSignUpListeners(submitSignUpEvent -> {
            String seller_name = sellerPage.getSellerName();
            String seller_address = sellerPage.getSellerAddress();
            String seller_phone_number = sellerPage.getSellerPhone();
            
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

        }, backSignUpEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);

            sellerPage.nextPageName(AccountPage.SIGNUPPAGE);
        });
    }

    private void initCourierListeners() {
        courierPage.initSignUpListeners(submitSignUpEvent -> {
            String courier_name = courierPage.getCourierName();
            String courier_email_address = courierPage.getCourierEmail();
            String courier_address = courierPage.getCourierAddress();

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
            
            boolean courier_verified_status = false;
            
            if (!courier_email_address.isEmpty() && !courier_address.isEmpty())
                courier_verified_status = true;
            

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

        }, backSignUpEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            courierPage.nextPageName(AccountPage.SIGNUPPAGE);
        });

        courierPage.initMainListeners(profileEvent -> {

        }, logOutEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
        });
    }
}
