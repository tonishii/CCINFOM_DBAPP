package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout centerCardLayout;
    private final JPanel centerPanel;

    public MainFrame() {
        this.setTitle("E-Commerce Database Application");
        this.setSize(800,700);
        this.setLayout(new BorderLayout(10,10));
        this.setResizable(false);

        JPanel topBanner = new JPanel();
        topBanner.setBackground(Color.PINK);
        topBanner.setPreferredSize(new Dimension(800, 60));
        this.add(topBanner, BorderLayout.NORTH);

        centerCardLayout = new CardLayout(50, 40);
        centerPanel = new JPanel();
        centerPanel.setLayout(centerCardLayout);
        centerPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        this.add(centerPanel);

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
