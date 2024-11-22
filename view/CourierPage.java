package view;

import model.Courier;
import model.Order;
import model.Return;

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

    private JTable      ongoingOrdersTable,
                        ongoingReturnsTable,
                        activityOrdersTable,
                        activityReturnsTable;

    private JScrollPane ongoingOrdersPane,
                        ongoingReturnsPane,
                        activityOrdersPane,
                        activityReturnsPane;

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
        courierPage.setBackground(Colors.WHITE);

        topPanel = new JPanel();
        topPanel.setBackground(Colors.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        ongoingOrderBtn = new JButton("Ongoing Orders");
        Colors.setButtonUI(ongoingOrderBtn);
        ongoingOrderBtn.setFocusable(false);

        activityBtn = new JButton("Activity Report");
        Colors.setButtonUI(activityBtn);
        activityBtn.setFocusable(false);

        editBtn = new JButton("Edit Account");
        Colors.setButtonUI(editBtn);
        editBtn.setFocusable(false);

        logOutBtn = new JButton("Log out");
        Colors.setButtonUI(logOutBtn);
        logOutBtn.setFocusable(false);

        topPanel.add(ongoingOrderBtn);
        topPanel.add(activityBtn);
        topPanel.add(editBtn);
        topPanel.add(logOutBtn);

        mainCardLayout = new CardLayout();

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "Courier", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        bottomPanel.add(getOngoingOrdersPage(), ONGOINGORDERSPAGE);
        bottomPanel.add(getEditPage(), EDITPAGE);
        bottomPanel.add(getActivityPage(), ACTIVITYPAGE);

        courierPage.add(topPanel);
        courierPage.add(bottomPanel);

        return courierPage;
    }

    public JPanel getOngoingOrdersPage() {
        JPanel oOPage = new JPanel(new GridBagLayout());
        oOPage.setBackground(Colors.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel ongoingOrdersLabel = new JLabel("Ongoing Orders:");

        OrderClassTableModel oCTM = new OrderClassTableModel(new ArrayList<>());
        ongoingOrdersTable = new JTable(oCTM);
        ongoingOrdersTable.setDefaultEditor(Object.class, null);

        this.ongoingOrdersPane = new JScrollPane(ongoingOrdersTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ongoingOrdersPane.setPreferredSize(new Dimension(550, 200));

        JLabel ongoingReturnsLabel = new JLabel("Ongoing Returns:");

        ReturnTableModel rTM = new ReturnTableModel(new ArrayList<>());
        ongoingReturnsTable = new JTable(rTM);
        ongoingReturnsTable.setDefaultEditor(Object.class, null);

        this.ongoingReturnsPane = new JScrollPane(ongoingReturnsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ongoingReturnsPane.setPreferredSize(new Dimension(550, 200));

        oOPage.add(ongoingOrdersLabel, gbc);
        gbc.gridy = 1;
        oOPage.add(ongoingOrdersPane, gbc);
        gbc.gridy = 2;
        oOPage.add(ongoingReturnsLabel, gbc);
        gbc.gridy = 3;
        oOPage.add(ongoingReturnsPane, gbc);

        return oOPage;
    }

    public JPanel getActivityPage() {
        JPanel activityPanel = new JPanel(new GridBagLayout());
        activityPanel.setBackground(Colors.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel activityMainLabel = new JLabel("Activity Report");
        JLabel activityOrdersLabel = new JLabel("Completed Orders:");

        OrderClassTableModel oCTM = new OrderClassTableModel(new ArrayList<>());
        activityOrdersTable = new JTable(oCTM);
        activityOrdersTable.setDefaultEditor(Object.class, null);

        this.activityOrdersPane = new JScrollPane(activityOrdersTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        activityOrdersPane.setPreferredSize(new Dimension(550, 200));

        JLabel activityReturnsLabel = new JLabel("Completed Returns:");

        ReturnTableModel rTM = new ReturnTableModel(new ArrayList<>());
        activityReturnsTable = new JTable(rTM);
        activityReturnsTable.setDefaultEditor(Object.class, null);

        this.activityReturnsPane = new JScrollPane(activityReturnsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        activityReturnsPane.setPreferredSize(new Dimension(550, 200));

        activityPanel.add(activityMainLabel, gbc);
        gbc.gridy = 1;
        activityPanel.add(activityOrdersLabel, gbc);
        gbc.gridy = 2;
        activityPanel.add(activityOrdersPane, gbc);
        gbc.gridy = 3;
        activityPanel.add(activityReturnsLabel, gbc);
        gbc.gridy = 4;
        activityPanel.add(activityReturnsPane, gbc);

        return activityPanel;
    }

    public JPanel getEditPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Colors.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lbl = new JLabel("[USER DETAILS]");
        panel.add(lbl, gbc);

        lbl = new JLabel("Account name: ");
        profileCourierNameField = new JTextField();
        profileCourierNameField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileCourierNameField, gbc);

        lbl = new JLabel("Email Address: ");
        profileCourierEmailField = new JTextField();
        profileCourierEmailField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileCourierEmailField, gbc);

        lbl = new JLabel("Address: ");
        profileCourierAddressField = new JTextField();
        profileCourierAddressField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileCourierAddressField, gbc);

        saveBtn = new JButton("Save Changes");
        Colors.setButtonUI(saveBtn);
        saveBtn.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

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

    public void initMainListeners(ActionListener orderLtr, ActionListener actLtr, ActionListener editLtr, ActionListener saveLtr, ActionListener logOutLtr) {
        ongoingOrderBtn.addActionListener(orderLtr);
        activityBtn.addActionListener(actLtr);
        editBtn.addActionListener(editLtr);
        saveBtn.addActionListener(saveLtr);
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
        ongoingOrdersTable.setModel(mdl);
        ongoingOrdersPane.setViewportView(ongoingOrdersTable);
    }

    public void updateORTable(ArrayList<Return> returns) {
        ReturnTableModel mdl = new ReturnTableModel(returns);
        ongoingReturnsTable.setModel(mdl);
        ongoingReturnsPane.setViewportView(ongoingReturnsTable);
    }

    public void updateAOTable(ArrayList<Order> orders) {
        OrderClassTableModel mdl = new OrderClassTableModel(orders);
        activityOrdersTable.setModel(mdl);
        activityOrdersPane.setViewportView(activityOrdersTable);
    }

    public void updateARTable(ArrayList<Return> returns) {
        ReturnTableModel mdl = new ReturnTableModel(returns);
        activityReturnsTable.setModel(mdl);
        activityReturnsPane.setViewportView(activityReturnsTable);
    }

    public String getCourierName() { return courierNameField.getText().trim(); }
    public String getCourierEmail() { return courierEmailField.getText().trim(); }
    public String getCourierAddress() { return courierAddressField.getText().trim(); }

    public String getProfileCourierName() {
        return profileCourierNameField.getText().trim();
    }

    public String getProfileCourierEmail() {
        return profileCourierEmailField.getText().trim();
    }

    public String getProfileCourierAddress() {
        return profileCourierAddressField.getText().trim();
    }
}
