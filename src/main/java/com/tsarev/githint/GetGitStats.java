package com.tsarev.githint;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.tsarev.githint.common.TableModelListProxy;
import com.tsarev.githint.ui.ListViewable;
import com.tsarev.githint.ui.StatListToolWindow;
import com.tsarev.githint.ui.StatListToolboxTab;
import com.tsarev.githint.vcs.api.FileChangeInfoProvider;
import com.tsarev.githint.vcs.common.SimpleDiffProvider;
import com.tsarev.githint.vcs.git.GitFileChangeInfoProvider;
import com.tsarev.githint.vcs.git.GitHistoryProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        Collection<ListViewable> viewableStats = BasicStatManager.collectCommonStats(
                project,
                currentFile,
                changeInfoProvider,
                gitHistoryProvider
        );

        TableModelListProxy<ListViewable> viewedData = new TableModelListProxy<>(
                new ArrayList<>(),
                3,
                ListViewable::getColumnContent,
                (index) -> Object.class,
                (index) -> "hello");

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(StatListToolWindow.ID);
        ContentManager contentManager = toolWindow.getContentManager();
        StatListToolboxTab component = new StatListToolboxTab(viewedData);
        Content content = contentManager.getFactory().createContent(component, "first", true);
        contentManager.addContent(content);
        viewedData.clear();
        viewedData.addAll(viewableStats);
        toolWindow.show(() -> {});
    }
}