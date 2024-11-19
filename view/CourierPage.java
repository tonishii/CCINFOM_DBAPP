package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class CourierPage extends JPanel implements AccountPage {
    private CardLayout courierCardLayout;
    private JTextField courierNameField,
                       courierEmailField,
                       courierAddressField;
    private JButton    submitSignUpBtn,
                       courierBackBtn;
    private JLabel     errorLbl;

    // Main page
    private JPanel     topPanel,
                       bottomPanel;

    private JButton    profileBtn,
                       logOutBtn;

    private CardLayout mainCardLayout;

    public CourierPage() {
        this.courierCardLayout = new CardLayout();
        this.setLayout(courierCardLayout);

        this.add(getSignUpPage(), "signup");
        this.add(getMainPage(), "main");
    }

    @Override
    public JPanel getSignUpPage() {
        JPanel signUpPage = new JPanel(new GridBagLayout());
        signUpPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Courier sign-up", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel label = new JLabel("Enter courier name: ");
        courierNameField = new JTextField();
        courierNameField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 0;
        signUpPage.add(label, gbc);
        signUpPage.add(courierNameField, gbc);

        label = new JLabel("Enter email address: ");
        courierEmailField = new JTextField();
        courierEmailField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 1;
        signUpPage.add(label, gbc);
        signUpPage.add(courierEmailField, gbc);

        label = new JLabel("Enter courier address: ");
        courierAddressField = new JTextField();
        courierAddressField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 2;
        signUpPage.add(label,gbc);
        signUpPage.add(courierAddressField,gbc);

        gbc.gridy = 3;
        submitSignUpBtn = new JButton("Submit");
        submitSignUpBtn.setFocusable(false);
        signUpPage.add(submitSignUpBtn,gbc);

        courierBackBtn = new JButton("Back");
        courierBackBtn.setFocusable(false);
        signUpPage.add(courierBackBtn,gbc);

        gbc.gridy = 4;
        errorLbl = new JLabel();
        signUpPage.add(errorLbl,gbc);

        return signUpPage;
    }

    @Override
    public JPanel getMainPage() {
        JPanel courierPage = new JPanel();

        topPanel = new JPanel();
        topPanel.setBackground(Color.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        profileBtn = new JButton("Profile");
        profileBtn.setFocusable(false);

        logOutBtn = new JButton("Log out");
        logOutBtn.setFocusable(false);

        topPanel.add(profileBtn);
        topPanel.add(logOutBtn);

        mainCardLayout = new CardLayout();

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "Courier", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        courierPage.add(topPanel);
        courierPage.add(bottomPanel);

        return courierPage;
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        courierBackBtn.addActionListener(backLtr);
    }

    public void initMainListeners(ActionListener profLtr, ActionListener logOutLtr) {
        profileBtn.addActionListener(profLtr);
        logOutBtn.addActionListener(logOutLtr);
    }

    @Override
    public void nextPageName(String name) {
        this.courierCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

    public void setErrorLbl(String text) { this.errorLbl.setText(text); }

    public String getCourierName() { return courierNameField.getText().trim(); }
    public String getCourierEmail() { return courierEmailField.getText().trim(); }
    public String getCourierAddress() { return courierAddressField.getText().trim(); }
}
