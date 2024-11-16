package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainMenu extends JFrame {
    private JPanel menu;
    private JLabel text;
    private JButton login;
    private JButton signup;
    private JButton exit;


    private JPanel connPanel;
    private JLabel userText;
    private JLabel passText;
    private JTextField username;
    private JTextField password;
    private JButton submit;

    public MainMenu() {
        this.setTitle("Login");
        this.setSize(500,300);
        this.setLayout(new BorderLayout(10,10));

        this.connPanel = new JPanel(new GridBagLayout());
        this.connPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "SQL Login", TitledBorder.CENTER, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        this.userText = new JLabel("Username: ");
        userText.setVisible(true);
        userText.setHorizontalTextPosition(JLabel.CENTER);
        userText.setVerticalTextPosition(JLabel.TOP);
        userText.setForeground(Color.BLACK);

        this.passText = new JLabel("Password: ");
        passText.setVisible(true);
        passText.setHorizontalTextPosition(JLabel.CENTER);
        passText.setVerticalTextPosition(JLabel.TOP);
        passText.setForeground(Color.BLACK);

        this.username = new JTextField();
        username.setPreferredSize(new Dimension(200, 20));

        this.password = new JTextField();
        password.setPreferredSize(new Dimension(200, 20));

        this.submit = new JButton("Submit");
        this.exit = new JButton("Exit");

        connPanel.add(userText, gbc);
        gbc.gridx = 1;
        connPanel.add(username, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        connPanel.add(passText, gbc);
        gbc.gridx = 1;
        connPanel.add(password, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        connPanel.add(submit, gbc);
        gbc.gridx = 1;
        connPanel.add(exit, gbc);

        this.add(connPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private boolean showOptions() {
        this.setTitle("Main Menu");
        this.setSize(500,500);
        this.setLayout(new BorderLayout(10,10));

        this.menu = new JPanel(new GridBagLayout());
        this.menu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Main Menu", TitledBorder.CENTER, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        this.text = new JLabel("Select an option:");
        text.setFont(new Font("Montserrat", Font.PLAIN, 25));
        text.setVisible(true);
        text.setHorizontalTextPosition(JLabel.CENTER);
        text.setVerticalTextPosition(JLabel.TOP);
        text.setForeground(Color.BLACK);

        this.login = new JButton("Login");
        this.signup = new JButton("Sign Up");
        this.exit = new JButton("Exit");

        menu.add(text, gbc);
        gbc.gridy = 1;
        menu.add(login, gbc);
        gbc.gridy = 2;
        menu.add(signup, gbc);
        gbc.gridy = 3;
        menu.add(exit, gbc);

        this.add(menu);
        this.remove(connPanel);
        return true;
    }

    public JButton getSubmit() {
        return submit;
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }
}
