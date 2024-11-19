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

public class SelectAccount extends JPanel {
    public CardLayout selectCardLayout;

    // Constants used for the main select acc page and login page
    public static final String SELECTACCPAGE = "select";
    public static final String LOGINPAGE = "login";

    private JComboBox<String> accountTypeBox;
    private JButton loginBtn;
    private JButton signupBtn;
    private JButton backBtn;
    private JTextField idLoginField;
    private JButton submitLoginBtn;

    private JButton backLoginBtn;

    public SelectAccount() {
        this.selectCardLayout = new CardLayout();
        this.setLayout(selectCardLayout);

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

        DefaultListCellRenderer dLCR = new DefaultListCellRenderer();
        dLCR.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        String[] accountTypes = { "User", "Courier", "Seller" };
        this.accountTypeBox = new JComboBox<>(accountTypes);
        this.accountTypeBox.setPreferredSize(new Dimension(200,40));
        this.accountTypeBox.setRenderer(dLCR);
        this.accountTypeBox.setFont(new Font("Verdana", Font.BOLD, 18));

        this.loginBtn = new JButton("Login");
        this.loginBtn.setFocusable(false);
        this.loginBtn.setContentAreaFilled(false);
        this.loginBtn.setPreferredSize(new Dimension(100,40));
        this.signupBtn = new JButton("Sign Up");
        this.signupBtn.setFocusable(false);
        this.signupBtn.setContentAreaFilled(false);
        this.signupBtn.setPreferredSize(new Dimension(100,40));
        this.backBtn = new JButton("Back");
        this.backBtn.setFocusable(false);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.setPreferredSize(new Dimension(100,40));

        accountSelectPage.add(textLbl, gbc);
        gbc.gridy = 1;
        accountSelectPage.add(accountTypeBox, gbc);
        gbc.gridy = 2;
        accountSelectPage.add(loginBtn, gbc);
        gbc.gridy = 3;
        accountSelectPage.add(signupBtn, gbc);
        gbc.gridy = 4;
        accountSelectPage.add(backBtn, gbc);

        JPanel loginPage = new JPanel(new GridBagLayout());
        loginPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Account Login", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        textLbl = new JLabel("Enter your ID: ");

        idLoginField = new JTextField();
        idLoginField.setPreferredSize(new Dimension(200, 20));

        submitLoginBtn = new JButton("Submit");
        submitLoginBtn.setFocusable(false);

        backLoginBtn = new JButton("Back");
        backLoginBtn.setFocusable(false);

        loginPage.add(textLbl);
        loginPage.add(idLoginField);
        loginPage.add(submitLoginBtn);
        loginPage.add(backLoginBtn);

        this.add(accountSelectPage, SELECTACCPAGE);
        this.add(loginPage, LOGINPAGE);
    }

    public void initSelectListeners(ActionListener loginLtr, ActionListener signUpLtr, ActionListener submitLoginLtr, ActionListener backLoginLtr, ActionListener backLtr) {
        this.loginBtn.addActionListener(loginLtr);
        this.signupBtn.addActionListener(signUpLtr);
        this.backBtn.addActionListener(backLtr);
        this.submitLoginBtn.addActionListener(submitLoginLtr);
        this.backLoginBtn.addActionListener(backLoginLtr);
    }

    public void nextPageName(String name) {
        this.selectCardLayout.show(this, name);
    }

    public Account getAccountType() {
        return switch ((String) Objects.requireNonNull(accountTypeBox.getSelectedItem())) {
            case "Seller" -> new Seller();
            case "Courier" -> new Courier();
            case "User" -> new User();
            default -> null;
        };
    }

    public String getID() { return idLoginField.getText().trim(); }
}
