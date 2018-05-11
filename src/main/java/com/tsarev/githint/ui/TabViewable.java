package com.tsarev.githint.ui;

/**
 * Something, that can be viewed at tab statistics.
 */
public interface TabViewable {

    /**
     * Column number.
     */
    int getColumnNumber();

    /**
     * Get content for column.
     */
    Object getColumnContent(int columnIndex);
}
