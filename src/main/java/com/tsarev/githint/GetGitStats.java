package com.tsarev.githint;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.tsarev.githint.vcs.common.SimpleDiffProvider;
import com.tsarev.githint.vcs.git.GitFileChangeInfoProvider;
import com.tsarev.githint.vcs.git.GitHistoryProvider;
import com.tsarev.githint.vcs.api.FileChangeInfoProvider;

public class GetGitStats extends AnAction {
    public GetGitStats() {
        super("Get git statistics");
    }

    public void actionPerformed(AnActionEvent event) {
        VirtualFile currentFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
        Project project = event.getProject();
        if (project == null || currentFile == null) {
            return;
        }

        GitHistoryProvider gitHistoryProvider = new GitHistoryProvider();
        SimpleDiffProvider diffProvider = new SimpleDiffProvider();
        FileChangeInfoProvider changeInfoProvider = new GitFileChangeInfoProvider(diffProvider);

        String collectedSats = BasicStatManager.collectCommonStats(
                project,
                currentFile,
                changeInfoProvider,
                gitHistoryProvider
        );

        Messages.showMessageDialog(project,
                collectedSats,
                "Stats",
                Messages.getInformationIcon());
    }
}