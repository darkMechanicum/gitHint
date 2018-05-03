package com.tsarev.githint.algorithm;

import com.tsarev.githint.infrastructure.LoadFiles;
import com.tsarev.githint.infrastructure.ReadFileSupportTest;
import com.tsarev.githint.algorithms.subsequence.LongestSubsequenceAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Тест на проверку работы алгоритма поиска подпоследовательности у одинакового содержимого.
 */
public class SameFileSubsequenceTest extends ReadFileSupportTest {

    @Test
    @LoadFiles(firstFile = "diffProviderTests/longSimpleContent1.txt",
            secondFile = "diffProviderTests/longSimpleContent1.txt")
    public void test() {
        String[] fileLines = parseFile(getFirstContent());

        int linesLength = fileLines.length;

        LongestSubsequenceAlgorithm algorithm = new LongestSubsequenceAlgorithm(fileLines, fileLines);
        algorithm.run();

        List<Integer> firstTrace = algorithm.getFirstArrayIndexTrace();
        String[] firstSubsequence = getFromIndexes(fileLines, firstTrace);
        List<Integer> secondTrace = algorithm.getSecondArrayIndexTrace();
        String[] secondSubsequence = getFromIndexes(fileLines, secondTrace);

        Assert.assertEquals("Должнпри сравнении файла с самим собой длина " +
                        "подпоследовательности должна быть равна количеству строк.",
                linesLength,
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
