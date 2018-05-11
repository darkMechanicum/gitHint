package com.tsarev.githint.vcs.api;

import com.intellij.openapi.vcs.changes.Change;

import java.util.Date;

/**
 * Common change info.
 */
public class FileChangeCommonInfo {

    /**
     * Author name.
     */
    public final String author;

    /**
     * Change date.
     */
    public final Date changeDate;

    /**
     * Change type.
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
     * Possible change type.
     */
    public enum ChangeType {
        CHANGED,
        CREATED,
        MOVED,
        DELETED;

        /**
         * Type conversion from {@link com.intellij.openapi.vcs}.
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
