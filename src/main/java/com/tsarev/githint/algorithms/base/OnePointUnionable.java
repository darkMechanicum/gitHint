package com.tsarev.githint.algorithms.base;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Базовый шаг алгоритма с двумя относительными точками
 * и проверокой, которая принимает текущую точку в каестве аргумента.
 */
public final class OnePointUnionable<ResultType, PointerType extends AbstractPoint<PointerType>>
        extends IndentifiableUnionable<ResultType,PointerType> {

    private final List<PointerType> relative;

    private final Function<PointerType, Boolean> checker;

    private final Function<ResultType, ResultType> merger;

    public OnePointUnionable(long id,
                             PointerType relative,
                             Function<PointerType, Boolean> checker,
                             Function<ResultType, ResultType> merger) {
        super(id);
        this.checker = checker;
        this.merger = merger;
        this.relative = Collections.singletonList(relative);
    }

    @Override
    public List<PointerType> neededPoints() {
        return relative;
    }

    @Override
    public boolean canUnion(PointerType currentPoint,
                            List<PointerType> points,
                            Function<PointerType, Boolean> violateChecker) {
        assert points.size() == 1;
        return !violateChecker.apply(points.get(0)) && checker.apply(currentPoint);
    }

    @Override
    public ResultType unionPrevious(PointerType currentPoint, List<ResultType> previous) {
        assert previous.size() == 1;
        return merger.apply(previous.get(0));
    }
}
