package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;

public class SQLConnect extends JPanel {
    private JLabel userLbl;
    private JLabel passLbl;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton submitBtn;
    private JButton exitBtn;

    private JLabel errorLbl;

    public SQLConnect() {

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(800, 640));

        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "SQL Connection Login", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.ipadx = 40;
        gbc.ipady = 40;
        gbc.weightx = GridBagConstraints.RELATIVE;
        gbc.weighty = GridBagConstraints.RELATIVE;

        this.userLbl = new JLabel("Username: ");
        userLbl.setFont(new Font("Verdana", FontUIResource.BOLD, 18));
        userLbl.setVisible(true);
        userLbl.setHorizontalTextPosition(JLabel.CENTER);
        userLbl.setVerticalTextPosition(JLabel.TOP);
        userLbl.setForeground(Color.BLACK);

        this.passLbl = new JLabel("Password: ");
        passLbl.setFont(new Font("Verdana", FontUIResource.BOLD, 18));
        passLbl.setVisible(true);
        passLbl.setHorizontalTextPosition(JLabel.CENTER);
        passLbl.setVerticalTextPosition(JLabel.TOP);
        passLbl.setForeground(Color.BLACK);

        this.usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 20));

        this.passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 20));

        this.errorLbl = new JLabel();

        this.submitBtn = new JButton("Submit");
        this.submitBtn.setFocusable(false);
        this.exitBtn = new JButton("Exit");
        this.exitBtn.setFocusable(false);

        this.add(userLbl, gbc);
        gbc.gridx = 1;
        gbc.ipady = 20;
        this.add(usernameField, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.ipady = 40;
        this.add(passLbl, gbc);
        gbc.gridx = 1;
        gbc.ipady = 20;
        this.add(passwordField, gbc);
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.ipady = 40;
        this.add(errorLbl, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        this.add(submitBtn, gbc);
        gbc.gridx = 1;
        this.add(exitBtn, gbc);
    }

    public void initConnListeners(ActionListener submitLtr, ActionListener exitLtr) {
        this.submitBtn.addActionListener(submitLtr);
        this.exitBtn.addActionListener(exitLtr);
    }

    public void setErrorLbl(String text) { errorLbl.setText(text); }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return passwordField.getText().trim(); }
}
