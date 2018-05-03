package com.tsarev.githint.algorithms.base;

import java.util.List;
import java.util.function.Function;

/**
 * Базовый предикат применимости функции слияния.
 */
public interface BaseUnionable<ResultType, PointType extends AbstractPoint> {

    /**
     * Относительные координаты элементов массива,
     * которые нужны для вычисления предиката.
     */
    List<PointType> neededPoints();

    /**
     * Проверка, можно ли выполнить слияние
     * предыдущих результатов.
     *
     * @param currentPoint текущий идентификатор
     * @param points абсолютные координаты элементов массива
     * @param violateChecker функция проверки доступности идентификатора
     */
    boolean canUnion(PointType currentPoint,
                     List<PointType> points,
                     Function<PointType, Boolean> violateChecker);

    /**
     * Слияние предыдущих результатов.
     */
    ResultType unionPrevious(PointType currentPoint,
                             List<ResultType> previous);

    /**
     * Получение идентификатора для разделения шагов.
     */
    long getId();
}
