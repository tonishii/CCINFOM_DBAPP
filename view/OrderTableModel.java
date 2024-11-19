package view;

import model.OrderContent;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Set;

public class OrderTableModel extends DefaultTableModel {
    private final ArrayList<OrderContent> orderContents;
    private final String[] columnNames = {"Select", "Product Name", "-", "Qty", "+", "Price"};
    private final ArrayList<Boolean> selectedRows;

    public OrderTableModel(Set<OrderContent> orderContents) {
        this.orderContents = new ArrayList<>(orderContents);

        this.selectedRows = new ArrayList<>();
        for (int i = 0; i < orderContents.size(); i++) {
            selectedRows.add(false); // Initialize all rows as unselected
        }
    }

    public OrderContent getOrderContent(int index) {
        return orderContents.get(index);
    }

    @Override
    public int getRowCount() {
        if (orderContents == null) {
            return 0;
        }
        return orderContents.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        OrderContent orderContent = orderContents.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> selectedRows.get(rowIndex);
            case 1 -> orderContent.getProductName();
            case 2 -> "-";
            case 3 -> orderContent.getQuantity();
            case 4 -> "+";
            case 5 -> orderContent.getPriceEach();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            selectedRows.set(rowIndex, (Boolean) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 2 || columnIndex == 4;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else if (columnIndex == 2 || columnIndex == 4) {
            return Integer.class;
        } else {
            return String.class;
        }
    }

    public void updateQuantity(int rowIndex, int direction) {
        OrderContent orderContent = orderContents.get(rowIndex);
        int qty = Math.max(0, orderContent.getQuantity() + direction);
        orderContent.setQuantity(qty);

        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
