package com.tsarev.githint.vcs.api;

/**
 * Full file change info.
 */
public class ChangedFileContent {

    /**
     * Common change info.
     */
    public final FileChangeCommonInfo commonInfo;

    /**
     * Changed content.
     */
    public final ChangedLines changedLines;

    public ChangedFileContent(FileChangeCommonInfo commonInfo, ChangedLines changedLines) {
        this.commonInfo = commonInfo;
        this.changedLines = changedLines;
    }
}
