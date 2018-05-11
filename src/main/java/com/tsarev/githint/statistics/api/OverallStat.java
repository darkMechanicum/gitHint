package com.tsarev.githint.statistics.api;

import java.util.Map;

/**
 * Статистика по несокльких файлам.
 */
public class OverallStat<KeyT> {

    /**
     * Таблица собранной статистики.
     */
    private final Map<KeyT, StatEntry<KeyT, ?>> stats;

    public OverallStat(Map<KeyT, StatEntry<KeyT, ?>> stats) {
        this.stats = stats;
    }

    public <DataT extends EntryData> StatEntry<KeyT, DataT> getStatFor(KeyT statKey, Class<DataT> dataClass) {
        // TODO add check.
        return (StatEntry<KeyT, DataT>) stats.get(statKey);
    }
}
