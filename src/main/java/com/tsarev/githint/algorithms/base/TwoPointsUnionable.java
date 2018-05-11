package com.tsarev.githint.algorithms.base;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Base algorithm step with two relative points precondition
 * and one external precondition.
 */
public final class TwoPointsUnionable<ResultType, PointType extends AbstractPoint<PointType>>
        extends IndentifiableUnionable<ResultType, PointType> {

    private final List<PointType> relative;

    private final Function<PointType, Boolean> checker;

    private final BiFunction<ResultType, ResultType, ResultType> merger;

    public TwoPointsUnionable(long id,
                              PointType firstRelative,
                              PointType secondRelative,
                              Function<PointType, Boolean> checker,
                              BiFunction<ResultType, ResultType, ResultType> merger) {
        super(id);
        this.checker = checker;
        this.merger = merger;
        this.relative = Arrays.asList(firstRelative, secondRelative);
    }

    @Override
    public List<PointType> neededPoints() {
        return relative;
    }

    @Override
    public boolean canUnion(PointType currentPoint,
                            List<PointType> points,
                            Function<PointType, Boolean> violateChecker) {
        assert points.size() == 2;
        PointType firstPoint = points.get(0);
        PointType secondPoint = points.get(1);
        boolean hasAny = !violateChecker.apply(firstPoint) && !violateChecker.apply(secondPoint);
        return hasAny && checker.apply(currentPoint);
    }

    @Override
    public ResultType unionPrevious(PointType currentPoint,
                                    List<ResultType> previous) {
        assert previous.size() == 2;
        return merger.apply(previous.get(0), previous.get(1));
    }
}
