package view;

import schemaobjects.Account;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainController {
    private final MainMenu mainMenuPage;

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
        ActionListener submitSignUpLtr = signUpEvent -> {
            account.signUp(conn);

            mainMenuPage.addToCenterPanel(account.getPage(), account.toString() + "main");
            mainMenuPage.nextPageName(account.toString() + "main");
        };

        mainMenuPage.initSelectListeners(loginEvent -> {    // Happens when you press login button
            this.account = mainMenuPage.getAccountType();

            assert (account != null);

            mainMenuPage.nextAccountPageName("login");
        }, signUpEvent -> {    // Happens when you press sign up button
            this.account = mainMenuPage.getAccountType();

            assert (account != null);

            mainMenuPage.addToCenterPanel(account.getSignUpPage(), account.toString() + "signup");
            account.initSignUpListeners(submitSignUpLtr);
            mainMenuPage.nextPageName(account.toString() + "signup");

        }, submitLoginEvent -> {        // Happens when you press submit in login page
            if (account.login(Integer.parseInt(mainMenuPage.getID()), conn)) {
                mainMenuPage.addToCenterPanel(account.getPage(), account.toString() + "main");
                mainMenuPage.nextPageName(account.toString() + "main");
            } else {
                // Could also cause an error while logging in (returns false)
            }

        }, backEvent -> this.mainMenuPage.nextPageName("conn"));
    }

    private void initAccountListeners() {

    }
}
