package view;

import model.Account;
import model.Courier;
import model.Seller;
import model.User;

import java.sql.*;
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
                connectPage.setErrorLbl("Error: " + e.getMessage());
            }

        }, exitEvent -> {
            try {
                if (conn != null) {
                    this.conn.close();
                }
                System.exit(0);

            } catch (Exception e) {
                connectPage.setErrorLbl("Error: " + e.getMessage());
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
            if (account.login(Integer.parseInt(selectAccountPage.getID()), conn)) {
                (account instanceof User ? userPage :
                 account instanceof Seller ? sellerPage :
                 courierPage).nextPageName(AccountPage.MAINPAGE);

                mainMenuPage.nextPageName(account.toString());
            } else {
                selectAccountPage.setErrorLbl("Error: Account was not found");
            }

        }, backLoginEvent -> selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE)
        , backEvent -> mainMenuPage.nextPageName(MainFrame.CONNECTPAGE));
    }

    private void initUserListeners() {
        userPage.initSignUpListeners(submitSignUpEvent -> {
            String user_name = userPage.getUserName();
            String user_firstname = userPage.getUserFirstName();
            String user_lastname = userPage.getUserLastName();
            String user_address = userPage.getUserAddress();

            String user_phone_number = userPage.getUserPhone();

            Pattern pattern = Pattern.compile("^\\d{11}$");
            Matcher matcher = pattern.matcher(user_phone_number);

            while (!matcher.matches()) {
                userPage.setErrorLbl("Invalid Phone Number!\nRe-Enter phone number: ");
                user_phone_number = userPage.getUserPhone();
                matcher = pattern.matcher(user_phone_number);
            }

            Date user_creation_date = new Date(System.currentTimeMillis());
            boolean user_verified_status = false;

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
                userPage.setErrorLbl("Error: " + e.getMessage());
            }

        }, backSignUpEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            userPage.nextPageName(AccountPage.SIGNUPPAGE);
        });

        userPage.initMainListeners(shopEvent -> {
            userPage.nextMainPageName(UserPage.SHOPPAGE);

            // GET THE LIST OF PRODUCTS AND REFLECT ON THE J-LIST
        }, cartEvent -> {
            userPage.nextMainPageName(UserPage.CARTPAGE);

        }, ordersEvent -> {
            userPage.nextMainPageName(UserPage.ORDERSPAGE);

        }, profileEvent -> {
            userPage.nextMainPageName(UserPage.PROFILEPAGE);

        }, logOutEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);

        }, addToCartEvent -> {

        }, viewInfoEvent -> {

        }, checkOutEvent -> {

        }, removeItemEvent -> {

        }, returnEvent -> {

        }, rateEvent -> {

        }, receiveEvent -> {

        }, saveChangeEvent -> {

        });
    }

    private void initSellerListeners() {
        sellerPage.initSignUpListeners(submitSignUpEvent -> {
            String seller_name = sellerPage.getSellerName();
            String seller_address = sellerPage.getSellerAddress();
            String seller_phone_number = sellerPage.getSellerPhone();

            Pattern pattern = Pattern.compile("^\\d{11}$");
            Matcher matcher = pattern.matcher(seller_phone_number);

            while (!matcher.matches()) {
                sellerPage.setErrorLbl("Invalid Phone Number!\nRe-Enter phone number: ");
                seller_phone_number = sellerPage.getSellerPhone();
                matcher = pattern.matcher(seller_phone_number);
            }

            Date seller_creation_date = new Date(System.currentTimeMillis());
            boolean seller_verified_status = false;

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

            } catch (Exception e) {
                sellerPage.setErrorLbl("Error during seller sign-up: " + e.getMessage());
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
                    System.out.print("Invalid Email!\n Re-Enter email address:  "); // THIS MUST BE CHANGED
                    courier_email_address = courierPage.getCourierEmail();
                    matcher = pattern.matcher(courier_email_address);
                }
            }

            boolean courier_verified_status = false;

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

            } catch (Exception e){
                courierPage.setErrorLbl("Error: " + e.getMessage());
            }

        }, backSignUpEvent -> {
            mainMenuPage.nextPageName(MainFrame.SELECTACCPAGE);
            selectAccountPage.nextPageName(SelectAccount.SELECTACCPAGE);
            courierPage.nextPageName(AccountPage.SIGNUPPAGE);
        });
    }
}
