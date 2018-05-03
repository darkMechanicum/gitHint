package com.tsarev.githint.statistics.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Запись о статистике.
 */
public class StatEntry<KeyT, DataT> {

    /**
     * Ключ записи.
     */
    public final KeyT key;

    /**
     * Авторы, за коммиты которого выше всего данный показатель.
     */
    public final Collection<String> authors;

    /**
     * Значение показателя.
     */
    public final DataT data;

    /**
     * Конструктор.
     */
    public StatEntry(KeyT key, Collection<String> authors, DataT data) {
        this.key = key;
        this.authors = Collections.unmodifiableCollection(new ArrayList<>(authors));
        this.data = data;
    }
}
