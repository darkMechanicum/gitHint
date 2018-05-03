package com.tsarev.githint.vcs.git;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vcs.*;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vcs.versionBrowser.CommittedChangeList;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import com.tsarev.githint.vcs.api.*;
import git4idea.GitVcs;

import java.util.*;

/**
 * Реализация {@link FileChangeInfoProvider} используя Git.
 */
public class GitFileChangeInfoProvider implements FileChangeInfoProvider {

    /**
     * Получатель различий файлов.
     */
    private final FileDiffProvider diffProvider;

    /**
     * Конструктор.
     */
    public GitFileChangeInfoProvider(FileDiffProvider diffProvider) {
        this.diffProvider = diffProvider;
    }

    /** {@inheritDoc} */
    @Override
    public ChangedFileContent getChangedContentFor(Project project,
                                                   VirtualFile file,
                                                   VcsRevisionNumber revision) {
        FileChangeCommonInfo commonChangeInfo = getCommonChangeInfoFor(project, file, revision);

        FilePath filePath = VcsUtil.getFilePath(file);
        GitVcs gitVcs = GitVcs.getInstance(project);
        CommittedChangesProvider committedChangesProvider = gitVcs.getCommittedChangesProvider();
        Pair<CommittedChangeList, FilePath> oneList;
        try {
            long current = System.currentTimeMillis();
            oneList = committedChangesProvider.getOneList(file, revision);
            System.out.println("Time to get one revision list: " + (System.currentTimeMillis() - current));
        } catch (VcsException e) {
            throw new RuntimeException();
        }
        CommittedChangeList changeList = oneList.getFirst();
        Collection<Change> changes = changeList.getChanges();
        for (Change change : changes) {
            ContentRevision lastRevisionContent = change.getAfterRevision();
            if (lastRevisionContent == null) {
                lastRevisionContent = change.getBeforeRevision();
            }
            if (FileUtil.filesEqual(lastRevisionContent.getFile().getIOFile(), filePath.getIOFile())) {
                ChangedLines changedLines = parseChange(change);
                return new ChangedFileContent(commonChangeInfo, changedLines);
            }
        }
        return null;

    }

    @Override
    public List<ChangedFileContent> getChangedContentFor(Project project, VirtualFile file, List<VcsRevisionNumber> revisions) {
        ArrayList<ChangedFileContent> contents = new ArrayList<>();
        for (VcsRevisionNumber revision : revisions) {
            contents.add(getChangedContentFor(project, file, revision));
        }
        return contents;
    }

    /** {@inheritDoc} */
    @Override
    public FileChangeCommonInfo getCommonChangeInfoFor(Project project,
                                                       VirtualFile file,
                                                       VcsRevisionNumber revision) {
        FilePath filePath = VcsUtil.getFilePath(file);
        GitVcs gitVcs = GitVcs.getInstance(project);
        CommittedChangesProvider committedChangesProvider = gitVcs.getCommittedChangesProvider();
        Pair<CommittedChangeList, FilePath> oneList;
        try {
            oneList = committedChangesProvider.getOneList(file, revision);
        } catch (VcsException e) {
            throw new RuntimeException();
        }
        CommittedChangeList changeList = oneList.getFirst();
        Collection<Change> changes = changeList.getChanges();
        for (Change change : changes) {
            ContentRevision lastRevisionContent = change.getAfterRevision();
            if (lastRevisionContent == null) {
                lastRevisionContent = change.getBeforeRevision();
            }
            if (FileUtil.filesEqual(lastRevisionContent.getFile().getIOFile(), filePath.getIOFile())) {
                return new FileChangeCommonInfo(
                        changeList.getCommitterName(),
                        changeList.getCommitDate(),
                        FileChangeCommonInfo.ChangeType.fromIdeaType(change.getType())
                );
            }
        }
        return null;
    }

    @Override
    public FileChangeCommonInfo getCommonChangeInfoForLatest(Project project, VirtualFile file) {
        GitVcs gitVcs = GitVcs.getInstance(project);
        FilePath filePath = VcsUtil.getFilePath(file);
        try {
            VcsFileRevision lastRevision = gitVcs.getVcsHistoryProvider().getLastRevision(filePath);
            return getCommonChangeInfoFor(project, file, lastRevision.getRevisionNumber());
        } catch (VcsException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Получение различий, сделанных в ревизии.
     */
    private ChangedLines parseChange(Change change) {
        try {
            long current = System.currentTimeMillis();
            String beforeContent = change.getBeforeRevision().getContent();
            String afterContent = change.getAfterRevision().getContent();
            System.out.println("Time get content: " + (System.currentTimeMillis() - current));
            return diffProvider.parseChanges(beforeContent, afterContent);
        } catch (VcsException e) {
            throw new RuntimeException();
        } finally {

        }
    }

    /** {@inheritDoc} */
    @Override
    public ChangedFileContent getChangedContentForLatest(Project project, VirtualFile file) {
        GitVcs gitVcs = GitVcs.getInstance(project);
        FilePath filePath = VcsUtil.getFilePath(file);
        try {
            VcsFileRevision lastRevision = gitVcs.getVcsHistoryProvider().getLastRevision(filePath);
            return getChangedContentFor(project, file, lastRevision.getRevisionNumber());
        } catch (VcsException e) {
            throw new RuntimeException();
        }
    }
}
