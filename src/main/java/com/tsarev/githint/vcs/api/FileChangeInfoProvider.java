package com.tsarev.githint.vcs.api;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface to get file changed contents.
 */
public interface FileChangeInfoProvider {

    /**
     * Get single file changed contents.
     */
    ChangedFileContent getChangedContentFor(Project project,
                                            VirtualFile file,
                                            VcsRevisionNumber revision);

    /**
     * Get list of file changed contents.
     */
    List<ChangedFileContent> getChangedContentFor(Project project,
                                                  VirtualFile file,
                                                  List<VcsRevisionNumber> revisions);

    /**
     * Get changed content stream based on revisions stream.
     * Warning! Content loading can be made during stream iteration.
     */
    Stream<ChangedFileContent> getChangedContentFor(Project project,
                                                  VirtualFile file,
                                                  Stream<VcsRevisionNumber> revisions);

    /**
     * Get single file changed contents for last revision.
     */
    ChangedFileContent getChangedContentForLatest(Project project,
                                                  VirtualFile file);

    /**
     * Get file change info.
     */
    FileChangeCommonInfo getCommonChangeInfoFor(Project project,
                                                VirtualFile file,
                                                VcsRevisionNumber revision);

    /**
     * Get file change info for last revision.
     */
    FileChangeCommonInfo getCommonChangeInfoForLatest(Project project,
                                                      VirtualFile file);

}
