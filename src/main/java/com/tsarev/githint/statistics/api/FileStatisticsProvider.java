package com.tsarev.githint.statistics.api;

import com.tsarev.githint.vcs.api.ChangedFileContent;

import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * Interface to collect statistics for changed contents.
 * Used statistic methods and method keys depends on implementation.
 * </p>
 * <p>
 * For every statistics method there must be a key, to get results
 * from computed {@link OverallStat}.
 * </p>
 */
public interface FileStatisticsProvider<KeyT> {

    /**
     * Get statistics for one file.
     */
    <DataT> StatEntry<KeyT, DataT> getStatFor(Class<DataT> dataClass,
                                              KeyT statType,
                                              ChangedFileContent changed);

    /**
     * Get statistics for changed content list.
     */
    <DataT> StatEntry<KeyT, DataT> getStatFor(Class<DataT> dataClass,
                                              KeyT statType,
                                              List<ChangedFileContent> changed);

    /**
     * Get statistics for changed content stream.
     * Terminates the stream.
     */
    <DataT> StatEntry<KeyT, DataT> getStatFor(Class<DataT> dataClass,
                                              KeyT statType,
                                              Stream<ChangedFileContent> changed);

    /**
     * Get all registered statistics for changed content list.
     */
    OverallStat<KeyT> getAllStatFor(List<ChangedFileContent> changed);

    /**
     * Get all registered statistics for changed content stream.
     */
    OverallStat<KeyT> getAllStatFor(Stream<ChangedFileContent> changed);
}
