package com.tsarev.githint.vcs.git.jgit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.tsarev.githint.vcs.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JGit based {@link FileChangeInfoProvider} implementation.
 */
public class JGitFileChangeInfoProvider implements FileChangeInfoProvider {

    /**
     * Difference provider.
     */
    private final FileDiffProvider diffProvider;

    public JGitFileChangeInfoProvider(FileDiffProvider diffProvider) {
        this.diffProvider = diffProvider;
    }

    // ---- [JGitFileChangeInfoProvider] Interface methods ------------------------------------------

    /** {@inheritDoc} */
    @Override
    public ChangedFileContent getChangedContentFor(Project project, VirtualFile file, VcsRevisionNumber revision) {
        String revstr = revision.asString();
        return getChangedFileContent(project, file, revstr);
    }

    /** {@inheritDoc} */
    @Override
    public List<ChangedFileContent> getChangedContentFor(Project project, VirtualFile file, List<VcsRevisionNumber> revisions) {
        return getChangedContentFor(project, file, revisions.stream()).collect(Collectors.toList());
    }

    /** {@inheritDoc} */
    @Override
    public Stream<ChangedFileContent> getChangedContentFor(Project project, VirtualFile file, Stream<VcsRevisionNumber> revisions) {
        return revisions.map((revision) -> this.getChangedContentFor(project, file, revision));
    }

    /** {@inheritDoc} */
    @Override
    public ChangedFileContent getChangedContentForLatest(Project project, VirtualFile file) {
        return getChangedFileContent(project, file, "HEAD");
    }

    /** {@inheritDoc} */
    @Override
    public FileChangeCommonInfo getCommonChangeInfoFor(Project project, VirtualFile file, VcsRevisionNumber revision) {
        return getFileChangeCommonInfo(project, file, revision.asString());
    }

    /** {@inheritDoc} */
    @Override
    public FileChangeCommonInfo getCommonChangeInfoForLatest(Project project, VirtualFile file) {
        return this.getFileChangeCommonInfo(project, file, "HEAD");
    }

    // ---- [JGitFileChangeInfoProvider] Private methods ------------------------------------------

    /**
     * Get common info for specified revision and project.
     * Revision may present as symbolic reference.
     *
     * TODO [735119] [15.05.2018] [Aleksandr.Tsarev] Add file support.
     */
    @NotNull
    private FileChangeCommonInfo getFileChangeCommonInfo(Project project, VirtualFile file, String revstr) {
        // Get project paths.
        String projectDirPath = project.getBaseDir().getPath();
        String gitDirPath = projectDirPath + "/.git";

        // Open repo.
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(new File(gitDirPath))
                .build()) {

            // Find commit.
            ObjectId objectId = repository.resolve(revstr);
            RevCommit revCommit = repository.parseCommit(objectId);

            // Get info.
            return extractCommonInfo(revCommit);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Try to load file for specified commit.
     */
    private String loadFile(String filePath, Repository repository, RevCommit revCommit) throws IOException {
        // Get git tree id for commit.
        RevTree tree = revCommit.getTree();

        // Walk tree.
        TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, tree);

        // Extract blob from tree.
        ObjectId foundFile = treeWalk.getObjectId(0);
        ObjectLoader foundFileLoader = repository.open(foundFile);
        return new String(foundFileLoader.getBytes(), "utf-8");
    }

    /**
     * Get common info from {@link RevCommit}.
     */
    @NotNull
    private FileChangeCommonInfo extractCommonInfo(RevCommit revCommit) {
        PersonIdent authorIdent = revCommit.getAuthorIdent();
        Date commitTime = new Date(revCommit.getCommitTime());
        // TODO [708331] [15.05.2018] [Aleksandr.Tsarev] Add change type loading.
        return new FileChangeCommonInfo(authorIdent.toString(), commitTime, null);
    }

    /**
     * Get changed content for specified project, file and revision.
     * Revision may present as symbolic reference.
     */
    @NotNull
    private ChangedFileContent getChangedFileContent(Project project, VirtualFile file, String revstr) {
        // Build paths.
        String filePath = file.getPath();
        String projectDirPath = project.getBaseDir().getPath();
        String relativeFilePath = filePath.substring(projectDirPath.length() + 1, filePath.length());
        String gitDirPath = projectDirPath + "/.git";

        // Open repo.
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(new File(gitDirPath))
                .build()) {
            // Find commit.
            ObjectId objectId = repository.resolve(revstr);
            RevCommit revCommit = repository.parseCommit(objectId);

            FileChangeCommonInfo commonInfo = extractCommonInfo(revCommit);

            // Find parent commit.
            RevCommit parentCommit = repository.parseCommit(revCommit.getParent(0));

            // Extract contents.
            // TODO [464412] [15.05.2018] [Aleksandr.Tsarev] Add parent search, if there is no revision for file in direct parent.
            String latestFile = loadFile(relativeFilePath, repository, revCommit);
            String previousFile = loadFile(relativeFilePath, repository, parentCommit);

            // Get changes.
            ChangedLines changed = diffProvider.parseChanges(previousFile, latestFile);

            return new ChangedFileContent(commonInfo, changed);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
