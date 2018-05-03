package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.ChangedFileContent;

/**
 * Интерфейс для описания алгоритма сбора статистики.
 */
public interface StatAccumulator<KeyT, ResultT> {

    /**
     * Добавление данных в акумулятор.
     */
    void addData(ChangedFileContent changedContent);

    /**
     * Получение статистики для накопленных результатов.
     */
    StatEntry<KeyT, ResultT> getStat();

}
