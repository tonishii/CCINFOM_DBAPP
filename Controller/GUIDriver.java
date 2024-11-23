package Controller;

import View.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class GUIDriver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // change the default fonts of:
            FontUIResource font = new FontUIResource("Verdana", FontUIResource.PLAIN, 14);
            UIManager.put("Label.font", font); // JLabels
            UIManager.put("Button.font", font); // JButtons
            UIManager.put("List.font", font); // JLists
            UIManager.put("RadioButton.font", font); // JRadioButtons
            UIManager.put("TextField.font", font); // JTextFields
            UIManager.put("OptionPane.buttonFont", font); // JOptionPane's buttons
            UIManager.put("OptionPane.messageFont", font); // JOptionPane's messages
            UIManager.put("CheckBoxMenuItem.font", font); // JComboBox's items
            UIManager.put("TextArea.font", font); // JTextArea

            new MainController(
                new MainFrame(),
                new SQLConnect(),
                new SelectAccount(),
                new UserPage(),
                new SellerPage(),
                new CourierPage()
            );
        });
    }
}
