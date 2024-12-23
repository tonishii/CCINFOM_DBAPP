package View;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class Colors {
    public static final Color PINK = new Color(251, 175, 218);
    public static final Color BLACK = new Color(45, 45, 45);
    public static final Color WHITE = new Color(236, 237, 242);
    public static final Color LIGHT_PINK = new Color(251, 195, 228);
    public static final Color GRAY = new Color(211, 211, 211);
    
    
    // Customizes the appearance of a JButton
    public static JButton setButtonUI(JButton button)  {
        JButton b = button;

        Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        
        Border frame = BorderFactory.createCompoundBorder(raised, lowered);
        Border space = BorderFactory.createEmptyBorder(5,15,5,15);
        
        Border compound = BorderFactory.createCompoundBorder(frame, space);
        
        b.setBorder(compound);
        b.setBackground(Colors.WHITE);
        b.setForeground(Colors.BLACK);
        
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        return b;
    }
}
