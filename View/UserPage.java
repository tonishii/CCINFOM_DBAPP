package View;

import Model.Order;
import Model.OrderContent;
import Model.Product;
import Model.User;
import Model.Return;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.*;

public class UserPage extends JPanel implements AccountPage {
    private final CardLayout userCardLayout;

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

    private JTextArea           productInfoArea;
    private JComboBox<String>   browseByBox;
    private JList<String>       browseByList;
    private Map<String, String> options;
    private JList<Product>      productList;

    // Cart option

    private JTable            cartTable;
    private JButton           checkOutBtn,
                              removeBtn;

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

        JLabel label = new JLabel("<html><b>Browse by: </b></html>");
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

        productInfoArea = new JTextArea();
        productInfoArea.setEditable(false);
        productInfoArea.setFocusable(false);

        JScrollPane pane = new JScrollPane(productInfoArea);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(400, 200));
        pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                "Product details", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        gbc.gridx = 1;
        panel.add(pane, gbc);

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

        final String[] columnNames = {"Select", "Product Name", "-", "Qty", "+", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        cartTable = new JTable(model);
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

        JLabel lbl = new JLabel("<html><b><u>USER DETAILS</u></b></html>");
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

    // Removes and reinitializes the signup and main panels in the main panel
    public void refreshUserPage() {
        Component[] comps = this.getComponents();
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

    // Updates the browse JList
    public void updateBrowseList(Map<String, String> options) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.options = options;

        for (String option : options.keySet()) {
            mdl.addElement(option);
        }
        browseByList.setModel(mdl);
    }

    // Updates the products JList
    public void updateProductsList(ArrayList<Product> products) {
        DefaultListModel<Product> mdl = new DefaultListModel<>();
        for (Product product : products) {
            mdl.addElement(product);
        }
        this.productList.setModel(mdl);
        this.productListPane.setViewportView(productList);
    }

    // Updates shopping cart contents
    public void updateCartTable(Set<OrderContent> shoppingCart) {
        final String[] columnNames = {"Select", "Product Name", "-", "Qty", "+", "Price"};

        DefaultTableModel mdl = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only make checkbox and button columns editable
                return column == 0 || column == 2 || column == 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Set Boolean class for checkbox column
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        for (OrderContent order : shoppingCart) {
            mdl.addRow(new Object[]{false, order.getProductName(), "-", order.getQuantity(), "+", order.getPriceEach()});
        }

        cartTable.setModel(mdl);
        cartTable.getColumnModel().getColumn(0).setCellRenderer(cartTable.getDefaultRenderer(Boolean.class));
        cartTable.getColumnModel().getColumn(0).setCellEditor(cartTable.getDefaultEditor(Boolean.class));

        cartTable.getColumnModel().getColumn(2).setCellRenderer(new ActionButtonRenderer("-"));
        cartTable.getColumnModel().getColumn(2).setCellEditor(new ActionButtonEditor("-", cartTable, -1));

        cartTable.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer("+"));
        cartTable.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor("+", cartTable, 1));
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
            String display = "Order No. " + order.getOrderID();
            this.options.put(display, Integer.toString(order.getOrderID()));
            mdl.addElement(display);
        }
        
        ordersList.setModel(mdl);
    }
    
    public void updateReturnsList(ArrayList<Return> returns) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.options = new LinkedHashMap<>();
        
        for (Return r : returns) {
            String display = "Order No. " + r.getOrderID() + ",  Product No. " + r.getProductID();
            String value = r.getOrderID() + " " + r.getProductID(); // value.split(" ") to get order id and product ig
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
    
    // Converts the orders JList into an order content JList
    public void ordersListToProducts(ArrayList<OrderContent> items) {
        DefaultListModel<String> mdl = new DefaultListModel<>();
        
        for (OrderContent item : items) {
            String display = "Order ID: " + item.getOrderID() + ", Product ID: " + item.getProductID() + ", Product Name: " + item.getProductName();
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
        
        String[] reasons = {"Damaged Item", "Wrong Item",  "Change of Mind", "Counterfeit Item"};
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

        orderInp = new JTextField(5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        receiveOrd.add(orderInp, gbc);

        return receiveOrd;
    }

    public int getCheckoutConfirmPage(ArrayList<OrderContent> selectedItems) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        double totalOrderPrice = 0.0;

        for (OrderContent order : selectedItems) {
            double totalPrice = order.getQuantity() * order.getPriceEach();
            listModel.addElement(String.format("%s - Qty: %d, Price Each: ₱%.2f, Total: ₱%.2f",
            order.getProductName(), order.getQuantity(), order.getPriceEach(), totalPrice));
            totalOrderPrice += totalPrice;
        }

        JList<String> breakdownList = new JList<>(listModel);
        breakdownList.setVisibleRowCount(10);

        JScrollPane scrollPane = new JScrollPane(breakdownList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel lbl = new JLabel(String.format("Total Order Price: ₱%.2f", totalOrderPrice));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(lbl, BorderLayout.SOUTH);

        return JOptionPane.showConfirmDialog(
            null,
            panel,
            "Order Summary",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void nextPageName(String name) {
        this.userCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

    public void setProductInfo(String text) { this.productInfoArea.setText(text); }

    public ArrayList<OrderContent> getSelectedRecords(ArrayList<OrderContent> shoppingCart) {
        ArrayList<OrderContent> selectedRecords = new ArrayList<>();

        for (int i = 0; i < cartTable.getRowCount(); i++) {
            if ((Boolean) cartTable.getValueAt(i, 0) && ((Integer) cartTable.getValueAt(i, 3)) > 0) {
                selectedRecords.add(shoppingCart.get(i).setQuantity((Integer) cartTable.getValueAt(i, 3)));
            }
        }

        return selectedRecords;
    }

    public int getSpinnerVal() { return (int) this.spinner.getValue(); }
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

    static class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer(String label) {
            setText(label);
            setContentAreaFilled(false);
            setFocusable(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground((isSelected || hasFocus) ? table.getSelectionBackground() : UIManager.getColor("Button.background"));
            return this;
        }
    }
}
