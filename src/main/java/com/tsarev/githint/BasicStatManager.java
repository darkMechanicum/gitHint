package com.tsarev.githint;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.tsarev.githint.statistics.common.CommonCompositeStatProvider;
import com.tsarev.githint.statistics.common.accumulators.MaxAverageOldNewLinesDiff;
import com.tsarev.githint.vcs.api.ChangedFileContent;
import com.tsarev.githint.statistics.api.OverallStat;
import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.FileChangeInfoProvider;
import com.tsarev.githint.vcs.api.FileHistoryProvider;
import com.tsarev.githint.statistics.common.CommonStatTypes;
import com.tsarev.githint.statistics.common.accumulators.MaxNewLines;

import java.util.List;
import java.util.stream.Stream;

public class BasicStatManager {

    public static String collectCommonStats(
            Project project,
            VirtualFile file,
            FileChangeInfoProvider changeInfoProvider,
            FileHistoryProvider historyProvider) {

        CommonCompositeStatProvider statProvider = new CommonCompositeStatProvider();
        statProvider.register(CommonStatTypes.MAX_NEW_LINES, MaxNewLines::new);
        statProvider.register(CommonStatTypes.MAX_AVG_OLD_NEW_LINES_DIFF, MaxAverageOldNewLinesDiff::new);

        List<VcsRevisionNumber> revisionList = historyProvider.getRevisionList(project, file);
        Stream<VcsRevisionNumber> revisions = revisionList.subList(0, revisionList.size() - 2).stream();
        Stream<ChangedFileContent> changed = changeInfoProvider.getChangedContentFor(project, file, revisions);
        OverallStat<CommonStatTypes> stats = statProvider.getAllStatFor(changed);

        StatEntry<CommonStatTypes, Long> maxNewLinesStat = stats.getStatFor(CommonStatTypes.MAX_NEW_LINES, Long.class);
        StatEntry<CommonStatTypes, Long> maxAvgDiffStat = stats.getStatFor(CommonStatTypes.MAX_AVG_OLD_NEW_LINES_DIFF, Long.class);

        String maxNewLinesString = "Max new lines.\n" +
                "Authors: " + maxNewLinesStat.authors + "\n" +
                "Max new lines count " + maxNewLinesStat.data;

        String avgLineDiffString = "Max avg diff.\n" +
                "Authors: " + maxAvgDiffStat.authors + "\n" +
                "Max avg: " + maxAvgDiffStat.data;

        return maxNewLinesString + "\n" +
                "------------------------------\n" +
                avgLineDiffString;
    }
}
