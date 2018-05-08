package com.tsarev.githint.algorithms.subsequence;

import com.tsarev.githint.algorithms.base.*;
import com.tsarev.githint.algorithms.twodimensional.TwoDimensionalDynamicAlgorithm;
import com.tsarev.githint.algorithms.twodimensional.TwoDimensionalPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LCS dynamic based algorithm.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Longest_common_subsequence_problem">Longest common subsequence problem</a>
 */
public class LongestSubsequenceAlgorithm {

    /**
     * First sequence.
     */
    private Object[] firstArray;

    /**
     * Second sequence.
     */
    private Object[] secondArray;

    /**
     * LCS length.
     */
    private long result;

    /**
     * LCS elements identifiers from first sequence.
     */
    private List<Integer> firstArrayIndexTrace;

    /**
     * LCS elements identifiers from second sequence.
     */
    private List<Integer> secondArrayIndexTrace;

    /**
     * Used dynamic algorithm.
     */
    private final TwoDimensionalDynamicAlgorithm<Long> dynamicAlgorithm;

    private static final int EMPTY_STEP_ID = 1;
    private static final int EQUAL_STEP_ID = 2;
    private static final int NON_EQUAL_STEP_ID = 3;

    /**
     * Empty step within matrix borders.
     */
    private final BaseUnionable<Long, TwoDimensionalPoint> emptyProvider =
            new NoPointUnionable<>(
                    EMPTY_STEP_ID,
                    (point) -> point.x == 0 || point.y == 0,
                    (point) -> 0L);

    /**
     * Equal elements step.
     */
    private final BaseUnionable<Long, TwoDimensionalPoint> previosEquals =
            new OnePointUnionable<>(
                    EQUAL_STEP_ID,
                    new TwoDimensionalPoint(-1, -1),
                    (point) -> !doesViolateBorders(point.x, point.y) && areEqual(point.x, point.y),
                    (previous) -> previous + 1
            );


    /**
     * Non equal elements step.
     */
    private final BaseUnionable<Long, TwoDimensionalPoint> previousNotEqual =
            new TwoPointsUnionable<>(
                    NON_EQUAL_STEP_ID,
                    new TwoDimensionalPoint(-1, 0),
                    new TwoDimensionalPoint(0, -1),
                    (point) -> !doesViolateBorders(point.x, point.y) && !areEqual(point.x, point.y),
                    Math::max
            );

    /**
     * Constructor.
     *
     * @param firstArray first sequence
     * @param secondArray second sequence
     */
    public LongestSubsequenceAlgorithm(Object[] firstArray,
                                       Object[] secondArray) {
        this.firstArray = firstArray;
        this.secondArray = secondArray;
        this.dynamicAlgorithm = new TwoDimensionalDynamicAlgorithm<>(
                firstArray.length + 1,
                secondArray.length + 1,
                emptyProvider,
                previosEquals,
                previousNotEqual
        );
    }

    /**
     * Perform LCS search.
     */
    public void run() {
        try {
            BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder resultHolder = dynamicAlgorithm.run();
            result = resultHolder.data;
            firstArrayIndexTrace = preTrace(resultHolder, holder -> holder.usedPoint.x - 1);
            secondArrayIndexTrace = preTrace(resultHolder, holder -> holder.usedPoint.y - 1);
        } catch (AlgorithmException e) {
            throw new RuntimeException("Что-то пошло не так.", e);
        }
    }

    public long getResult() {
        return result;
    }

    public List<Integer> getFirstArrayIndexTrace() {
        return firstArrayIndexTrace;
    }

    public List<Integer> getSecondArrayIndexTrace() {
        return secondArrayIndexTrace;
    }

    /**
     * Subsequence identifiers search.
     */
    private List<Integer> preTrace(BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder resultHolder,
                                   Function<BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder, Integer> indexExtractor) {
        List<BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder> previousHolders = resultHolder.previousPoints
                .stream()
                .map(dynamicAlgorithm::getForPoint)
                .collect(Collectors.toList());
        BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder maxPreviousHolder = null;
        for (BaseDynamicAlgorithm<Long, TwoDimensionalPoint>.ResultHolder previousHolder : previousHolders) {
            if (maxPreviousHolder == null) {
                maxPreviousHolder = previousHolder;
            } else {
                maxPreviousHolder = maxPreviousHolder.data < previousHolder.data ? previousHolder : maxPreviousHolder;
            }
        }
        if (maxPreviousHolder == null) {
            return new ArrayList<>();
        }
        List<Integer> previousTrace = preTrace(maxPreviousHolder, indexExtractor);
        if (resultHolder.usedStepId == EQUAL_STEP_ID) {
            previousTrace.add(indexExtractor.apply(resultHolder));
        }
        return previousTrace;
    }

    /**
     * Sequence range check.
     */
    private boolean doesViolateBorders(int x, int y) {
        return x - 1 >= firstArray.length || y - 1 >= secondArray.length;
    }

    /**
     * Sequence elements comparison.
     */
    private boolean areEqual(int x, int y) {
        return Objects.equals(firstArray[x - 1], secondArray[y - 1]);
    }
}
