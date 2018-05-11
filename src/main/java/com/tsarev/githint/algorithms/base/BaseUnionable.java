package com.tsarev.githint.algorithms.base;

import java.util.List;
import java.util.function.Function;

/**
 * Base dynamic algorithm step description.
 */
public interface BaseUnionable<ResultType, PointType extends AbstractPoint> {

    /**
     * Relative identifiers, needed to perform step.
     */
    List<PointType> neededPoints();

    /**
     * Check if step can be performed.
     *
     * @param currentPoint current algorithm identifier
     * @param points absolute identifiers for {@link BaseUnionable#neededPoints()}
     * @param violateChecker function to check if identifier violates external conditions
     */
    boolean canUnion(PointType currentPoint,
                     List<PointType> points,
                     Function<PointType, Boolean> violateChecker);

    /**
     * Perform step.
     */
    ResultType unionPrevious(PointType currentPoint,
                             List<ResultType> previous);

    /**
     * Get id to differentiate steps.
     */
    long getId();
}
