package com.tsarev.githint.algorithms.base;

/**
 * Идентификация промежуточного результата.
 */
public interface AbstractPoint<PointType extends AbstractPoint> {

    /**
     * Получение индекса, смещенного на переданный от текущего.
     */
    PointType shiftThis(PointType pointType);
}
