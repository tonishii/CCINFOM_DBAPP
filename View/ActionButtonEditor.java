package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JButton button;
    private final JTable table;
    private final int direction;
    private int currentRow;

    public ActionButtonEditor(String label, JTable table, int direction) {
        this.table = table;
        this.direction = direction;
        this.button = new JButton(label);

        button.addActionListener(this::updateQuantity);
    }

    public void updateQuantity(ActionEvent e) {
        int currentQty = (Integer) table.getValueAt(currentRow, 3);
        int newQty = Math.max(0, currentQty + direction);

        if (newQty == 0) {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove product", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                DefaultTableModel mdl = (DefaultTableModel) table.getModel();
                mdl.removeRow(currentRow);

                table.setModel(mdl);
            } else {
                JOptionPane.showMessageDialog(null, "Aborting remove...");
            }

        } else {
            table.setValueAt(newQty, currentRow, 3);
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
