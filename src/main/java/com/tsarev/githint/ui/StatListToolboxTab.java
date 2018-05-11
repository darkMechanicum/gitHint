package com.tsarev.githint.ui;

import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Statistics tool window tab contents.
 */
public class StatListToolboxTab extends JPanel {

    /**
     * Simple adds label to table cell.
     */
    private static final TableCellRenderer DEFAULT_CELL_RENDERER =
            (table1, value, isSelected, hasFocus, row, column) -> new JLabel(value.toString());

    /**
     * Renders list from collection elements if collection is provided, or fallbacks to default.
     */
    private static final TableCellRenderer COLLECTION_LIST_RENDERER = StatListToolboxTab::renderCollection;

    /**
     * Constructor.
     *
     * @param model used data model
     */
    public StatListToolboxTab(TableModel model) {
        super(new GridLayout(1, 1));
        this.add(createStatTable(model));
    }

    /**
     * Generate data table.
     */
    private static JBTable createStatTable(TableModel tableModel) {
        // Use override to avoid column model configuring.
        return new JBTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                switch (column) {
                    case 0 : return DEFAULT_CELL_RENDERER;
                    case 1 : return COLLECTION_LIST_RENDERER;
                    case 2 : return DEFAULT_CELL_RENDERER;
                    default: throw new RuntimeException();
                }
            }
        };
    }

    /**
     * Render cell collection.
     */
    private static Component renderCollection(JTable table,
                                              Object value,
                                              boolean isSelected,
                                              boolean hasFocus,
                                              int row,
                                              int column) {
        if (Collection.class.isInstance(value)) {
            Collection<?> asCollection = (Collection<?>) value;
            JBList<?> renderedList = new JBList<>(asCollection);
            renderedList.setCellRenderer((ListCellRenderer<Object>) (list, listValue, index, isSelected1, cellHasFocus) -> new JLabel(listValue.toString()));
            return renderedList;
        } else {
            return DEFAULT_CELL_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
