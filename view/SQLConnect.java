package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SQLConnect extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton submitBtn;
    private final JButton exitBtn;

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

        JLabel userLbl = new JLabel("Username: ");
        userLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        userLbl.setForeground(Color.BLACK);

        JLabel passLbl = new JLabel("Password: ");
        passLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        passLbl.setForeground(Color.BLACK);

        this.usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 20));

        this.passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 20));

        this.submitBtn = new JButton("Submit");
        this.submitBtn.setFocusable(false);
        this.submitBtn.setContentAreaFilled(false);
        this.submitBtn.setPreferredSize(new Dimension(100, 40));

        this.exitBtn = new JButton("Exit");
        this.exitBtn.setFocusable(false);
        this.exitBtn.setContentAreaFilled(false);
        this.exitBtn.setPreferredSize(new Dimension(100, 40));

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
        gbc.gridy = 3;
        gbc.gridx = 0;
        this.add(submitBtn, gbc);
        gbc.gridx = 1;
        this.add(exitBtn, gbc);
    }

    public void initConnListeners(ActionListener submitLtr, ActionListener exitLtr) {
        this.submitBtn.addActionListener(submitLtr);
        this.exitBtn.addActionListener(exitLtr);
    }

    public void clearTextFields()
    {
        usernameField.setText("");
        passwordField.setText("");
    }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return new String(passwordField.getPassword()).trim(); }
}
