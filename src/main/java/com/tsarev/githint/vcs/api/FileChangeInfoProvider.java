package com.tsarev.githint.vcs.api;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

/**
 * Интерфейс получения сведений об изменениях файла.
 */
public interface FileChangeInfoProvider {

    /**
     * Получение сведений об изменениях файла.
     */
    ChangedFileContent getChangedContentFor(Project project,
                                            VirtualFile file,
                                            VcsRevisionNumber revision);

    /**
     * Получение сведений об изменениях файла для нескольких ревизий.
     */
    List<ChangedFileContent> getChangedContentFor(Project project,
                                                     VirtualFile file,
                                                     List<VcsRevisionNumber> revisions);

    /**
     * Получение сведений об изменениях файла для его последней ревизии.
     */
    ChangedFileContent getChangedContentForLatest(Project project,
                                                  VirtualFile file);

    /**
     * Получение базовых сведений об изменениях файла.
     */
    FileChangeCommonInfo getCommonChangeInfoFor(Project project,
                                                VirtualFile file,
                                                VcsRevisionNumber revision);

    /**
     * Получение базовых сведений об изменениях файла для последней ревизии.
     */
    FileChangeCommonInfo getCommonChangeInfoForLatest(Project project,
                                                      VirtualFile file);

}
