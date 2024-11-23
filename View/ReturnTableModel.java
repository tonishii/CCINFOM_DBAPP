package View;

import Model.enums.ReturnReason;
import Model.enums.ReturnStatus;
import Model.Return;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;

public class ReturnTableModel extends DefaultTableModel {
    private final ArrayList<Return> returns;
    private final String[] columnNames = {"Order ID", "Product ID", "Courier ID", "Return Reason", "Return Description", "Return Date", "Return Status"};
    private final ArrayList<Boolean> selectedRows;

    public ReturnTableModel(ArrayList<Return> returns) {
        this.returns = new ArrayList<>(returns);

        this.selectedRows = new ArrayList<>();
        for (int i = 0; i < this.returns.size(); i++) {
            selectedRows.add(false); // Initialize all rows as unselected
        }
    }

    public Return getReturn(int index) {
        return returns.get(index);
    }

    @Override
    public int getRowCount() {
        if (returns == null) {
            return 0;
        }
        return returns.size();
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
        Return returnObj = returns.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> returnObj.getOrderID();
            case 1 -> returnObj.getProductID();
            case 2 -> returnObj.getCourierID();
            case 3 -> returnObj.getReason();
            case 4 -> returnObj.getDescription();
            case 5 -> returnObj.getDate();
            case 6 -> returnObj.getStatus();
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
            case 3 -> {
                return ReturnReason.class;
            }
            case 4 -> {
                return String.class;
            }
            case 5 -> {
                return Date.class;
            }
            case 6 -> {
                return ReturnStatus.class;
            }
        }
        return null;
    }
}
