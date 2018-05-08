package com.tsarev.githint.vcs.api;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface to get file revisions.
 */
public interface FileHistoryProvider {

    /**
     * Get revisions list for the file. Last revision is first in the list.
     */
    List<VcsRevisionNumber> getRevisionList(Project project, VirtualFile file);

    /**
     * Get revisions for the file, from the last to first.
     *
     * @param parallel if returned stream should be parallel
     */
    Stream<VcsRevisionNumber> getRevisionStream(Project project, VirtualFile file, boolean parallel);
}
