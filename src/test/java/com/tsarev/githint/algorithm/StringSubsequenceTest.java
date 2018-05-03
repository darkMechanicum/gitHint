package com.tsarev.githint.algorithm;

import com.tsarev.githint.algorithms.subsequence.LongestSubsequenceAlgorithm;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Тест на проверку работы алгоритма поиска подпоследовательности.
 */
public class StringSubsequenceTest {

    /**
     * Разделитель. Нужен для наглядности записи.
     */
    private static final char DELIMITER = '_';

    /**
     * Первая строка. Разделитель для наглядности.
     */
    private static final String FIST_STRING =   "a_1b_c_d_k_e_____e__z_kl_l";
    /**
     * Вторая строка. Разделитель для наглядности.
     */
    private static final String SECOND_STRING = "__1b_2_d___e_345_e_8__kl__";

    /**
     * Наибольшая длина общей подпоследовательности.
     */
    private static final int MAX_SUBSEQUENCE_LENGTH = 7;

    @Test
    public void test() {
        Character[] array1 = parseString(FIST_STRING);
        Character[] array2 = parseString(SECOND_STRING);

        LongestSubsequenceAlgorithm algorithm = new LongestSubsequenceAlgorithm(array1, array2);
        algorithm.run();

        List<Integer> firstTrace = algorithm.getFirstArrayIndexTrace();
        Character[] firstTraceString = getFromIndexes(array1, firstTrace);
        List<Integer> secondTrace = algorithm.getSecondArrayIndexTrace();
        Character[] secondTraceString = getFromIndexes(array2, secondTrace);

        Assert.assertEquals("Должно получится "+ MAX_SUBSEQUENCE_LENGTH,
                MAX_SUBSEQUENCE_LENGTH,
                algorithm.getResult());
        Assert.assertArrayEquals("Полученные подпоследовательности должны быть равны",
                firstTraceString,
                secondTraceString);
    }

    /**
     * Разбить строку на символы, игнорирую разделитель.
     */
    private Character[] parseString(String raw) {
        List<Character> characters = new ArrayList<>();
        for (int i = 0; i < raw.length(); i++) {
            char charAt = raw.charAt(i);
            if (charAt != DELIMITER) {
                characters.add(charAt);
            }
        }
        return characters.toArray(new Character[0]);
    }

    /**
     * Составление подстроки из строки по индексам.
     */
    private Character[] getFromIndexes(Character[] raw, List<Integer> indexes) {
        return indexes.stream()
                .map(i -> raw[i])
                .toArray(Character[]::new);
    }
}
