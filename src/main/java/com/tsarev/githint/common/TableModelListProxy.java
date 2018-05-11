package com.tsarev.githint.common;

import org.jetbrains.annotations.NotNull;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class to observe list modifications and notify {@link TableModel} row notifications.
 */
public class TableModelListProxy<T> extends ObservableListProxy<T, AbstractTableModel> implements TableModel {

    /**
     * Inner table model, linked with this list.
     */
    private final AbstractTableModel innerModel;

    /**
     * Fixed column count.
     */
    private final int columnCount;

    /**
     * Function to get row element.
     */
    private final BiFunction<T, Integer, Object> rowElements;

    /**
     * Function to get column class.
     */
    private final Function<Integer, Class<?>> columnClasses;

    /**
     * Function to get column name.
     */
    private final Function<Integer, String> columnNames;

    /**
     * Constructor.
     *
     * @param innerCollection proxied modifiable collection
     * @param rowElements function to get element from row
     * @param columnClasses function to get column class
     * @param columnNames function to get column name
     */
    public TableModelListProxy(List<T> innerCollection,
                               int columnCount,
                               BiFunction<T, Integer, Object> rowElements,
                               Function<Integer, Class<?>> columnClasses,
                               Function<Integer, String> columnNames) {
        super(innerCollection,
                AbstractTableModel::fireTableRowsInserted,
                AbstractTableModel::fireTableRowsDeleted,
                AbstractTableModel::fireTableRowsUpdated);
        this.columnCount = columnCount;
        this.rowElements = rowElements;
        this.columnClasses = columnClasses;
        this.columnNames = columnNames;
        this.innerModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return TableModelListProxy.this.size();
            }

            @Override
            public int getColumnCount() {
                return TableModelListProxy.this.columnCount;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return TableModelListProxy.this.rowElements.apply(TableModelListProxy.this.get(rowIndex), columnIndex);
            }
        };
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new TableModelListProxy<>(
                innerSubList(fromIndex, toIndex),
                columnCount,
                rowElements,
                columnClasses,
                columnNames
        );
    }

    @Override
    protected AbstractTableModel getModel() {
        return innerModel;
    }

    @Override
    public int getRowCount() {
        return this.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.apply(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses.apply(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return innerModel.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return innerModel.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        innerModel.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        innerModel.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        innerModel.removeTableModelListener(l);
    }
}
