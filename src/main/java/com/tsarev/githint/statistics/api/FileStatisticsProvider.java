package com.tsarev.githint.statistics.api;

import com.tsarev.githint.statistics.common.CommonStatTypes;
import com.tsarev.githint.vcs.api.ChangedFileContent;

import java.util.List;

/**
 * Интерфейс получения сводки по конкретному файлу.
 */
public interface FileStatisticsProvider<KeyT> {

    /**
     * Получене статистики для файла.
     */
    <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                         CommonStatTypes statType,
                                                         ChangedFileContent changed);

    /**
     * Получение статистики для ряда файлов.
     */
    <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                         CommonStatTypes statType,
                                                         List<ChangedFileContent> changed);

    /**
     * Получение статистики для набора файлов.
     */
    OverallStat<KeyT> getAllStatFor(List<ChangedFileContent> changed);
}
