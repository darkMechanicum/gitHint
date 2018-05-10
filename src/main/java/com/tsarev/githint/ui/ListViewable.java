package com.tsarev.githint.ui;

/**
 * Something, that can be viewed as list element.
 */
public interface ListViewable {

    /**
     * Column number.
     */
    int getColumnNumber();

    /**
     * Get content for column.
     */
    String getColumnContent(int columnIndex);
}
