package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPage extends JPanel implements AccountPage {
    private CardLayout userCardLayout;

    // Constants used for switching between the different options
    public static final String SHOPPAGE = "shop";
    public static final String CARTPAGE = "cart";
    public static final String ORDERSPAGE = "orders";
    public static final String PROFILEPAGE = "profile";

    // Sign-Up page
    private JTextField userNameField,
                       userFirstNameField,
                       userLastNameField,
                       userAddressField,
                       userPhoneField;

    private JButton    submitSignUpBtn,
                       userBackBtn;
    private JLabel     errorLbl;

    // Main page
    private JPanel     topPanel,
                       bottomPanel;

    private CardLayout mainCardLayout;

    // Shop option
    private JButton    shopBtn,
                       cartBtn,
                       ordersBtn,
                       profileBtn,
                       logOutBtn,
                       addToCartBtn,
                       viewItemBtn;

    private JLabel     productInfoLabel;
    private JComboBox<String> browseByBox;
    private JList<String>     productList;

    // Cart option

    private JTable            cartTable;
    private JButton           checkOutBtn,
                              removeBtn;
    private JLabel            totalLbl;

    // Orders option

    private JComboBox<String> ordersBox;
    private JList<String>     ordersList;

    private JLabel            orderInfoLbl;
    private JButton           returnBtn,
                              rateBtn,
                              receiveBtn;

    // Profile option

    private JTextField        nameField,
                              addressField;
    private JButton           saveChangesBtn;

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
        mainCardLayout = new CardLayout();

        JPanel panel = new JPanel();

        topPanel = new JPanel();
        topPanel.setBackground(Color.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        shopBtn = new JButton("Shop");
        shopBtn.setFocusable(false);

        cartBtn = new JButton("Cart");
        cartBtn.setFocusable(false);

        ordersBtn = new JButton("Orders");
        ordersBtn.setFocusable(false);

        profileBtn = new JButton("Profile");
        profileBtn.setFocusable(false);

        logOutBtn = new JButton("Log out");
        logOutBtn.setFocusable(false);

        topPanel.add(shopBtn);
        topPanel.add(cartBtn);
        topPanel.add(ordersBtn);
        topPanel.add(profileBtn);
        topPanel.add(logOutBtn);

        bottomPanel = new JPanel(mainCardLayout);

        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "User", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        bottomPanel.add(getShopPage(), SHOPPAGE);
        bottomPanel.add(getCartPage(), CARTPAGE);
        bottomPanel.add(getOrdersPage(), ORDERSPAGE);
        bottomPanel.add(getProfilePage(), PROFILEPAGE);

        nextMainPageName(SHOPPAGE);

        panel.add(topPanel);
        panel.add(bottomPanel);

        return panel;
    }

    public JPanel getShopPage() {
        JPanel panel = new JPanel();

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Product information", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        productInfoLabel = new JLabel();
        infoPanel.add(productInfoLabel);

        JLabel label = new JLabel("Browse by: ");
        panel.add(label);

        String[] browseOptions = { "By shop", "By product type" };
        browseByBox = new JComboBox<>(browseOptions);
        panel.add(browseByBox);

        productList = new JList<>(new DefaultListModel<>());
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setFocusable(false);

        JScrollPane productListPane = new JScrollPane(productList);
        productListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productListPane.setPreferredSize(new Dimension(400, 300));

        panel.add(productListPane);

        addToCartBtn = new JButton("Add");
        addToCartBtn.setFocusable(false);

        viewItemBtn = new JButton("View info");
        viewItemBtn.setFocusable(false);

        panel.add(addToCartBtn);
        panel.add(viewItemBtn);

        return panel;
    }

    public JPanel getCartPage() {
        JPanel panel = new JPanel();

        String[] columnHeader = {""};           // TENTATIVE
        String[][] data = new String[25][2];

        cartTable = new JTable(data, columnHeader);
        cartTable.setDefaultEditor(Object.class, null);
        cartTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane cartTablePane = new JScrollPane(cartTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        cartTablePane.setPreferredSize(new Dimension(400, 300));
        panel.add(cartTablePane);

        checkOutBtn = new JButton("Check out");
        checkOutBtn.setFocusable(false);
        panel.add(checkOutBtn);

        removeBtn = new JButton("Remove");
        removeBtn.setFocusable(false);
        panel.add(removeBtn);

        JLabel lbl = new JLabel("Total: ");
        panel.add(lbl);

        totalLbl = new JLabel();
        panel.add(totalLbl);
        return panel;
    }

    public JPanel getOrdersPage() {
        JPanel panel = new JPanel();

        ordersList = new JList<>(new DefaultListModel<>());
        ordersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersList.setFocusable(false);

        JScrollPane ordersListPane = new JScrollPane(productList);
        ordersListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ordersListPane.setPreferredSize(new Dimension(400, 300));

        panel.add(ordersListPane);

        orderInfoLbl = new JLabel();

        JPanel infoPanel = new JPanel();
        infoPanel.add(orderInfoLbl);       // USE THIS FOR SETTING THE ORDER INFO PACK IT INTO ONE STRING

        String[] orderTypes = { "Orders", "Returns" };
        ordersBox = new JComboBox<>(orderTypes);
        panel.add(ordersBox);

        returnBtn = new JButton("Return");
        returnBtn.setFocusable(false);
        panel.add(returnBtn);

        rateBtn = new JButton("Rate");
        rateBtn.setFocusable(false);
        panel.add(rateBtn);

        receiveBtn = new JButton("Receive");
        receiveBtn.setFocusable(false);
        panel.add(receiveBtn);

        return panel;
    }

    public JPanel getProfilePage() {
        JPanel panel = new JPanel();

        JLabel lbl = new JLabel("Name: ");
        nameField = new JTextField();
        panel.add(lbl);
        panel.add(nameField);

        lbl = new JLabel("Address: ");
        addressField = new JTextField();
        panel.add(lbl);
        panel.add(addressField);

        saveChangesBtn = new JButton("Save Changes");
        saveChangesBtn.setFocusable(false);
        panel.add(saveChangesBtn);

        return panel;
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        userBackBtn.addActionListener(backLtr);

    }

    public void initMainListeners(ActionListener shopLtr, ActionListener cartLtr, ActionListener ordersLtr, ActionListener profLtr, ActionListener logOutLtr,
    ActionListener addLtr, ActionListener viewInfoLtr, ActionListener checkOutLtr, ActionListener removeLtr, ActionListener returnLtr, ActionListener rateLtr,
    ActionListener receiveLtr, ActionListener saveChangesLtr) {
        shopBtn.addActionListener(shopLtr);
        cartBtn.addActionListener(cartLtr);
        ordersBtn.addActionListener(ordersLtr);
        profileBtn.addActionListener(profLtr);
        logOutBtn.addActionListener(logOutLtr);

        addToCartBtn.addActionListener(addLtr);
        viewItemBtn.addActionListener(viewInfoLtr);

        checkOutBtn.addActionListener(checkOutLtr);
        removeBtn.addActionListener(removeLtr);

        returnBtn.addActionListener(returnLtr);
        rateBtn.addActionListener(rateLtr);
        receiveBtn.addActionListener(receiveLtr);

        saveChangesBtn.addActionListener(saveChangesLtr);
    }

    @Override
    public void nextPageName(String name) {
        this.userCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

//    public void setDataValue() {      USE THIS FOR SETTING PRODUCTS IN THE CART TABLE
//        cartTable.setValueAt();
//    }

    public void setErrorLbl(String text) { this.errorLbl.setText(text); }
    public void setProductInfo(String text) { this.productInfoLabel.setText(text); }
    public void setOrderInfo(String text) { this.orderInfoLbl.setText(text); }

    public String getUserName() { return userNameField.getText().trim(); }
    public String getUserFirstName() { return userNameField.getText().trim(); }
    public String getUserLastName() { return userNameField.getText().trim(); }
    public String getUserAddress() { return userNameField.getText().trim(); }
    public String getUserPhone() { return userPhoneField.getText().trim(); }

    public String getBrowseByOption() { return (String) browseByBox.getSelectedItem(); }
    public String getSelectedProduct() { return productList.getSelectedValue(); }

    public void addProductToList(String input) { ((DefaultListModel<String>) productList.getModel()).addElement(input); }
    public void removeProductFromList(String input) { ((DefaultListModel<String>) productList.getModel()).removeElement(input); }

    public void addOrdersToList(String input) { ((DefaultListModel<String>) ordersList.getModel()).addElement(input); }
    public void removeOrdersFromList(String input) { ((DefaultListModel<String>) ordersList.getModel()).removeElement(input); }
}
