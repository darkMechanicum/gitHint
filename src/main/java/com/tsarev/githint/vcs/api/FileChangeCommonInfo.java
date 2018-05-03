package com.tsarev.githint.vcs.api;

import com.intellij.openapi.vcs.changes.Change;

import java.util.Date;

/**
 * Базовая информация об изменении.
 */
public class FileChangeCommonInfo {

    /**
     * Имя автора.
     */
    public final String author;

    /**
     * Дата изменения.
     */
    public final Date changeDate;

    /**
     * Тип изменения.
     */
    public final ChangeType changeType;

    public FileChangeCommonInfo(String author,
                                Date changeDate,
                                ChangeType changeType) {
        this.author = author;
        this.changeDate = changeDate;
        this.changeType = changeType;
    }

    /**
     * Возможные типы изменения.
     */
    public enum ChangeType {
        CHANGED,
        CREATED,
        MOVED,
        DELETED;

        /**
         * Конвертация из предложенного в {@link com.intellij.openapi.vcs} перечисления.
         */
        public static FileChangeCommonInfo.ChangeType fromIdeaType(Change.Type type) {
            switch (type) {
                case NEW:
                    return FileChangeCommonInfo.ChangeType.CREATED;
                case MOVED:
                    return FileChangeCommonInfo.ChangeType.MOVED;
                case DELETED:
                    return FileChangeCommonInfo.ChangeType.DELETED;
                case MODIFICATION:
                    return FileChangeCommonInfo.ChangeType.CHANGED;
                default:
                    throw new RuntimeException();
            }
        }
    }
}
