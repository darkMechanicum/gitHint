package com.tsarev.githint.vcs.api;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

/**
 * Интерфейс для получения исторических данных о файле.
 */
public interface FileHistoryProvider {

    /**
     * Получение списка ревизий для файла.
     */
    List<VcsRevisionNumber> getRevisionList(Project project, VirtualFile file);
}
