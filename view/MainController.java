package view;

import schemaobjects.Account;

import java.sql.Connection;
import java.sql.DriverManager;

public class MainController {
    private MainMenu mainMenuPage;

    private Connection conn;
    private Account account;

    public MainController() {
        this.mainMenuPage = new MainMenu();

        this.mainMenuPage.addToCenterPanel(mainMenuPage.getConnectionPage(), "conn");
        initConnectionListeners();

        this.mainMenuPage.addToCenterPanel(mainMenuPage.getAccountPage(), "acc");
        initSelectListeners();
        // add the pages here
    }

    private void initConnectionListeners() {
        mainMenuPage.initConnListeners(submitEvent -> {
            try {

                String url = "jdbc:mysql://localhost:3306/mydb";
                String username = mainMenuPage.getUsername();
                String password = mainMenuPage.getPassword();

                Class.forName("com.mysql.cj.jdbc.Driver");
                this.conn = DriverManager.getConnection(url, username, password);

                this.mainMenuPage.nextAccountPageName("select");
                this.mainMenuPage.nextPageName("acc");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }, exitEvent -> {
            try {
                if (!conn.isClosed()) {
                    this.conn.close();
                }
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
    }

    private void initSelectListeners() {
        mainMenuPage.initAccListeners(loginEvent -> {
            this.account = mainMenuPage.getAccountType();

            if (account != null) {
                mainMenuPage.nextAccountPageName("login");

                // Could cause a parsing error
                if (account.login(Integer.parseInt(mainMenuPage.getID()), conn)) {
                    this.mainMenuPage.add(account.displayPage(), account.toString());
                    mainMenuPage.nextPageName(account.toString());
                } else {
                    // Could also cause an error while logging in (returns false)
                }
            } else {

            }

        }, signUpEvent -> {
            this.account = mainMenuPage.getAccountType();

            if (account != null) {
                mainMenuPage.nextAccountPageName("signUp");
//                account.signUp();
            } else {

            }
        }, backEvent -> {
            this.mainMenuPage.nextPageName("conn");
        });
    }

    private void initAccountListeners() {

    }
}
