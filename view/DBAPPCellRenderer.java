package view;

import javax.swing.*;
import java.awt.*;

public class DBAPPCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        System.out.println("HELLO" + value.toString());

        setText(value.toString());
        return this;
    }
}
