package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SQLConnect extends JPanel {
    private JLabel userLbl;
    private JLabel passLbl;
    private JTextField usernameField;
    private JTextField passwordField;
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

        this.errorLbl = new JLabel();

        this.submitBtn = new JButton("Submit");
        this.submitBtn.setFocusable(false);
        this.exitBtn = new JButton("Exit");
        this.exitBtn.setFocusable(false);

        this.add(userLbl, gbc);
        gbc.gridx = 1;
        this.add(usernameField, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        this.add(passLbl, gbc);
        gbc.gridx = 1;
        this.add(passwordField, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;

        this.add(errorLbl, gbc);

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
