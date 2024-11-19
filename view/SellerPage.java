package view;

import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SellerPage extends JPanel implements AccountPage {
    private CardLayout sellerCardLayout;

    public static final String SIGNUP = "signup";
    public static final String PRODUCTLIST = "plist";

    private JTextField sellerNameField,
                       sellerPhoneField,
                       sellerAddressField;
    private JButton    submitSignUpBtn,
                       sellerBackBtn;

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
                       rejectBtn;

    private  JComboBox<String>   sellerCRBox;
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
        topPanel = new JPanel();
        topPanel.setBackground(Color.PINK);
        topPanel.setPreferredSize(new Dimension(800, 60));

        genBtn = new JButton("Generate");
        genBtn.setFocusable(false);

        editAccBtn = new JButton("Edit Account");
        editAccBtn.setFocusable(false);

        logoutBtn = new JButton("Log Out");
        logoutBtn.setFocusable(false);
        topPanel.add(genBtn);
        topPanel.add(editAccBtn);
        topPanel.add(logoutBtn);

        bottomPanel = new JPanel(mainCardLayout);
        bottomPanel.setPreferredSize(new Dimension(800, 640));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
    "Seller", TitledBorder.LEFT, TitledBorder.TOP, new Font("Montserrat", Font.PLAIN, 12)));

        bottomPanel.add(getProductListPage(), PRODUCTLIST);

        panel.add(topPanel);
        panel.add(bottomPanel);

        nextMainPageName(PRODUCTLIST);


        return panel;
    }

    public JPanel getProductListPage(){
        JPanel panel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();

        // top panel
        JPanel panelCenter = new JPanel(new GridBagLayout());
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
        sellerListPane.setPreferredSize(new Dimension(300, 400));

        productRefundInfo = new JTextArea();
        productRefundInfo.setEditable(false);
        productRefundInfo.setBorder(new LineBorder(Color.BLACK, 6));
        productRefundInfo.setPreferredSize(new Dimension(390, 250));
        productRefundInfo.setFocusable(false);

        addBtn = new JButton("Add");
        addBtn.setFocusable(false);

        removeBtn = new JButton("Remove");
        removeBtn.setFocusable(false);

        editBtn = new JButton("Edit");
        editBtn.setFocusable(false);

        approveBtn = new JButton("Approve");
        approveBtn.setFocusable(false);
        approveBtn.setVisible(false);

        rejectBtn = new JButton("Reject");
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
        panelCenter.add(removeBtn, gbc);
        panelCenter.add(rejectBtn,gbc);

        gbc.gridy = 4;
        panelCenter.add(editBtn, gbc);

        gbc.weighty = 1.0;

        panel.add(panelCenter);

        return panel;
    }

    @Override
    public void initSignUpListeners(ActionListener signUpLtr, ActionListener backLtr) {
        submitSignUpBtn.addActionListener(signUpLtr);
        sellerBackBtn.addActionListener(backLtr);
    }

    public void initMainListeners(ActionListener genLtr, ActionListener editAccLtr, ActionListener logoutLtr){
        genBtn.addActionListener(genLtr);
        editAccBtn.addActionListener(editAccLtr);
        logoutBtn.addActionListener(logoutLtr);

    }

    @Override
    public void nextPageName(String name) {
        this.sellerCardLayout.show(this, name);
    }

    @Override
    public void nextMainPageName(String name) {
        this.mainCardLayout.show(bottomPanel, name);
    }

    public String getSellerName() { return sellerNameField.getText().trim(); }
    public String getSellerAddress() { return sellerAddressField.getText().trim(); }
    public String getSellerPhone() { return sellerPhoneField.getText().trim(); }
}
