package view;

import model.Courier;
import model.Order;
import model.Return;
import model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CourierPage extends JPanel implements AccountPage {
    private CardLayout courierCardLayout;

    // Constants used for switching between the different options
    public static final String ONGOINGORDERSPAGE = "ongoing";
    public static final String ACTIVITYPAGE = "activity";
    public static final String EDITPAGE = "edit";

    private JTextField  courierNameField,
                        courierEmailField,
                        courierAddressField,
                        profileCourierNameField,
                        profileCourierEmailField,
                        profileCourierAddressField;

    private JButton    submitSignUpBtn,
                       courierBackBtn;

    // Main page
    private JPanel     topPanel,
                       bottomPanel;

    private JButton     ongoingOrderBtn,
                        activityBtn,
                        editBtn,
                        logOutBtn,
                        saveBtn;

    private JTable      ongoingTable,
                        returnsTable;

    private JScrollPane ongoingPane,
                        returnsPane;

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
        signUpPage.add(courierAddressField, gbc);

        gbc.gridy = 3;
        submitSignUpBtn = new JButton("Submit");
        submitSignUpBtn.setFocusable(false);
        signUpPage.add(submitSignUpBtn, gbc);

        courierBackBtn = new JButton("Back");
        courierBackBtn.setFocusable(false);
        signUpPage.add(courierBackBtn, gbc);

        return signUpPage;
    }

    @Override
    public JPanel getMainPage() {
        JPanel courierPage = new JPanel();

        topPanel = new JPanel();
        topPanel.setBackground(Color.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        ongoingOrderBtn = new JButton("Ongoing Orders");
        ongoingOrderBtn.setFocusable(false);

        activityBtn = new JButton("Activity Report");
        activityBtn.setFocusable(false);

        editBtn = new JButton("Edit Account");
        editBtn.setFocusable(false);

        logOutBtn = new JButton("Log out");
        logOutBtn.setFocusable(false);

        topPanel.add(ongoingOrderBtn);
        topPanel.add(activityBtn);
        topPanel.add(editBtn);
        topPanel.add(logOutBtn);

        mainCardLayout = new CardLayout();

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "Courier", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        bottomPanel.add(getOngoingOrdersPage(), ONGOINGORDERSPAGE);
        bottomPanel.add(getEditPage(), EDITPAGE);

        courierPage.add(topPanel);
        courierPage.add(bottomPanel);

        return courierPage;
    }

    public JPanel getOngoingOrdersPage() {
        JPanel oOPage = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel ongoingOrdersLabel = new JLabel("Ongoing Orders:");

        OrderClassTableModel oCTM = new OrderClassTableModel(new ArrayList<>());
        ongoingTable = new JTable(oCTM);
        ongoingTable.setDefaultEditor(Object.class, null);

        this.ongoingPane = new JScrollPane(ongoingTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ongoingPane.setPreferredSize(new Dimension(550, 200));

        JLabel ongoingReturnsLabel = new JLabel("Ongoing Returns:");

        ReturnTableModel rTM = new ReturnTableModel(new ArrayList<>());
        returnsTable = new JTable(rTM);
        returnsTable.setDefaultEditor(Object.class, null);

        this.returnsPane = new JScrollPane(returnsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        returnsPane.setPreferredSize(new Dimension(550, 200));

        oOPage.add(ongoingOrdersLabel, gbc);
        gbc.gridy = 1;
        oOPage.add(ongoingPane, gbc);
        gbc.gridy = 2;
        oOPage.add(ongoingReturnsLabel, gbc);
        gbc.gridy = 3;
        oOPage.add(returnsPane, gbc);

        return oOPage;
    }

    public JPanel getEditPage() {
        JPanel panel = new JPanel();

        JLabel lbl = new JLabel("User details: ");
        panel.add(lbl);

        lbl = new JLabel("Account name: ");
        profileCourierNameField = new JTextField();
        profileCourierNameField.setPreferredSize(new Dimension(200, 20));
        panel.add(lbl);
        panel.add(profileCourierNameField);

        lbl = new JLabel("Email Address: ");
        profileCourierEmailField = new JTextField();
        profileCourierEmailField.setPreferredSize(new Dimension(200, 20));
        panel.add(lbl);
        panel.add(profileCourierEmailField);

        lbl = new JLabel("Address: ");
        profileCourierAddressField = new JTextField();
        profileCourierAddressField.setPreferredSize(new Dimension(200, 20));
        panel.add(lbl);
        panel.add(profileCourierAddressField);

        saveBtn = new JButton("Save Changes");
        saveBtn.setFocusable(false);
        panel.add(saveBtn);

        return panel;
    }

    public void updateProfilePage(Courier courier) {
        profileCourierNameField.setText(courier.getName());
        profileCourierEmailField.setText(courier.getEmailAddress());
        profileCourierAddressField.setText(courier.getAddress());
    }
    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        courierBackBtn.addActionListener(backLtr);
    }

    public void initMainListeners(ActionListener orderLtr, ActionListener actLtr, ActionListener editLtr, ActionListener logOutLtr) {
        ongoingOrderBtn.addActionListener(orderLtr);
        activityBtn.addActionListener(actLtr);
        editBtn.addActionListener(editLtr);
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

    public void clearTextFields() {
        courierNameField.setText("");
        courierEmailField.setText("");
        courierAddressField.setText("");
    }

    public void updateOOTable(ArrayList<Order> orders) {
        OrderClassTableModel mdl = new OrderClassTableModel(orders);
        ongoingTable.setModel(mdl);
        ongoingPane.setViewportView(ongoingTable);
    }

    public void updateORTable(ArrayList<Return> returns) {
        ReturnTableModel mdl = new ReturnTableModel(returns);
        returnsTable.setModel(mdl);
        returnsPane.setViewportView(returnsTable);
    }

    public String getCourierName() { return courierNameField.getText().trim(); }
    public String getCourierEmail() { return courierEmailField.getText().trim(); }
    public String getCourierAddress() { return courierAddressField.getText().trim(); }
}
