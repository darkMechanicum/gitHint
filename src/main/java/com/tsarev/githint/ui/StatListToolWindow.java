package com.tsarev.githint.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Class to represent statistics tool window initialization.
 */
public class StatListToolWindow implements ToolWindowFactory {

    /**
     * Identifier of this tool window.
     */
    public static final String ID = "statisticsToolWindow";

    @Override
    public void createToolWindowContent(@NotNull Project project,
                                        @NotNull ToolWindow toolWindow) {

    }
}
