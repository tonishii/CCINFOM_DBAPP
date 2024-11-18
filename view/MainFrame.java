package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout centerCardLayout;
    private final JPanel centerPanel;

    public MainFrame() {
        this.setTitle("E-Commerce Database Application");
        this.setSize(800,700);

        centerCardLayout = new CardLayout(50, 40);
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
}
