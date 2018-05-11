package com.tsarev.githint.vcs.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File changes, bound to lines.
 */
public class ChangedLines {

    public final String[] newContent;

    public final String[] oldContent;

    public final List<ChangedLineBlock> changedBlocks;

    public ChangedLines(String[] newContent,
                        String[] oldContent,
                        List<ChangedLineBlock> changedBlocks) {
        this.newContent = newContent;
        this.oldContent = oldContent;
        this.changedBlocks = Collections.unmodifiableList(changedBlocks);
    }

    /**
     * One changed block description.
     */
    public static class ChangedLineBlock {

        public final List<String> newLines;

        public final List<String> oldLines;

        public final int newStart;

        public final int oldStart;

        public ChangedLineBlock(List<String> newLines,
                                List<String> oldLines,
                                int newStart,
                                int oldStart) {
            this.newLines = new ArrayList<>(newLines);
            this.oldLines = new ArrayList<>(oldLines);
            this.newStart = newStart;
            this.oldStart = oldStart;
        }

        @Override
        public String toString() {
            return "Old start: " + oldStart + "\n" +
                    "New start: " + newStart + "\n" +
                    "Old content: " + oldLines.toString() + "\n" +
                    "New content: " + newLines.toString();
        }
    }

}
