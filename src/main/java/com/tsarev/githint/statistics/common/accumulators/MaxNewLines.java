package com.tsarev.githint.statistics.common.accumulators;

import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.statistics.common.CommonStatTypes;
import com.tsarev.githint.statistics.common.NumberData;
import com.tsarev.githint.statistics.common.StatAccumulator;
import com.tsarev.githint.vcs.api.ChangedFileContent;
import com.tsarev.githint.vcs.api.ChangedLines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Statistics aggregator to collect authors, who made maximum new lines.
 */
public class MaxNewLines implements StatAccumulator<CommonStatTypes, NumberData> {

    /**
     * Per author new lines count table.
     */
    private Map<String, Long> agregation = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public void addData(ChangedFileContent changedContent) {
        String author = changedContent.commonInfo.author;
        for (ChangedLines.ChangedLineBlock block : changedContent.changedLines.changedBlocks) {
            Long newLinesSize = (long) block.newLines.size();
            if (!agregation.containsKey(author)) {
                agregation.put(author, newLinesSize);
            } else {
                agregation.put(author, agregation.get(author) + newLinesSize);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public StatEntry<CommonStatTypes, NumberData> getStat() {
        Collection<String> maxNewLinesAuthors = null;
        long maxNewLines = -1;
        for (Map.Entry<String, Long> aggregateEntry : agregation.entrySet()) {
            Long maxCandidateValue = aggregateEntry.getValue();
            String maxCandidateAuthor = aggregateEntry.getKey();
            if (maxNewLinesAuthors == null || maxNewLines == -1) {
                maxNewLinesAuthors = new ArrayList<>();
                maxNewLinesAuthors.add(maxCandidateAuthor);
                maxNewLines = maxCandidateValue;
            } else if (maxNewLines == maxCandidateValue) {
                maxNewLinesAuthors.add(maxCandidateAuthor);
            } else {
                maxNewLines = maxCandidateValue;
                maxNewLinesAuthors.clear();
                maxNewLinesAuthors.add(maxCandidateAuthor);
            }
        }
        return new StatEntry<>(getKey(), maxNewLinesAuthors, new NumberData(maxNewLines));
    }

    /** {@inheritDoc} */
    @Override
    public CommonStatTypes getKey() {
        return CommonStatTypes.MAX_NEW_LINES;
    }
}
