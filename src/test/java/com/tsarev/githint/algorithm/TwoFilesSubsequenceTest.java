package com.tsarev.githint.algorithm;

import com.tsarev.githint.infrastructure.LoadFiles;
import com.tsarev.githint.infrastructure.ReadFileSupportTest;
import com.tsarev.githint.algorithms.subsequence.LongestSubsequenceAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Тест на проверку работы алгоритма поиска подпоследовательности.
 */
public class TwoFilesSubsequenceTest extends ReadFileSupportTest {

    /**
     * Наибольшая длина общей подпоследовательности строк в файле.
     */
    private static final int MAX_SUBSEQUENCE_LENGTH = 6;

    @Test
    @LoadFiles(firstFile = "diffProviderTests/shortComplexContent1.txt",
            secondFile = "diffProviderTests/shortComplexContent2.txt")
    public void test() {
        String[] firstLines = parseFile(getFirstContent());
        String[] secondLines = parseFile(getSecondContent());

        LongestSubsequenceAlgorithm algorithm = new LongestSubsequenceAlgorithm(firstLines, secondLines);
        algorithm.run();

        List<Integer> firstTrace = algorithm.getFirstArrayIndexTrace();
        String[] firstSubsequence = getFromIndexes(firstLines, firstTrace);
        List<Integer> secondTrace = algorithm.getSecondArrayIndexTrace();
        String[] secondSubsequence = getFromIndexes(secondLines, secondTrace);

        Assert.assertEquals("Должно получится " + MAX_SUBSEQUENCE_LENGTH,
                MAX_SUBSEQUENCE_LENGTH,
                algorithm.getResult());
        Assert.assertArrayEquals("Полученные подпоследовательности должны быть равны",
                firstSubsequence,
                secondSubsequence);
    }

    /**
     * Разбить файл на строки.
     */
    private String[] parseFile(String rawFile) {
        return rawFile.split("\n");
    }

    /**
     * Составление подпоследовательности строк файла по индексам.
     */
    private String[] getFromIndexes(String[] raw, List<Integer> indexes) {
        return indexes.stream()
                .map(i -> raw[i])
                .toArray(String[]::new);
    }
}
