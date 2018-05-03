package com.tsarev.githint.vcs.api;

/**
 * Интерфейс, преднащначенный для анализа различий состава двух ревизий.
 */
public interface FileDiffProvider {

    /**
     * Анализ и получение различий между двумя файлами, представленными строками.
     *
     * @param first содержимое первого файла
     * @param second содержимое второго файла
     */
    ChangedLines parseChanges(String first, String second);
}
