package com.tsarev.githint.vcs.git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import com.tsarev.githint.vcs.api.FileHistoryProvider;
import git4idea.GitFileRevision;
import git4idea.history.GitFileHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация получения исторической информации, основанная на Git.
 */
public class GitHistoryProvider implements FileHistoryProvider {

    /** {@inheritDoc} */
    @Override
    public List<VcsRevisionNumber> getRevisionList(Project project, VirtualFile file) {
        FilePath filePath = VcsUtil.getFilePath(file);
        ArrayList<GitFileRevision> revisions = new ArrayList<>();
        GitFileHistory.loadHistory(
                project,
                filePath,
                null,
                null,
                revisions::add,
                exception -> {});
        return revisions.stream()
                .map(GitFileRevision::getRevisionNumber)
                .collect(Collectors.toList());
    }

}
