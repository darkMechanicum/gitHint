package com.tsarev.githint.statistics.common.accumulators;

import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.statistics.common.CommonStatTypes;
import com.tsarev.githint.statistics.common.StatAccumulator;
import com.tsarev.githint.vcs.api.ChangedFileContent;
import com.tsarev.githint.vcs.api.ChangedLines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Агрегатор статистики для полчения автора с максимальным количеством новых линий.
 */
public class MaxAverageOldNewLinesDiff implements StatAccumulator<CommonStatTypes, Long> {

    /**
     * Old new diff aggregation table.
     */
    private Map<String, Long> agregation = new HashMap<>();

    /**
     * Old new diff aggregation count table.
     */
    private Map<String, Long> agregationCount = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public void addData(ChangedFileContent changedContent) {
        String author = changedContent.commonInfo.author;
        for (ChangedLines.ChangedLineBlock block : changedContent.changedLines.changedBlocks) {
            Long newLinesSize = (long) block.newLines.size();
            Long oldLinesSize = (long) block.oldLines.size();
            long absChangedDiff = Math.abs(newLinesSize - oldLinesSize);
            if (!agregation.containsKey(author)) {
                agregation.put(author, absChangedDiff);
                agregationCount.put(author, 1L);
            } else {
                agregation.put(author, agregation.get(author) + absChangedDiff);
                agregationCount.put(author, agregationCount.get(author) + 1);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public StatEntry<CommonStatTypes, Long> getStat() {
        Collection<String> maxNewLinesAuthors = new ArrayList<>();
        long maxAvg = -1;
        for (String author : agregation.keySet()) {
            long valueCandidate = agregation.get(author) / agregationCount.get(author);
            if (maxAvg == valueCandidate) {
                maxNewLinesAuthors.add(author);
            } else if (valueCandidate > maxAvg) {
                maxAvg = valueCandidate;
                maxNewLinesAuthors.clear();
                maxNewLinesAuthors.add(author);
            }
        }
        return new StatEntry<>(getKey(), maxNewLinesAuthors, maxAvg);
    }

    /** {@inheritDoc} */
    @Override
    public CommonStatTypes getKey() {
        return CommonStatTypes.MAX_AVG_OLD_NEW_LINES_DIFF;
    }
}
