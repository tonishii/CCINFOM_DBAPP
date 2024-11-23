package View;

import Model.enums.OrderStatus;
import Model.*;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;

public class OrderClassTableModel extends DefaultTableModel {
    private final ArrayList<Order> orders;
    private final String[] columnNames = {"Order ID", "User ID", "Courier ID", "Purchase Date", "Total Price", "Order Status", "Receive Date"};
    private final ArrayList<Boolean> selectedRows;

    public OrderClassTableModel(ArrayList<Order> orders) {
        this.orders = new ArrayList<>(orders);

        this.selectedRows = new ArrayList<>();
        for (int i = 0; i < this.orders.size(); i++) {
            selectedRows.add(false); // Initialize all rows as unselected
        }
    }

    public Order getOrder(int index) {
        return orders.get(index);
    }

    @Override
    public int getRowCount() {
        if (orders == null) {
            return 0;
        }
        return orders.size();
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
        Order order = orders.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> order.getOrderID();
            case 1 -> order.getUserID();
            case 2 -> order.getCourierID();
            case 3 -> order.getPurchaseDate();
            case 4 -> order.getTotalPrice();
            case 5 -> order.getStatus();
            case 6 -> order.getReceiveDate();
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
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0, 1, 2 -> {
                return Integer.class;
            }
            case 3, 6 -> {
                return Date.class;
            }
            case 4 -> {
                return Float.class;
            }
            case 5 -> {
                return OrderStatus.class;
            }
        }
        return null;
    }
}
