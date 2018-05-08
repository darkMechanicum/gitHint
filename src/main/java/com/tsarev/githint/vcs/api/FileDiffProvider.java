package com.tsarev.githint.vcs.api;

/**
 * Interface for computing content difference.
 */
public interface FileDiffProvider {

    /**
     * Get difference between two line based contents.
     *
     * @param first first content
     * @param second second content
     */
    ChangedLines parseChanges(String first, String second);
}
