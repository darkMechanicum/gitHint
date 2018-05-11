package com.tsarev.githint.algorithms.subsequence;

import java.util.List;

/**
 * LCS algorithm adapter for {@link String}.
 */
public class StringSubsequenceAlgorithmAdapter {

    /**
     * First string.
     */
    private String firstString;

    /**
     * LCS length.
     */
    private long result;

    /**
     * LCS.
     */
    private String longestSubsequence;

    /**
     * LCS algorithm.
     */
    private final LongestSubsequenceAlgorithm subsequenceAlgorithm;

    /**
     * Constructor.
     */
    public StringSubsequenceAlgorithmAdapter(String firstString,
                                             String secondString) {
        this.firstString = firstString;
        this.subsequenceAlgorithm = new LongestSubsequenceAlgorithm(
                wrapperChars(firstString),
                wrapperChars(secondString)
        );
    }

    /**
     * Получение массива символов.
     */
    public Character[] wrapperChars(String raw) {
        char[] chars = raw.toCharArray();
        Character[] charsWrapped = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            charsWrapped[i] = chars[i];
        }
        return charsWrapped;
    }

    /**
     * Run the algorithm.
     */
    public void run() {
        subsequenceAlgorithm.run();
        List<Integer> indexTrace = subsequenceAlgorithm.getFirstArrayIndexTrace();
        longestSubsequence = getSubsequence(firstString, indexTrace);
    }

    /**
     * Get LCS from source string and LCS indexes.
     */
    private String getSubsequence(String source, List<Integer> indexes) {
        char[] chars = source.toCharArray();
        char[] subsequence = new char[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            subsequence[i] = chars[indexes.get(i)];
        }
        return new String(subsequence);
    }

    /**
     * Get LCS length.
     */
    public long getResult() {
        return result;
    }

    /**
     * Get LCS.
     */
    public String getLongestSubsequence() {
        return longestSubsequence;
    }
}
