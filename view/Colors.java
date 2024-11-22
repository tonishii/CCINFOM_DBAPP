package view;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Colors {
    public static final Color PINK = new Color(251, 175, 218);
    public static final Color BLACK = new Color(45, 45, 45);
    public static final Color WHITE = new Color(236, 237, 242);
    public static final Color LIGHT_PINK = new Color(251, 195, 228);
    public static final Color GRAY = new Color(211, 211, 211);
    
    public static JButton setButtonUI(JButton button)  {
        JButton b = button;
//        Border outline = BorderFactory.createLineBorder(Colors.DARK_PINK, 3);

        Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        
        Border frame = BorderFactory.createCompoundBorder(raised, lowered);
        Border space = BorderFactory.createEmptyBorder(5,15,5,15);
        
        Border compound = BorderFactory.createCompoundBorder(frame, space);
        
//        Border compound = BorderFactory.createCompoundBorder(outline, inner);
        
        b.setBorder(compound);
        b.setBackground(Colors.WHITE);
        b.setForeground(Colors.BLACK);
        
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        return b;
    }
}
