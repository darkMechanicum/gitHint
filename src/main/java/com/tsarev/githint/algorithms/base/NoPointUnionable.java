package com.tsarev.githint.algorithms.base;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Base algorithm step with no preconditions.
 */
public final class NoPointUnionable<ResultType, PointType extends AbstractPoint<PointType>>
        extends IndentifiableUnionable<ResultType, PointType> {

    private final Function<PointType, ResultType> emptyResult;

    private final Function<PointType, Boolean> checker;

    public NoPointUnionable(long id,
                            Function<PointType, Boolean> checker,
                            Function<PointType, ResultType> emptyResult) {
        super(id);
        this.emptyResult = emptyResult;
        this.checker = checker;
    }

    @Override
    public List<PointType> neededPoints() {
        return Collections.emptyList();
    }

    @Override
    public boolean canUnion(PointType currentPoint,
                            List<PointType> points,
                            Function<PointType, Boolean> violateChecker) {
        return checker.apply(currentPoint);
    }

    @Override
    public ResultType unionPrevious(PointType currentPoint, List<ResultType> previous) {
        return emptyResult.apply(currentPoint);
    }
}
