package com.tsarev.githint.statistics.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Statistics entry.
 */
public class StatEntry<KeyT, DataT extends EntryData> {

    /**
     * Entry key.
     */
    public final KeyT key;

    /**
     * Authors, whose statistic is higher.
     */
    public final Collection<String> authors;

    /**
     * Statistics value.
     */
    public final DataT data;

    /**
     * Constructor.
     */
    public StatEntry(KeyT key, Collection<String> authors, DataT data) {
        this.key = key;
        this.authors = Collections.unmodifiableCollection(new ArrayList<>(authors));
        this.data = data;
    }
}
