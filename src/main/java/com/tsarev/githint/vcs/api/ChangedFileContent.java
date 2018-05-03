package com.tsarev.githint.vcs.api;

/**
 * Полные сведения об изменениях в файле.
 */
public class ChangedFileContent {

    /**
     * Базовая информация об изменении.
     */
    public final FileChangeCommonInfo commonInfo;

    /**
     * Изменения содержимого.
     */
    public final ChangedLines changedLines;

    public ChangedFileContent(FileChangeCommonInfo commonInfo, ChangedLines changedLines) {
        this.commonInfo = commonInfo;
        this.changedLines = changedLines;
    }
}
