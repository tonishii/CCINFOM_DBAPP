package view;

import model.Order;
import model.OrderContent;
import model.Product;
import model.User;
import model.Return;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.*;

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
                       addToCartBtn;

    private JLabel              productInfoLabel;
    private JComboBox<String>   browseByBox;
    private JList<String>       browseByList;
    private Map<String, String> options;
    private JList<Product>      productList;

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
    
    // For request return, receive order, and product rating panels
    
    private JTextField          orderInp,
                                prodInp;
    private JComboBox<String>   comboBox;
    private JComboBox<Integer>  intComboBox;
    private JTextArea           descInp;
    private JSpinner            spinner;

    // Profile option

    private JTextField        profileNameField,
                              profilePhoneField,
                              profileAddressField,
                              profileFirstNameField,
                              profileLastNameField;

    private JButton           saveChangesBtn;

    private JScrollPane productListPane;

    public UserPage() {
        this.userCardLayout = new CardLayout();
        this.setLayout(userCardLayout);

        this.add(getSignUpPage(), "signup");
        this.add(getMainPage(), "main");
    }

    @Override
    public JPanel getSignUpPage() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel signUpPage = new JPanel();
        signUpPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "User sign-up", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));
        
        signUpPage.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Enter user account name: ");
        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(200, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpPage.add(label, gbc);
        gbc.gridx = 1;
        signUpPage.add(userNameField, gbc);

        label = new JLabel("Enter user first name: ");
        userFirstNameField = new JTextField();
        userFirstNameField.setPreferredSize(new Dimension(200, 20));

        gbc.gridx = 0;
        gbc.gridy = 1;
        signUpPage.add(label, gbc);
        gbc.gridx = 1;
        signUpPage.add(userFirstNameField, gbc);

        label = new JLabel("Enter user last name: ");
        userLastNameField = new JTextField();
        userLastNameField.setPreferredSize(new Dimension(200, 20));

        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpPage.add(label, gbc);
        gbc.gridx = 1;
        signUpPage.add(userLastNameField, gbc);

        label = new JLabel("Enter user address: ");
        userAddressField = new JTextField();
        userAddressField.setPreferredSize(new Dimension(200, 20));

        gbc.gridx = 0;
        gbc.gridy = 3;
        signUpPage.add(label, gbc);
        gbc.gridx = 1;
        signUpPage.add(userAddressField, gbc);

        label = new JLabel("Enter phone number:  ");
        userPhoneField = new JTextField();
        userPhoneField.setPreferredSize(new Dimension(200, 20));

        gbc.gridx = 0;
        gbc.gridy = 4;
        signUpPage.add(label, gbc);
        gbc.gridx = 1;
        signUpPage.add(userPhoneField, gbc);

        submitSignUpBtn = new JButton("Submit");
        submitSignUpBtn.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        signUpPage.add(submitSignUpBtn, gbc);

        userBackBtn = new JButton("Back");
        userBackBtn.setFocusable(false);
        gbc.gridx = 1;
        signUpPage.add(userBackBtn, gbc);

        return signUpPage;
    }

    @Override
    public JPanel getMainPage() {
        mainCardLayout = new CardLayout();

        JPanel panel = new JPanel();
        panel.setBackground(Colors.WHITE);

        topPanel = new JPanel();
        topPanel.setBackground(Colors.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Colors.BLACK));

        shopBtn = new JButton("Shop");
        Colors.setButtonUI(shopBtn);
        shopBtn.setFocusable(false);

        cartBtn = new JButton("Cart");
        Colors.setButtonUI(cartBtn);
        cartBtn.setFocusable(false);

        ordersBtn = new JButton("Orders");
        Colors.setButtonUI(ordersBtn);
        ordersBtn.setFocusable(false);

        profileBtn = new JButton("Profile");
        Colors.setButtonUI(profileBtn);
        profileBtn.setFocusable(false);

        logOutBtn = new JButton("Log out");
        Colors.setButtonUI(logOutBtn);
        logOutBtn.setFocusable(false);

        topPanel.add(shopBtn);
        topPanel.add(cartBtn);
        topPanel.add(ordersBtn);
        topPanel.add(profileBtn);
        topPanel.add(logOutBtn);

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BLACK, 2),
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel panel = new JPanel();
        panel.setBackground(Colors.WHITE);
        panel.setLayout(new GridBagLayout());
        
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setSize(new Dimension(400, 300));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Product information", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        productInfoLabel = new JLabel();
        productInfoLabel.setSize(new Dimension(450, 250));
        infoPanel.add(productInfoLabel);

        JLabel label = new JLabel("Browse by: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        String[] browseOptions = { "By shop", "By product type" };
        browseByBox = new JComboBox<>(browseOptions);
        gbc.gridx = 1;
        panel.add(browseByBox, gbc);

        browseByList = new JList<>(new DefaultListModel<>());
        browseByList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        browseByList.setFocusable(false);

        JScrollPane browseByListPane = new JScrollPane(browseByList);
        browseByListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        browseByListPane.setPreferredSize(new Dimension(300, 200));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(browseByListPane, gbc);

        productList = new JList<>(new DefaultListModel<>());

        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setFocusable(false);

        this.productListPane = new JScrollPane(productList);
        productListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productListPane.setPreferredSize(new Dimension(300, 200));

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(productListPane, gbc);

        gbc.gridx = 1;
        panel.add(infoPanel, gbc);
        
        addToCartBtn = new JButton("Add to Cart");
        Colors.setButtonUI(addToCartBtn);
        addToCartBtn.setFocusable(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(addToCartBtn, gbc);

        return panel;
    }

    public JPanel getCartPage() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Colors.WHITE);

        OrderTableModel mdl = new OrderTableModel(new HashSet<>());

        cartTable = new JTable(mdl);
        cartTable.setDefaultEditor(Object.class, null);

        cartTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(mdl, -1));
        cartTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(mdl, 1));

        cartTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer("-"));
        cartTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("+"));

        JScrollPane cartTablePane = new JScrollPane(cartTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        cartTablePane.setPreferredSize(new Dimension(550, 400));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(cartTablePane, gbc);

        checkOutBtn = new JButton("Check out");
        Colors.setButtonUI(checkOutBtn);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        checkOutBtn.setFocusable(false);
        panel.add(checkOutBtn, gbc);

        removeBtn = new JButton("Remove");
        Colors.setButtonUI(removeBtn);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        removeBtn.setFocusable(false);
        panel.add(removeBtn, gbc);

        JLabel lbl = new JLabel("Total: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lbl, gbc);

        totalLbl = new JLabel();
        gbc.gridx = 1;
        panel.add(totalLbl, gbc);
        
        return panel;
    }

    public JPanel getOrdersPage() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Colors.WHITE);

        ordersList = new JList<>(new DefaultListModel<>());

        ordersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersList.setFocusable(false);

        JScrollPane ordersListPane = new JScrollPane(ordersList);
        ordersListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ordersListPane.setPreferredSize(new Dimension(400, 300));

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(ordersListPane, gbc);

        orderInfoLbl = new JLabel();

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
        "Details", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        infoPanel.add(orderInfoLbl);       // USE THIS FOR SETTING THE ORDER INFO PACK IT INTO ONE STRING
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(infoPanel, gbc);

        String[] orderTypes = { "Orders", "Returns" };
        ordersBox = new JComboBox<>(orderTypes);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(ordersBox, gbc);

        returnBtn = new JButton("Request Return");
        Colors.setButtonUI(returnBtn);
        returnBtn.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(returnBtn, gbc);

        rateBtn = new JButton("Rate a Product");
        Colors.setButtonUI(rateBtn);
        rateBtn.setFocusable(false);
        gbc.gridx = 1;
        panel.add(rateBtn, gbc);

        receiveBtn = new JButton("Receive Order");
        Colors.setButtonUI(receiveBtn);
        receiveBtn.setFocusable(false);
        gbc.gridx = 2;
        panel.add(receiveBtn, gbc);

        return panel;
    }

    public JPanel getProfilePage() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Colors.WHITE);

        JLabel lbl = new JLabel("[USER DETAILS]");
        gbc.gridx =  0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lbl, gbc);

        lbl = new JLabel("Account name: ");
        profileNameField = new JTextField();
        profileNameField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileNameField, gbc);

        lbl = new JLabel("Phone number: ");
        profilePhoneField = new JTextField();
        profilePhoneField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profilePhoneField, gbc);

        lbl = new JLabel("Address: ");
        profileAddressField = new JTextField();
        profileAddressField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileAddressField, gbc);

        lbl = new JLabel("First name: ");
        profileFirstNameField = new JTextField();
        profileFirstNameField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileFirstNameField, gbc);

        lbl = new JLabel("Last name: ");
        profileLastNameField = new JTextField();
        profileLastNameField.setPreferredSize(new Dimension(200, 20));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(profileLastNameField, gbc);

        saveChangesBtn = new JButton("Save Changes");
        Colors.setButtonUI(saveChangesBtn);
        saveChangesBtn.setFocusable(false);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(saveChangesBtn, gbc);

        return panel;
    }

    public void updateProfilePage(User user) {
        profileNameField.setText(user.getUsername());
        profilePhoneField.setText(user.getPhoneNumber());
        profileAddressField.setText(user.getAddress());
        profileFirstNameField.setText(user.getFirstName());
        profileLastNameField.setText(user.getLastName());
    }
    
    public void refreshUserPage() {
        Component comps[] = this.getComponents();
        for (Component comp : comps) {
            if (comp.getName() != null && (comp.getName().equals("signup") || comp.getName().equals("main"))) {
                this.remove(comp);
            }
        }

        this.add(getSignUpPage(), "signup");
        this.add(getMainPage(), "main");
        this.revalidate();
        this.repaint();
    }

    public void updateBrowseList(Map<String, String> options) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.options = options;

        for (String option : options.keySet()) {
            mdl.addElement(option);
        }
        browseByList.setModel(mdl);
    }

    public void updateProductsList(ArrayList<Product> products) {
        DefaultListModel<Product> mdl = new DefaultListModel<>();
        for (Product product : products) {
            mdl.addElement(product);
        }
        this.productList.setModel(mdl);
        this.productListPane.setViewportView(productList);
    }

    public void updateCartTable(Set<OrderContent> shoppingCart) {
        OrderTableModel mdl = new OrderTableModel(shoppingCart);
        cartTable.setModel(mdl);
    }

    public int getQuantity() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);

        JFormattedTextField field = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        field.setEditable(false);
        field.setFocusable(false);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Quantity:"));
        panel.add(spinner);

        int option = JOptionPane.showConfirmDialog(null, panel,
    "Enter Quantity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            return (int) spinner.getValue();
        } else {
            return 0;
        }
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        userBackBtn.addActionListener(backLtr);

    }

    public void initMainListeners(ActionListener shopLtr, ActionListener cartLtr, ActionListener ordersLtr, ActionListener ordersBoxLtr, ActionListener profLtr, ActionListener logOutLtr,
                                  ActionListener addLtr, ItemListener browseByBoxLtr, ActionListener checkOutLtr, ActionListener removeLtr, ActionListener returnLtr, ActionListener rateLtr,
                                  ActionListener receiveLtr, ActionListener saveChangesLtr, ListSelectionListener browseByListLtr, ListSelectionListener productSelectLtr, ListSelectionListener orderSelectLtr) {
        shopBtn.addActionListener(shopLtr);
        cartBtn.addActionListener(cartLtr);
        ordersBtn.addActionListener(ordersLtr);
        profileBtn.addActionListener(profLtr);
        logOutBtn.addActionListener(logOutLtr);

        addToCartBtn.addActionListener(addLtr);
        browseByBox.addItemListener(browseByBoxLtr);
        
        ordersBox.addActionListener(ordersBoxLtr);


        checkOutBtn.addActionListener(checkOutLtr);
        removeBtn.addActionListener(removeLtr);

        returnBtn.addActionListener(returnLtr);
        rateBtn.addActionListener(rateLtr);
        receiveBtn.addActionListener(receiveLtr);

        saveChangesBtn.addActionListener(saveChangesLtr);

        browseByList.getSelectionModel().addListSelectionListener(browseByListLtr);
        productList.getSelectionModel().addListSelectionListener(productSelectLtr);
        ordersList.getSelectionModel().addListSelectionListener(orderSelectLtr);
    }
    
    public String getOrdersViewOption() { return (String) ordersBox.getSelectedItem(); }
    
    public void updateOrdersList(ArrayList<Order> orders) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.options = new LinkedHashMap<>();
        
        for (Order order : orders) {
            String display = "Order No. " + Integer.toString(order.getOrderID());
            this.options.put(display, Integer.toString(order.getOrderID()));
            mdl.addElement(display);
        }
        
        ordersList.setModel(mdl);
    }
    
    public void updateReturnsList(ArrayList<Return> returns) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.options = new LinkedHashMap<>();
        
        for (Return r : returns) {
            String display = "Order No. " + Integer.toString(r.getOrderID()) + ",  Product No. " + Integer.toString(r.getProductID());
            String value = Integer.toString(r.getOrderID()) + " " + Integer.toString(r.getProductID()); // value.split(" ") to get order id and product ig
            this.options.put(display, value);
            mdl.addElement(display);
        }
        
        ordersList.setModel(mdl);
    }
    
    public String getSelectedOrder() {
        return ordersList.getSelectedValue();
    }

    public String getMappedValue(String val) {
        return this.options.get(val);
    }
    
    public void setOrderInfoLbl(String text) {
        orderInfoLbl.setText(text);
    }
    
    public void ordersListToProducts(ArrayList<OrderContent> items) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
//        this.options = new LinkedHashMap<>();
        
        for (OrderContent item : items) {
            String display = "Order ID: " + Integer.toString(item.getOrderID()) + ", Product ID: " + Integer.toString(item.getProductID()) + ", Product Name: " + item.getProductName();
//            String value = Integer.toString(item.getOrderID()) + " " + Integer.toString(item.getProductID());
//            this.options.put(display, value);
            mdl.addElement(display);
        }
        
        ordersList.setModel(mdl);
    }
    
    public JPanel getRequestReturnPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel reqRet = new JPanel();
        reqRet.setLayout(new GridBagLayout());
        
        JLabel orderLbl = new JLabel("Order ID: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        reqRet.add(orderLbl, gbc);
        
        JLabel prodLbl = new JLabel("Product ID: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        reqRet.add(prodLbl, gbc);
        
        JLabel rsnLbl = new JLabel("Reason: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        reqRet.add(rsnLbl, gbc);
        
        JLabel descLbl = new JLabel("Description: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        reqRet.add(descLbl, gbc);
        
        orderInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        reqRet.add(orderInp, gbc);
        
        prodInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        reqRet.add(prodInp, gbc);
        
        String reasons[] = {"Damaged Item", "Wrong Item",  "Change of Mind", "Counterfeit Item"};
        comboBox = new JComboBox<>(reasons);
        gbc.gridx = 1;
        gbc.gridy = 2;
        reqRet.add(comboBox, gbc);
        
        descInp = new JTextArea(5, 5);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        reqRet.add(descInp, gbc);
        
        return reqRet;
    }
    
    public String getComboBoxVal() {
        return (String) this.comboBox.getSelectedItem();
    }
    
    public String getReturnDesc() {
        return this.descInp.getText();
    }
    
    public String getOrderInp() {
        return this.orderInp.getText();
    }
    
    public String getProdInp() {
        return this.prodInp.getText();
    }
    
    public JPanel getRatingPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel rate = new JPanel();
        rate.setLayout(new GridBagLayout());
        
        JLabel orderLbl = new JLabel("Order ID: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        rate.add(orderLbl, gbc);
        
        JLabel prodLbl = new JLabel("Product ID: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        rate.add(prodLbl, gbc);
        
        JLabel rateLbl = new JLabel("Rating: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        rate.add(rateLbl, gbc);
        
        orderInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        rate.add(orderInp, gbc);
        
        prodInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        rate.add(prodInp, gbc);
        
        SpinnerModel sm = new SpinnerNumberModel(0, 0, 5, 1);
        spinner = new JSpinner(sm);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rate.add(spinner, gbc);
        
        return rate;
    }
    
    public JPanel getReceiveOrderPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel receiveOrd = new JPanel();
        receiveOrd.setLayout(new GridBagLayout());
        
        JLabel orderLbl = new JLabel("Order ID: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        receiveOrd.add(orderLbl, gbc);
        
//        JLabel lbl = new JLabel("ENTER DATE");
//        lbl.setFont(new Font("Verdana", Font.BOLD, 12));
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.gridwidth = 2;
//        receiveOrd.add(lbl, gbc);
//        
//        JLabel monthLbl = new JLabel("Month: ");
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        receiveOrd.add(monthLbl, gbc);
//        
//        JLabel dayLbl = new JLabel("Day: ");
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 1;
//        receiveOrd.add(dayLbl, gbc);
//        
//        JLabel yearLbl = new JLabel("Year: ");
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.gridwidth = 1;
//        receiveOrd.add(yearLbl, gbc);
//        
        orderInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        receiveOrd.add(orderInp, gbc);
//        
//        String months[] = {"January", "February", "March", "April", "May", "June", "July",
//                            "August", "September", "October", "November", "December"};
//        comboBox = new JComboBox<>(months);
//        gbc.gridx = 1;
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        receiveOrd.add(comboBox, gbc);
//
//        intComboBox = new JComboBox<>();
//        for (int i = 1; i <= 31; i++) {
//            intComboBox.addItem(i);
//        }
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        gbc.gridwidth = 1;
//        receiveOrd.add(intComboBox, gbc);
//        
//        SpinnerModel sm = new SpinnerNumberModel(2024, 2020, 2050, 1);
//        spinner = new JSpinner(sm);
//        spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
//        gbc.gridx = 1;
//        gbc.gridy = 4;
//        gbc.gridwidth = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        receiveOrd.add(spinner, gbc);
        
        return receiveOrd;
    }
    
    public int getSpinnerVal() {
        return (int) this.spinner.getValue();
    }
    
    public int getIntComboBoxVal() {
        return (int) this.intComboBox.getSelectedItem();
    }

    @Override
    public void nextPageName(String name) {
        this.userCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

    public void setProductInfo(String text) { this.productInfoLabel.setText(text); }
    public void setOrderInfo(String text) { this.orderInfoLbl.setText(text); }

    public ArrayList<OrderContent> getSelectedRecords() {
        OrderTableModel mdl = (OrderTableModel) cartTable.getModel();
        ArrayList<OrderContent> selectedRecords = new ArrayList<>();

        for (int i = 0; i < mdl.getRowCount(); i++) {
            if ((Boolean) mdl.getValueAt(i, 0)) {
                selectedRecords.add(mdl.getOrderContent(i));
            }
        }

        return selectedRecords;
    }

    public String getUserName() { return userNameField.getText().trim(); }
    public String getUserFirstName() { return userFirstNameField.getText().trim(); }
    public String getUserLastName() { return userLastNameField.getText().trim(); }
    public String getUserAddress() { return userAddressField.getText().trim(); }
    public String getUserPhone() { return userPhoneField.getText().trim(); }
    public String getEditedName() { return profileNameField.getText().trim(); }
    public String getEditedPhone() { return profilePhoneField.getText().trim(); }
    public String getEditedAddress() { return profileAddressField.getText().trim(); }
    public String getEditedFirstName() { return profileFirstNameField.getText().trim(); }
    public String getEditedLastName() { return profileLastNameField.getText().trim(); }
    public String getBrowseByOption() { return (String) browseByBox.getSelectedItem(); }
    public Product getSelectedProduct() { return productList.getSelectedValue(); }
    public String getSelectedOption() { return options.get(browseByList.getSelectedValue()); }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private final OrderTableModel tableModel;
        private final int direction;
        private int currentRow;

        public ButtonEditor(OrderTableModel tableModel, int direction) {
            this.tableModel = tableModel;
            this.direction = direction;

            button = new JButton(direction == -1 ? "-" : "+");
            button.addActionListener(this::updateQuantity);
        }

        private void updateQuantity(ActionEvent e) {
            tableModel.updateQuantity(currentRow, direction);
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
