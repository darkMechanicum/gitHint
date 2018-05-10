package com.tsarev.githint.ui;

import com.intellij.openapi.components.ProjectComponent;
import com.tsarev.githint.common.ObservableListProxy;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Single instance holder class responsible for saving view information.
 */
public class StatListViewDataHolder implements ProjectComponent {

    /**
     * Viewed data.
     */
    private final ObservableListProxy<ListViewable> data =
            new ObservableListProxy<>(new ArrayList<>());

    /**
     * Get inner data as {@link ListModel} for viewing.
     */
    public AbstractListModel<ListViewable> getAsModel() {
        return data;
    }

    /**
     * Get inner data as {@link List} for changing.
     */
    public List<ListViewable> getAsList() {
        return data;
    }
}
