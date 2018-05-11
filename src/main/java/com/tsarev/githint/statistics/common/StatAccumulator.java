package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.EntryData;
import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.ChangedFileContent;

/**
 * Interface reflecting corresponding statistics algorithm.
 */
public interface StatAccumulator<KeyT, ResultT extends EntryData> {

    /**
     * Adding content to accumulator.
     */
    void addData(ChangedFileContent changedContent);

    /**
     * Get accumulated statistics result.
     */
    StatEntry<KeyT, ResultT> getStat();

    /**
     * Get corresponding accumulator key.
     */
    KeyT getKey();

}
