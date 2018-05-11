package com.tsarev.githint;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.tsarev.githint.statistics.common.CommonCompositeStatProvider;
import com.tsarev.githint.statistics.common.NumberData;
import com.tsarev.githint.statistics.common.accumulators.MaxAverageOldNewLinesDiff;
import com.tsarev.githint.ui.StatEntryViewableAdapter;
import com.tsarev.githint.ui.TabViewable;
import com.tsarev.githint.vcs.api.ChangedFileContent;
import com.tsarev.githint.statistics.api.OverallStat;
import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.FileChangeInfoProvider;
import com.tsarev.githint.vcs.api.FileHistoryProvider;
import com.tsarev.githint.statistics.common.CommonStatTypes;
import com.tsarev.githint.statistics.common.accumulators.MaxNewLines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class BasicStatManager {

    public static Collection<TabViewable> collectCommonStats(
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

        StatEntry<CommonStatTypes, ?> maxNewLinesStat = stats.getStatFor(CommonStatTypes.MAX_NEW_LINES, NumberData.class);
        StatEntry<CommonStatTypes, ?> maxAvgDiffStat = stats.getStatFor(CommonStatTypes.MAX_AVG_OLD_NEW_LINES_DIFF, NumberData.class);

        ArrayList<TabViewable> result = new ArrayList<>();

        result.add(new StatEntryViewableAdapter(maxNewLinesStat));
        result.add(new StatEntryViewableAdapter(maxAvgDiffStat));

        return result;
    }
}
