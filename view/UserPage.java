package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPage extends JPanel implements AccountPage {
    private CardLayout userCardLayout;
    private JTextField userNameField,
                       userFirstNameField,
                       userLastNameField,
                       userAddressField,
                       userPhoneField;
    private JButton    submitSignUpBtn,
                       userBackBtn;
    private JLabel     errorLbl;

    public UserPage() {
        this.userCardLayout = new CardLayout();
        this.setLayout(userCardLayout);

        this.add(getSignUpPage(), "signup");
        this.add(getMainPage(), "main");
    }

    @Override
    public JPanel getSignUpPage() {
        JPanel signUpPage = new JPanel();
        signUpPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "User sign-up", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        JLabel label = new JLabel("Enter user account name: ");
        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(200, 20));

        signUpPage.add(label);
        signUpPage.add(userNameField);

        label = new JLabel("Enter user first name: ");
        userFirstNameField = new JTextField();
        userFirstNameField.setPreferredSize(new Dimension(200, 20));

        signUpPage.add(label);
        signUpPage.add(userFirstNameField);

        label = new JLabel("Enter user last name: ");
        userLastNameField = new JTextField();
        userLastNameField.setPreferredSize(new Dimension(200, 20));

        signUpPage.add(label);
        signUpPage.add(userLastNameField);

        label = new JLabel("Enter user address: ");
        userAddressField = new JTextField();
        userAddressField.setPreferredSize(new Dimension(200, 20));

        signUpPage.add(label);
        signUpPage.add(userAddressField);

        label = new JLabel("Enter phone number:  ");
        userPhoneField = new JTextField();
        userPhoneField.setPreferredSize(new Dimension(200, 20));

        signUpPage.add(label);
        signUpPage.add(userPhoneField);

        submitSignUpBtn = new JButton("Submit");
        submitSignUpBtn.setFocusable(false);
        signUpPage.add(submitSignUpBtn);

        userBackBtn = new JButton("Back");
        userBackBtn.setFocusable(false);
        signUpPage.add(userBackBtn);

        errorLbl = new JLabel();
        signUpPage.add(errorLbl);

        return signUpPage;
    }

    @Override
    public JPanel getMainPage() {
        JPanel userPage = new JPanel();

        return userPage;
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        userBackBtn.addActionListener(backLtr);

    }

    @Override
    public void nextPageName(String name) {
        this.userCardLayout.show(this, name);
    }

    public void setErrorLbl(String text) { this.errorLbl.setText(text); }
    public String getUserName() { return userNameField.getText(); }
    public String getUserFirstName() { return userNameField.getText(); }
    public String getUserLastName() { return userNameField.getText(); }
    public String getUserAddress() { return userNameField.getText(); }
    public String getUserPhone() { return userPhoneField.getText(); }
}
