package com.tsarev.githint.ui;

import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Statistics tool window tab contents.
 */
public class StatListToolboxTab extends JPanel {

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
        return new JBTable(tableModel);
    }
}
