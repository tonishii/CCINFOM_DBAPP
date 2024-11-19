package view;

import model.Account;
import model.Courier;
import model.Seller;
import model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainMenu extends JFrame {
    private JLabel userLbl;
    private JLabel passLbl;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton submitBtn;
    private JButton exitBtn;

    private JPanel accountPage;
    private CardLayout accountCardLayout;
    private JComboBox<String> accountTypeBox;
    private JButton loginBtn;
    private JButton signupBtn;
    private JButton backBtn;

    private JTextField idLoginField;
    private JButton submitLoginBtn;

    private JPanel signUpPage;

    private final CardLayout centerCardLayout;
    private final JPanel centerPanel;

    public MainMenu() {
        this.setTitle("E-Commerce Database Application");
        this.setSize(800,700);
        this.setLayout(new BorderLayout(10,10));
        this.setResizable(false);

        JPanel topBanner = new JPanel();
        topBanner.setBackground(Color.PINK);
        topBanner.setPreferredSize(new Dimension(800, 60));
        this.add(topBanner, BorderLayout.NORTH);

        centerCardLayout = new CardLayout(50, 40);
        centerPanel = new JPanel();
        centerPanel.setLayout(centerCardLayout);
        centerPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        this.add(centerPanel, BorderLayout.CENTER);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JPanel getConnectionPage() {
        JPanel connPanel = new JPanel(new GridBagLayout());
        connPanel.setPreferredSize(new Dimension(800, 640));

        connPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "SQL Connection Login", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        this.userLbl = new JLabel("Username: ");
        userLbl.setVisible(true);
        userLbl.setHorizontalTextPosition(JLabel.CENTER);
        userLbl.setVerticalTextPosition(JLabel.TOP);
        userLbl.setForeground(Color.BLACK);

        this.passLbl = new JLabel("Password: ");
        passLbl.setVisible(true);
        passLbl.setHorizontalTextPosition(JLabel.CENTER);
        passLbl.setVerticalTextPosition(JLabel.TOP);
        passLbl.setForeground(Color.BLACK);

        this.usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 20));

        this.passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 20));

        this.submitBtn = new JButton("Submit");
        this.submitBtn.setFocusable(false);
        this.exitBtn = new JButton("Exit");
        this.exitBtn.setFocusable(false);

        connPanel.add(userLbl, gbc);
        gbc.gridx = 1;
        connPanel.add(usernameField, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        connPanel.add(passLbl, gbc);
        gbc.gridx = 1;
        connPanel.add(passwordField, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        connPanel.add(submitBtn, gbc);
        gbc.gridx = 1;
        connPanel.add(exitBtn, gbc);

        return connPanel;
    }

    public JPanel getAccountPage() {
        this.accountCardLayout = new CardLayout();
        this.accountPage = new JPanel(accountCardLayout);

        JPanel accountSelectPage = new JPanel(new GridBagLayout());
        accountSelectPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Account Login/Sign-up", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel textLbl = new JLabel("Account Type: ");
        textLbl.setFont(new Font("Montserrat", Font.PLAIN, 25));
        textLbl.setVisible(true);
        textLbl.setHorizontalTextPosition(JLabel.CENTER);
        textLbl.setVerticalTextPosition(JLabel.TOP);
        textLbl.setForeground(Color.BLACK);

        String[] accountTypes = { "User", "Courier", "Seller" };
        this.accountTypeBox = new JComboBox<>(accountTypes);

        this.loginBtn = new JButton("Login");
        this.signupBtn = new JButton("Sign Up");
        this.backBtn = new JButton("Back");

        accountSelectPage.add(textLbl, gbc);
        gbc.gridy = 1;
        accountSelectPage.add(accountTypeBox, gbc);

        accountSelectPage.add(loginBtn, gbc);
        gbc.gridy = 2;
        accountSelectPage.add(signupBtn, gbc);
        gbc.gridy = 3;
        accountSelectPage.add(backBtn, gbc);

        JPanel loginPage = new JPanel(new GridBagLayout());
        loginPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Account Login", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        textLbl = new JLabel("Enter your ID: ");

        idLoginField = new JTextField();
        idLoginField.setPreferredSize(new Dimension(200, 20));

        submitLoginBtn = new JButton("Submit");
        submitLoginBtn.setFocusable(false);

        loginPage.add(textLbl);
        loginPage.add(idLoginField);
        loginPage.add(submitLoginBtn);

        signUpPage = new JPanel();

        accountPage.add(accountSelectPage, "select");
        accountPage.add(loginPage, "login");
        accountPage.add(signUpPage, "signup");

        return accountPage;
    }

    public void initConnListeners(ActionListener submitLtr, ActionListener exitLtr) {
        this.submitBtn.addActionListener(submitLtr);
        this.exitBtn.addActionListener(exitLtr);
    }

    public void initSelectListeners(ActionListener loginLtr, ActionListener signUpLtr, ActionListener submitLoginLtr, ActionListener backLtr) {
        this.loginBtn.addActionListener(loginLtr);
        this.signupBtn.addActionListener(signUpLtr);
        this.exitBtn.addActionListener(backLtr);
        this.submitLoginBtn.addActionListener(submitLoginLtr);
    }

    public void nextAccountPageName(String name) {
        this.accountCardLayout.show(this.accountPage, name);
    }

    public void nextPageName(String name) {
        this.centerCardLayout.show(this.centerPanel, name);
    }

    public void addToCenterPanel(JPanel panel, String name) {
        this.centerPanel.add(panel, name);
    }

    public Account getAccountType() {
        return switch ((String) Objects.requireNonNull(accountTypeBox.getSelectedItem())) {
            case "Seller" -> new Seller();
            case "Courier" -> new Courier();
            case "User" -> new User();
            default -> null;
        };
    }

    public String getID() { return idLoginField.getText(); }
    public String getUsername() {
        return usernameField.getText();
    }
    public String getPassword() {
        return passwordField.getText();
    }
}
