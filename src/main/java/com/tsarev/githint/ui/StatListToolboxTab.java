package com.tsarev.githint.ui;

import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;

/**
 * Statistics tool window tab contents.
 */
public class StatListToolboxTab extends JPanel {

    /**
     * Created toolbox list renderer.
     */
    private static final ListCellRenderer<ListViewable> LIST_RENDERER =
            (list, value, index, isSelected, cellHasFocus) -> createListCell(value);

    /**
     * Constructor.
     *
     * @param listModel used data model
     */
    public StatListToolboxTab(ListModel<? extends ListViewable> listModel) {
        super(new GridLayout(1, 1));
        this.add(generateDataList(listModel));
    }

    /**
     * Create a list from elements collection with custom renderer.
     */
    private static JBList generateDataList(ListModel<? extends ListViewable> model) {
        JBList<? extends ListViewable> createdList = new JBList<>(model);
        createdList.setCellRenderer(LIST_RENDERER);
        return createdList;
    }

    /**
     * Create list cell from raw data.
     */
    private static JPanel createListCell(ListViewable viewable) {
        int columnNumber = viewable.getColumnNumber();
        JPanel cellPanel = new JPanel(new GridLayout(1, columnNumber));
        for (int i = 0; i < columnNumber; i++) {
            JLabel label = new JLabel(viewable.getColumnContent(i));
            cellPanel.add(label);
        }
        return cellPanel;
    }
}
