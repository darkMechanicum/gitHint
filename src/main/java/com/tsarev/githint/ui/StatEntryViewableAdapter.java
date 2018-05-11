package com.tsarev.githint.ui;

import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.statistics.common.CommonStatTypes;

/**
 * Simple stat entry adapter to view it.
 */
public class StatEntryViewableAdapter implements TabViewable {

    /**
     * Current number of columns.
     */
    private static final int TABLE_COLUMNS = 3;

    /**
     * Stat entry to be displayed.
     */
    private final StatEntry<CommonStatTypes, ?> statEntry;

    /**
     * Constructor.
     *
     * @param statEntry entry to view
     */
    public StatEntryViewableAdapter(StatEntry<CommonStatTypes, ?> statEntry) {
        this.statEntry = statEntry;
    }

    @Override
    public int getColumnNumber() {
        return TABLE_COLUMNS;
    }

    @Override
    public Object getColumnContent(int columnIndex) {
        switch (columnIndex) {
            case 0 : return statEntry.key;
            case 1 : return statEntry.authors;
            case 2 : return statEntry.data;
            default: throw new RuntimeException();
        }
    }
}
