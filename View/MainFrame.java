package View;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    private final CardLayout centerCardLayout;
    private final JPanel centerPanel;

    // Constants for page loading (card layout)
    public static final String CONNECTPAGE = "conn";
    public static final String SELECTACCPAGE = "acc";
    public static final String USERPAGE = "user";
    public static final String SELLERPAGE = "seller";
    public static final String COURIERPAGE = "courier";

    public MainFrame() {
        this.setTitle("E-Commerce Database Application");
        this.setSize(800,700);

        centerCardLayout = new CardLayout();
        centerPanel = new JPanel(centerCardLayout);
        this.add(centerPanel);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void nextPageName(String name) {
        this.centerCardLayout.show(this.centerPanel, name);
    }

    public void addToCenterPanel(JPanel panel, String name) {
        this.centerPanel.add(panel, name);
    }
    
    public static void clearInputs(Container cont) {
        for (Component comp : cont.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
            }
                
            else if (comp instanceof JPasswordField) {
                ((JTextField) comp).setText("");
            }

            else if (comp instanceof JLabel) {
                ((JLabel) comp).setText("");
            }
            
            else if (comp instanceof JTextArea) {
                ((JTextArea) comp).setText("");
            }
                
            else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setSelected(false);
            }
                
            else if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setSelectedIndex(0);
            }
                
            else if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                table.setModel(new DefaultTableModel());
            }
            else if (comp instanceof JList<?> list) {
                if (list.getModel() instanceof DefaultListModel<?> model) {
                    model.clear();
                }
            }
            else if (comp instanceof Container) {
                 clearInputs((Container) comp);
            }
        }
    }
}
