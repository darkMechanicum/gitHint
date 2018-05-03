package com.tsarev.githint.algorithms.base;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Базовый класс для реализации динамических алгоритмов над двумерным массивом.
 */
public abstract class BaseDynamicAlgorithm<ResultType, PointType extends AbstractPoint<PointType>> {

    /**
     * Набор функций слияния результатов.
     */
    private final Iterable<BaseUnionable<ResultType, PointType>> unionables;

    /**
     * Конструктор.
     */
    public BaseDynamicAlgorithm(Collection<BaseUnionable<ResultType, PointType>> unionables) {
        ArrayList<BaseUnionable<ResultType, PointType>> rawUnionables = new ArrayList<>(unionables);
        this.unionables = Collections.unmodifiableList(rawUnionables);
    }

    /**
     * Получение промежуточного результата по его идентификатору.
     */
    public abstract ResultHolder getForPoint(PointType point);

    /**
     * Проверка отсутствия идентификатора.
     */
    protected abstract boolean violatesBorders(PointType point);

    /**
     * Сохранение промежуточного результата по его идентификатору.
     */
    protected abstract void setForPoint(PointType point, ResultHolder holder);

    /**
     * Получение всех идентификаторов по порядку.
     */
    protected abstract Iterable<PointType> getPointIterator();

    /**
     * Поиск подходящей функции.
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
     * Выполнение алгоритма.
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
     * Произвести слияние результатов с выбранной функцией.
     */
    private void performUnion(BaseUnionable<ResultType, PointType> unionable,
                              List<PointType> selectedPoints,
                              PointType currentPoint) {
        ResultType merged = unionable.unionPrevious(currentPoint, getInnerResults(selectedPoints));
        ResultHolder holder = new ResultHolder(merged, currentPoint, selectedPoints, unionable.getId());
        setForPoint(currentPoint, holder);
    }

    /**
     * Получение списка промежуточных рпезультатов по списку
     * идентификаторов в том же порядке.
     */
    private List<ResultType> getInnerResults(List<PointType> points) {
        return points.stream()
                .map(point -> Optional.ofNullable(this.getForPoint(point)))
                .map(nullableHolder -> nullableHolder.map(holder -> holder.data))
                .map(nullableData -> nullableData.orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Класс для хранения промежуточных результатов.
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
