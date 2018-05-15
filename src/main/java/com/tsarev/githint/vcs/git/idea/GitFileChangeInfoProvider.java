package com.tsarev.githint.vcs.git.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vcs.*;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vcs.versionBrowser.ChangeBrowserSettings;
import com.intellij.openapi.vcs.versionBrowser.CommittedChangeList;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;
import com.tsarev.githint.vcs.api.*;
import git4idea.GitVcs;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * Idea git integration based implementation.
 */
public class GitFileChangeInfoProvider implements FileChangeInfoProvider {

    /**
     * Difference provider implementation.
     */
    private final FileDiffProvider diffProvider;

    /**
     * Constructor.
     *
     * @param diffProvider selected difference provider implementation
     */
    public GitFileChangeInfoProvider(FileDiffProvider diffProvider) {
        this.diffProvider = diffProvider;
    }

    // ---- [GitFileChangeInfoProvider] Interface methods ------------------------------------------

    /** {@inheritDoc} */
    @Override
    public ChangedFileContent getChangedContentFor(Project project,
                                                   VirtualFile file,
                                                   VcsRevisionNumber revision) {
        Pair<Change, CommittedChangeList> pair = getFileChangeForRevision(project, file, revision);
        if (pair != null) {
            FileChangeCommonInfo commonInfo = getFileChangeCommonInfo(pair);
            ChangedLines changedLines = parseChange(pair.getFirst());
            return new ChangedFileContent(commonInfo, changedLines);
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<ChangedFileContent> getChangedContentFor(Project project,
                                                         VirtualFile file,
                                                         List<VcsRevisionNumber> revisions) {
        ArrayList<ChangedFileContent> contents = new ArrayList<>();
        for (VcsRevisionNumber revision : revisions) {
            contents.add(getChangedContentFor(project, file, revision));
        }
        return contents;
    }

    /** {@inheritDoc} */
    @Override
    public Stream<ChangedFileContent> getChangedContentFor(Project project,
                                                           VirtualFile file,
                                                           Stream<VcsRevisionNumber> revisions) {
        return revisions.map(revision -> this.getChangedContentFor(project, file, revision));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileChangeCommonInfo getCommonChangeInfoFor(Project project,
                                                       VirtualFile file,
                                                       VcsRevisionNumber revision) {
        Pair<Change, CommittedChangeList> pair = getFileChangeForRevision(project, file, revision);
        return getFileChangeCommonInfo(pair);
    }

    /** {@inheritDoc} */
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
     * {@inheritDoc}
     */
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

    // ---- [GitFileChangeInfoProvider] Private methods ------------------------------------------

    /**
     * Get commit change info from changelist and change.
     *
     * @return corresponding {@link FileChangeCommonInfo}, or {@code null} if pair is {@code null}.
     */
    private FileChangeCommonInfo getFileChangeCommonInfo(@Nullable Pair<Change, CommittedChangeList> pair) {
        if (pair != null) {
            return new FileChangeCommonInfo(
                    pair.getSecond().getCommitterName(),
                    pair.getSecond().getCommitDate(),
                    FileChangeCommonInfo.ChangeType.fromIdeaType(pair.getFirst().getType())
            );
        } else {
            return null;
        }
    }

    /**
     * Get revision changed lines.
     */
    private ChangedLines parseChange(Change change) {
        try {
            String beforeContent = change.getBeforeRevision().getContent();
            String afterContent = change.getAfterRevision().getContent();
            return diffProvider.parseChanges(beforeContent, afterContent);
        } catch (VcsException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Get {@link Change} for selected revision and file.
     *
     * @return {@link Change} and its {@link CommittedChangeList}
     * or {@code null} if no revision were found or no file in
     * changelist match revision number.
     */
    private Pair<Change, CommittedChangeList> getFileChangeForRevision(Project project,
                                                                       VirtualFile file,
                                                                       VcsRevisionNumber revision) {
        // Get GitVcs.
        FilePath filePath = VcsUtil.getFilePath(file);
        GitVcs gitVcs = GitVcs.getInstance(project);

        if (gitVcs == null) {
            throw new RuntimeException("No git enabled at project.");
        }

        //noinspection unchecked We get here right type, as it is declared in GitVcs.
        CommittedChangesProvider<CommittedChangeList, ChangeBrowserSettings> committedChangesProvider = gitVcs.getCommittedChangesProvider();

        if (committedChangesProvider == null) {
            throw new RuntimeException("No CommittedChangesProvider found for GitVcs.");
        }

        // Get changelist.
        Pair<CommittedChangeList, FilePath> oneList;
        try {
            oneList = committedChangesProvider.getOneList(file, revision);
        } catch (VcsException e) {
            throw new RuntimeException("Failed to read changelist for revision.");
        }

        if (oneList == null) {
            return null;
        }

        // Find corresponding file change.
        CommittedChangeList changeList = oneList.getFirst();
        Collection<Change> changes = changeList.getChanges();
        for (Change change : changes) {
            ContentRevision chosenRevision = change.getAfterRevision();
            if (chosenRevision == null) {
                chosenRevision = change.getBeforeRevision();
            }
            if (chosenRevision == null) {
                return null;
            }
            if (FileUtil.filesEqual(chosenRevision.getFile().getIOFile(), filePath.getIOFile())) {
                return new Pair<>(change, changeList);
            }
        }
        return null;
    }
}
