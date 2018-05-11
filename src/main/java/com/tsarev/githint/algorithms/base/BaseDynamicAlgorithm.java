package com.tsarev.githint.algorithms.base;

import com.tsarev.githint.algorithms.subsequence.LongestSubsequenceAlgorithm;
import com.tsarev.githint.algorithms.twodimensional.TwoDimensionalDynamicAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Base class for any dynamic algorithm.
 * </p>
 * <p>
 * Core idea is at every moment there are some intermediate results
 * available with their identifiers ({@link ResultHolder}), which can be
 * merged with some algorithm steps ({@link BaseUnionable}).
 * </p>
 * <p>
 * There is array of possible identifiers that can be violated
 * (for example two dimensional array coordinates) to divide these results.
 * For each identifier, one by one, algorithm chooses one step to perform, based on steps
 * preconditions. Then, algorithm step merges previous results and saves new result
 * at the current identifier. This continues, while there are some identifiers left.
 * </p>
 * <p>
 * See two dimensional implementation: {@link TwoDimensionalDynamicAlgorithm}
 * <br>
 * See the example: {@link LongestSubsequenceAlgorithm}.
 * </p>
 */
public abstract class BaseDynamicAlgorithm<ResultType, PointType extends AbstractPoint<PointType>> {

    /**
     * Algorithm steps description.
     */
    private final Iterable<BaseUnionable<ResultType, PointType>> unionables;

    /**
     * Constructor.
     *
     * @param unionables used steps
     */
    public BaseDynamicAlgorithm(Collection<BaseUnionable<ResultType, PointType>> unionables) {
        ArrayList<BaseUnionable<ResultType, PointType>> rawUnionables = new ArrayList<>(unionables);
        this.unionables = Collections.unmodifiableList(rawUnionables);
    }

    /**
     * Get intermediate result by its identifier.
     */
    public abstract ResultHolder getForPoint(PointType point);

    /**
     * Check if identifier does not exists.
     */
    protected abstract boolean violatesBorders(PointType point);

    /**
     * Save intermediate result by its identifier.
     */
    protected abstract void setForPoint(PointType point, ResultHolder holder);

    /**
     * Iterate over identifiers.
     */
    protected abstract Iterable<PointType> getPointIterator();

    /**
     * Get suitable algorithm step.
     */
    private Runnable findOne(PointType currentPoint) throws AlgorithmException {
        Collection<Runnable> candidates = new ArrayList<>();
        for (BaseUnionable<ResultType, PointType> unionable : unionables) {
            List<PointType> shiftedPoints = unionable.neededPoints()
                    .stream()
                    .map(currentPoint::shiftThis)
                    .collect(Collectors.toList());
            if (unionable.canUnion(currentPoint, shiftedPoints, this::violatesBorders)) {
                candidates.add(() -> performUnion(unionable, shiftedPoints, currentPoint));
            }
        }
        if (candidates.size() != 1) {
            throw new AlgorithmException("Больше одного кондидата для точки " + currentPoint);
        }
        return candidates.iterator().next();
    }

    /**
     * Run algorithm.
     */
    public final ResultHolder run() throws AlgorithmException {
        PointType last = null;
        for (PointType currentPoint : getPointIterator()) {
            findOne(currentPoint).run();
            last = currentPoint;
        }
        return getForPoint(last);
    }

    /**
     * Perform step.
     */
    private void performUnion(BaseUnionable<ResultType, PointType> unionable,
                              List<PointType> selectedPoints,
                              PointType currentPoint) {
        ResultType merged = unionable.unionPrevious(currentPoint, getInnerResults(selectedPoints));
        ResultHolder holder = new ResultHolder(merged, currentPoint, selectedPoints, unionable.getId());
        setForPoint(currentPoint, holder);
    }

    /**
     * Get intermediate results by its identifiers honoring order.
     * If there is no result for some point (does not filled yet or not exist),
     * so {@code null} is returned at corresponding list position.
     */
    private List<ResultType> getInnerResults(List<PointType> points) {
        return points.stream()
                .map(point -> Optional.ofNullable(this.getForPoint(point)))
                .map(nullableHolder -> nullableHolder.map(holder -> holder.data))
                .map(nullableData -> nullableData.orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Intermediate result holder.
     */
    public class ResultHolder {
        public final ResultType data;
        public final PointType usedPoint;
        public final List<PointType> previousPoints;
        public final long usedStepId;

        private ResultHolder(ResultType data,
                             PointType usedPoint,
                             List<PointType> previousPoints, long usedStepId) {
            this.data = data;
            this.previousPoints = Collections.unmodifiableList(new ArrayList<>(previousPoints));
            this.usedPoint = usedPoint;
            this.usedStepId = usedStepId;
        }
    }
}
