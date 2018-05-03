package com.tsarev.githint.vcs.common;

import com.tsarev.githint.algorithms.subsequence.LongestSubsequenceAlgorithm;
import com.tsarev.githint.vcs.api.ChangedLines;
import com.tsarev.githint.vcs.api.FileDiffProvider;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Базовая реализация, основанная на алгоритме {@link LongestSubsequenceAlgorithm}.
 */
public class SimpleDiffProvider implements FileDiffProvider {

    /** {@inheritDoc} */
    @Override
    public ChangedLines parseChanges(String first, String second) {
        ArrayList<ChangedLines.ChangedLineBlock> changedBlocks = new ArrayList<>();

        String[] beforeLines = parseLines(first);
        String[] afterLines = parseLines(second);

        LongestSubsequenceAlgorithm algorithm = new LongestSubsequenceAlgorithm(beforeLines, afterLines);
        algorithm.run();
        List<Integer> firstIndexes = algorithm.getFirstArrayIndexTrace();
        List<Integer> secondIndexes = algorithm.getSecondArrayIndexTrace();

        int firstCounter = 0;
        int secondCounter = 0;
        boolean changedBlock = false;
        ArrayList<String> oldLines = new ArrayList<>();
        ArrayList<String> newLines = new ArrayList<>();

        while (firstCounter < beforeLines.length && secondCounter < afterLines.length) {
            if (firstIndexes.contains(firstCounter) && secondIndexes.contains(secondCounter)) {
                if (changedBlock) {
                    changedBlock = false;
                    changedBlocks.add(new ChangedLines.ChangedLineBlock(
                            newLines,
                            oldLines,
                            firstCounter - 1,
                            secondCounter - 1
                    ));
                    oldLines.clear();
                    newLines.clear();
                }
                firstCounter++;
                secondCounter++;
                continue;
            }
            changedBlock = true;
            while (!firstIndexes.contains(firstCounter) && firstCounter < beforeLines.length) {
                oldLines.add(beforeLines[firstCounter]);
                firstCounter++;
            }
            while (!secondIndexes.contains(secondCounter) && secondCounter < afterLines.length) {
                newLines.add(afterLines[secondCounter]);
                secondCounter++;
            }
        }

        if (changedBlock) {
            changedBlocks.add(new ChangedLines.ChangedLineBlock(
                    newLines,
                    oldLines,
                    firstCounter - 1,
                    secondCounter - 1
            ));
        }

        return new ChangedLines(afterLines, beforeLines, changedBlocks);
    }

    /**
     * Разбиение содержимого на строки.
     */
    private String[] parseLines(String raw) {
        Scanner scanner = new Scanner(new StringReader(raw));
        ArrayList<String> strings = new ArrayList<>();
        while (scanner.hasNext()) {
            strings.add(scanner.nextLine());
        }
        return strings.toArray(new String[0]);
    }
}
