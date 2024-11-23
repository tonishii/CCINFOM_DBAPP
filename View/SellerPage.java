package View;

import Model.Product;
import Model.Seller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SellerPage extends JPanel implements AccountPage {
    private CardLayout sellerCardLayout;
    private GridBagLayout newWindowLayout;

    public static final String SIGNUP = "signup";
    public static final String PRODUCTLIST = "plist";
    public static final String PRODUCTPOPLIST = "productpop";

    private JDialog    newWindow;
    private JTextField sellerNameField,
                       sellerPhoneField,
                       sellerAddressField,
                       productName,
                       productPrice,
                       productType,
                       productQuantity,
                       productDesc,
                       dateYear,
                       dateMonth;

    private JTable     ProductPopTable;

    private JButton    submitSignUpBtn,
                       sellerBackBtn;

    private Map<String, String> lists;

    private JTextArea  productRefundInfo;

    // Main page
    private JPanel     topPanel,
                       bottomPanel;

    private CardLayout mainCardLayout;

    // Top Panel Buttons
    private JButton    genBtn,
                       editAccBtn,
                       logoutBtn;

    //productListPage Buttons
    private JButton    addBtn,
                       removeBtn,
                       editBtn,
                       approveBtn,
                       rejectBtn,
                       cancelBtn,
                       saveProfileBtn,
                       addProductBtn,
                       saveProductBtn,
                       generateReportBtn,
                       backBtn;

    private  JComboBox<String>   sellerCRBox;
    private  JComboBox<String>   sellerReportBox;
    private  JList<String>       sellerCRList;

    public SellerPage() {
        this.sellerCardLayout = new CardLayout();
        this.setLayout(sellerCardLayout);

        this.add(getSignUpPage(), "signup");
        this.add(getMainPage(), "main");
    }

    @Override
    public JPanel getSignUpPage() {

        JPanel signUpPage = new JPanel(new GridBagLayout());
        signUpPage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Seller sign-up", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel("Enter seller name: ");
        sellerNameField = new JTextField();
        sellerNameField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 0;
        signUpPage.add(label, gbc);
        signUpPage.add(sellerNameField,gbc);

        label = new JLabel("Enter seller address: ");
        sellerAddressField = new JTextField();
        sellerAddressField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 2;
        signUpPage.add(label, gbc);
        signUpPage.add(sellerAddressField,gbc);

        label = new JLabel("Enter phone number: ");
        sellerPhoneField = new JTextField();
        sellerPhoneField.setPreferredSize(new Dimension(200, 20));

        gbc.gridy = 3;
        signUpPage.add(label, gbc);
        signUpPage.add(sellerPhoneField,gbc);

        gbc.gridy = 4;
        submitSignUpBtn = new JButton("Submit");
        submitSignUpBtn.setFocusable(false);
        signUpPage.add(submitSignUpBtn,gbc);

        sellerBackBtn = new JButton("Back");
        sellerBackBtn.setFocusable(false);
        signUpPage.add(sellerBackBtn, gbc);

        return signUpPage;
    }

    @Override
    public JPanel getMainPage() {
        mainCardLayout = new CardLayout();

        JPanel panel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(Colors.WHITE);
        topPanel = new JPanel();
        topPanel.setBackground(Colors.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        genBtn = new JButton("Generate");
        Colors.setButtonUI(genBtn);
        genBtn.setFocusable(false);

        editAccBtn = new JButton("Edit Account");
        Colors.setButtonUI(editAccBtn);
        editAccBtn.setFocusable(false);

        logoutBtn = new JButton("Log Out");
        Colors.setButtonUI(logoutBtn);
        logoutBtn.setFocusable(false);
        topPanel.add(genBtn);
        topPanel.add(editAccBtn);
        topPanel.add(logoutBtn);

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Seller", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        bottomPanel.add(getProductListPage(), PRODUCTLIST);
        bottomPanel.add(showProductPop(), PRODUCTPOPLIST);


        panel.add(topPanel);
        panel.add(bottomPanel);

        nextMainPageName(PRODUCTLIST);

        return panel;
    }

    public JPanel getProductListPage(){
        JPanel panel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFocusable(false);

        saveProfileBtn = new JButton("Save Profile");
        saveProfileBtn.setFocusable(false);

        addProductBtn = new JButton("Add Product");
        addProductBtn.setFocusable(false);

        saveProductBtn = new JButton("Save Product");
        saveProductBtn.setFocusable(false);

        generateReportBtn = new JButton("Generate Report");
        generateReportBtn.setFocusable(false);

        backBtn = new JButton("Back");
        backBtn.setFocusable(false);

        String[] reports = {"Sales Report", "Credibility Report", "Product Popularity Report"};
        sellerReportBox = new JComboBox<>(reports);
        sellerReportBox.setPreferredSize(new Dimension(300, 50));
        sellerReportBox.setFocusable(false);

        // top panel
        JPanel panelCenter = new JPanel(new GridBagLayout());
        panelCenter.setBackground(Colors.WHITE);
        gbc.insets = new Insets(0, 4 ,5 ,4);
        String[] stuff = {"Products", "Refunds"};
        sellerCRBox = new JComboBox<>(stuff);
        sellerCRBox.setPreferredSize(new Dimension(300, 50));
        sellerCRBox.setFocusable(false);
        sellerCRList = new JList<>(new DefaultListModel<>());
        sellerCRList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sellerCRList.setFocusable(false);

        JScrollPane sellerListPane = new JScrollPane(sellerCRList);
        sellerListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sellerListPane.setPreferredSize(new Dimension(300, 350));

        productRefundInfo = new JTextArea();
        productRefundInfo.setEditable(false);
        productRefundInfo.setBorder(new LineBorder(Color.BLACK, 6));
        productRefundInfo.setPreferredSize(new Dimension(390, 250));
        productRefundInfo.setFocusable(false);

        addBtn = new JButton("Add");
        Colors.setButtonUI(addBtn);
        addBtn.setFocusable(false);

        removeBtn = new JButton("Remove");
        Colors.setButtonUI(removeBtn);
        removeBtn.setFocusable(false);

        editBtn = new JButton("Edit");
        Colors.setButtonUI(editBtn);
        editBtn.setFocusable(false);

        approveBtn = new JButton("Approve");
        Colors.setButtonUI(approveBtn);
        approveBtn.setFocusable(false);
        approveBtn.setVisible(false);

        rejectBtn = new JButton("Reject");
        Colors.setButtonUI(rejectBtn);
        rejectBtn.setFocusable(false);
        rejectBtn.setVisible(false);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelCenter.add(sellerCRBox, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCenter.add(sellerListPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCenter.add(productRefundInfo, gbc);

        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelCenter.add(addBtn, gbc);
        panelCenter.add(approveBtn,gbc);

        gbc.gridy = 3;
        panelCenter.add(editBtn, gbc);
        panelCenter.add(rejectBtn,gbc);

        gbc.gridy = 4;
        panelCenter.add(removeBtn, gbc);

        gbc.weighty = 1.0;

        panel.add(panelCenter);

        return panel;
    }

    public void showEditAccount(){
        newWindow = new JDialog();
        newWindow.setTitle("Edit Account");
        newWindow.setSize(400, 200);
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Dispose of the dialog and enable buttons
                disposeNewWindow();
            }
        });

        JPanel panel = new JPanel( new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4 ,5 ,4);
        sellerNameField = new JTextField();
        sellerNameField.setPreferredSize(new Dimension(200, 20));
        sellerAddressField = new JTextField();
        sellerAddressField.setPreferredSize(new Dimension(200, 20));
        sellerPhoneField = new JTextField();
        sellerPhoneField.setPreferredSize(new Dimension(200, 20));
        gbc.gridy=0;
        panel.add(new JLabel("Name: "),gbc);
        panel.add(sellerNameField, gbc);
        gbc.gridy=1;
        panel.add(new JLabel("Address: "),gbc);
        panel.add(sellerAddressField, gbc);
        gbc.gridy=2;
        panel.add(new JLabel("Phone: "),gbc);
        panel.add(sellerPhoneField, gbc);
        gbc.gridy=3;
        panel.add(saveProfileBtn, gbc);
        panel.add(cancelBtn, gbc);

        newWindow.add(panel);
        newWindow.setResizable(false);
        newWindow.setLocationRelativeTo(null);
        newWindow.setVisible(true);

        //return JOptionPane.showConfirmDialog(null, panel, "Edit Account", JOptionPane.OK_CANCEL_OPTION);
    }

    public void showAddProduct(){
        newWindow = new JDialog();
        newWindow.setTitle("Add Product");
        newWindow.setSize(400, 200);
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Dispose of the dialog and enable buttons
                disposeNewWindow();
            }
        });

        JPanel panel = new JPanel( new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4 ,5 ,4);
        productName = new JTextField();
        productName.setPreferredSize(new Dimension(200, 20));
        productPrice = new JTextField();
        productPrice.setPreferredSize(new Dimension(200, 20));
        productType = new JTextField();
        productType.setPreferredSize(new Dimension(200, 20));
        productQuantity = new JTextField();
        productQuantity.setPreferredSize(new Dimension(200, 20));
        productDesc = new JTextField();
        productDesc.setPreferredSize(new Dimension(200, 20));
        gbc.gridy=0;
        panel.add(new JLabel("Product Name: "),gbc);
        panel.add(productName, gbc);
        gbc.gridy=1;
        panel.add(new JLabel("Product Price: "),gbc);
        panel.add(productPrice, gbc);
        gbc.gridy=2;
        panel.add(new JLabel("Product Type: "),gbc);
        panel.add(productType, gbc);
        gbc.gridy=3;
        panel.add(new JLabel("Product Quantity: "),gbc);
        panel.add(productQuantity, gbc);
        gbc.gridy=4;
        panel.add(new JLabel("Product Description: "),gbc);
        panel.add(productDesc, gbc);
        gbc.gridy=5;
        panel.add(addProductBtn, gbc);
        panel.add(cancelBtn, gbc);

        newWindow.add(panel);
        newWindow.setResizable(false);
        newWindow.setLocationRelativeTo(null);
        newWindow.setVisible(true);

        //return JOptionPane.showConfirmDialog(null, panel, "Add Product", JOptionPane.OK_CANCEL_OPTION);
    }

    public void showEditProduct(){
        newWindow = new JDialog();
        newWindow.setTitle("Edit Product");
        newWindow.setSize(400, 200);
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Dispose of the dialog and enable buttons
                disposeNewWindow();
            }
        });

        JPanel panel = new JPanel( new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4 ,5 ,4);
        productName = new JTextField();
        productName.setPreferredSize(new Dimension(200, 20));
        productPrice = new JTextField();
        productPrice.setPreferredSize(new Dimension(200, 20));
        productType = new JTextField();
        productType.setPreferredSize(new Dimension(200, 20));
        productQuantity = new JTextField();
        productQuantity.setPreferredSize(new Dimension(200, 20));
        productDesc = new JTextField();
        productDesc.setPreferredSize(new Dimension(200, 20));
        gbc.gridy=0;
        panel.add(new JLabel("Product Name: "),gbc);
        panel.add(productName, gbc);
        gbc.gridy=1;
        panel.add(new JLabel("Product Price: "),gbc);
        panel.add(productPrice, gbc);
        gbc.gridy=2;
        panel.add(new JLabel("Product Type: "),gbc);
        panel.add(productType, gbc);
        gbc.gridy=3;
        panel.add(new JLabel("Product Quantity: "),gbc);
        panel.add(productQuantity, gbc);
        gbc.gridy=4;
        panel.add(new JLabel("Product Description: "),gbc);
        panel.add(productDesc, gbc);
        gbc.gridy=5;
        panel.add(saveProductBtn, gbc);
        panel.add(cancelBtn, gbc);

        newWindow.add(panel);
        newWindow.setResizable(false);
        newWindow.setLocationRelativeTo(null);
        newWindow.setVisible(true);
        //return JOptionPane.showConfirmDialog(null, panel, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
    }

    public void showGenerate(){
        newWindow = new JDialog();
        newWindow.setTitle("Report Generation");
        newWindow.setSize(400, 250);
        newWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Dispose of the dialog and enable buttons
                disposeNewWindow();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 4 ,5 ,4);
        dateYear = new JTextField();
        dateYear.setPreferredSize(new Dimension(100, 20));
        dateMonth = new JTextField();
        dateMonth.setPreferredSize(new Dimension(100, 20));
        sellerReportBox.setPreferredSize(new Dimension(200, 30));
        sellerReportBox.setSelectedIndex(0);

        gbc.gridy = 0;
        gbc.weighty = 0.0;
        panel.add(new JLabel("Reports: "),gbc);

        gbc.gridy = 1;
        panel.add(sellerReportBox, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Month (Number): "), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(dateMonth, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Year (Number): "), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(dateYear, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(generateReportBtn, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(cancelBtn, gbc);

        newWindow.add(panel);
        newWindow.setResizable(false);
        newWindow.setLocationRelativeTo(null);
        newWindow.setVisible(true);
    }

    public JPanel showProductPop(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Colors.WHITE);

        final String[] columnNames = {"Rank", "Product Name", "Units Sold", "Average Rating"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        ProductPopTable = new JTable(model);

        JScrollPane ProductPopPane = new JScrollPane(ProductPopTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ProductPopPane.setPreferredSize(new Dimension(550, 400));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Product Popularity Report"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(ProductPopPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        return panel;
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        sellerBackBtn.addActionListener(backLtr);
    }

    public void initMainListeners(ActionListener genLtr, ActionListener editAccLtr, ActionListener logoutLtr,
                                  ActionListener selCRLtr, ListSelectionListener textLtr, ActionListener addLtr,
                                  ActionListener editProdLtr, ActionListener cancelLtr, ActionListener saveProfileLtr,
                                  ActionListener addProductLtr, ActionListener saveProductLtr, ActionListener removeProductLtr,
                                  ActionListener approveLtr, ActionListener rejectLtr, ActionListener generateReportLtr,
                                  ActionListener listReportLtr, ActionListener backLtr){
        genBtn.addActionListener(genLtr);
        editAccBtn.addActionListener(editAccLtr);
        logoutBtn.addActionListener(logoutLtr);
        sellerCRBox.addActionListener(selCRLtr);
        sellerCRList.addListSelectionListener(textLtr);
        addBtn.addActionListener(addLtr);
        editBtn.addActionListener(editProdLtr);
        cancelBtn.addActionListener(cancelLtr);
        saveProfileBtn.addActionListener(saveProfileLtr);
        addProductBtn.addActionListener(addProductLtr);
        saveProductBtn.addActionListener(saveProductLtr);
        removeBtn.addActionListener(removeProductLtr);
        approveBtn.addActionListener(approveLtr);
        rejectBtn.addActionListener(rejectLtr);
        generateReportBtn.addActionListener(generateReportLtr);
        sellerReportBox.addActionListener(listReportLtr);
        backBtn.addActionListener(backLtr);
    }

    @Override
    public void nextPageName(String name) {
        this.sellerCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

    public void updateSellerProductList(ArrayList<Product> lists){
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.lists = new LinkedHashMap<>();

        for (Product option : lists)
        {
            String display = "ID: " + option.getProductID() + " Name: " + option.getName();
            mdl.addElement(display);
            this.lists.put(display, option.getProductID() + " " + option.getSellerID());
        }

        sellerCRList.setModel(mdl);
    }

    public void updateSellerRefundList(Map<String,String> lists){
        DefaultListModel<String> mdl = new DefaultListModel<>();
        this.lists = lists;

        for (String option : lists.keySet()) {
            mdl.addElement(option);
        }

        sellerCRList.setModel(mdl);
    }

    public void setProductRefundInfo(String text){
        productRefundInfo.setText(text);
    }

    public void updateEditAccount(Seller seller) {
        sellerNameField.setText(seller.getName());
        sellerPhoneField.setText(seller.getPhoneNumber());
        sellerAddressField.setText(seller.getAddress());
    }

    public void updateEditProduct(Product product){
        productName.setText(product.getName());
        productPrice.setText(Float.toString(product.getPrice()));
        productType.setText(product.getType());
        productQuantity.setText(Integer.toString(product.getQuantity()));
        productDesc.setText(product.getDescription());
    }

    public void setDisableButtons(){
        genBtn.setEnabled(false);
        editAccBtn.setEnabled(false);
        logoutBtn.setEnabled(false);
        approveBtn.setEnabled(false);
        rejectBtn.setEnabled(false);
        addBtn.setEnabled(false);
        removeBtn.setEnabled(false);
        editBtn.setEnabled(false);
    }

    public void setEnableButtons(){
        genBtn.setEnabled(true);
        editAccBtn.setEnabled(true);
        logoutBtn.setEnabled(true);
        approveBtn.setEnabled(true);
        rejectBtn.setEnabled(true);
        addBtn.setEnabled(true);
        removeBtn.setEnabled(true);
        editBtn.setEnabled(true);
    }

    public void setInvisibleBtns(String n){
        if (n.equals("Refunds")) {
            approveBtn.setVisible(true);
            rejectBtn.setVisible(true);
            addBtn.setVisible(false);
            removeBtn.setVisible(false);
            editBtn.setVisible(false);
        } else if (n.equals("Products")) {
            addBtn.setVisible(true);
            removeBtn.setVisible(true);
            editBtn.setVisible(true);
            approveBtn.setVisible(false);
            rejectBtn.setVisible(false);
        }
    }

    public String getSelectedOption(){
        if (!(sellerCRList.getSelectedValue() == null))
            return lists.get(sellerCRList.getSelectedValue());
        return null;
    }

    public JDialog getNewWindow() { return newWindow; }
    public void disposeNewWindow() {
        newWindow.dispose();
        setEnableButtons();
    }

    public void updateProductPopTable(ArrayList<Object[]> data){
        final String[] columnNames = {"Rank", "Product Name", "Units Sold", "Average Rating"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        for (Object[] record : data){
            model.addRow(new Object[]{record[0], record[1], record[2], record[3]});
        }

        ProductPopTable.setModel(model);
    }

    public void setEnableTopButtons(){
        genBtn.setEnabled(true);
        editAccBtn.setEnabled(true);
        logoutBtn.setEnabled(true);
    }

    public void setDisableTopButtons(){
        genBtn.setEnabled(false);
        editAccBtn.setEnabled(false);
        logoutBtn.setEnabled(false);
    }

    public void enableMonthTextField(){
        dateMonth.setEditable(true);
    }

    public void disableMonthTextField(){
        dateMonth.setEditable(false);
        dateMonth.setText("");
    }

    public String getDateMonth(){ return dateMonth.getText().trim(); }
    public String getDateYear(){ return dateYear.getText().trim(); }
    public String getSellerReportBox(){ return (String) this.sellerReportBox.getSelectedItem(); }
    public void setBackSellerBox(){ this.sellerCRBox.setSelectedIndex(0); }
    public String getSellerCRBox(){ return (String) this.sellerCRBox.getSelectedItem(); }
    public String getSellerName() { return sellerNameField.getText().trim(); }
    public String getSellerAddress() { return sellerAddressField.getText().trim(); }
    public String getSellerPhone() { return sellerPhoneField.getText().trim(); }
    public String getProductName() { return productName.getText().trim(); }
    public String getProductPrice() { return productPrice.getText().trim(); }
    public String getProductType() { return productType.getText().trim(); }
    public String getProductQuantity() { return productQuantity.getText().trim(); }
    public String getProductDesc() { return productDesc.getText().trim(); }
}
