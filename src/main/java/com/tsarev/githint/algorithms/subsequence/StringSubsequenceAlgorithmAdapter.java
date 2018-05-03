package com.tsarev.githint.algorithms.subsequence;

import java.util.List;

/**
 * Алгоритм поиска большей общей подпоследовательности в строках.
 */
public class StringSubsequenceAlgorithmAdapter {

    /**
     * Первая строка.
     */
    private String firstString;

    /**
     * Длина самой большой подпоследовательности.
     */
    private long result;

    /**
     * Самая большая подпоследовательность.
     */
    private String longestSubsequence;

    /**
     * Динамический алгоритм.
     */
    private final LongestSubsequenceAlgorithm subsequenceAlgorithm;

    /**
     * Конструктор.
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
     * Выполнение алгоритма.
     */
    public void run() {
        subsequenceAlgorithm.run();
        List<Integer> indexTrace = subsequenceAlgorithm.getFirstArrayIndexTrace();
        longestSubsequence = getSubsequence(firstString, indexTrace);
    }

    /**
     * Получение подпоследовательности по индексам.
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
     * Получение длины наибольшей подпоследовательности.
     */
    public long getResult() {
        return result;
    }

    /**
     * Получение подпоследовательности в виде строки.
     */
    public String getLongestSubsequence() {
        return longestSubsequence;
    }

    /**
     * Получение индексов подпоследовательности для первой строки.
     */
    public List<Integer> getFirstIndexes() {
        return subsequenceAlgorithm.getFirstArrayIndexTrace();
    }

    /**
     * Получение индексов подпоследовательности для второй строки.
     */
    public List<Integer> getSecondIndexes() {
        return subsequenceAlgorithm.getSecondArrayIndexTrace();
    }
}
