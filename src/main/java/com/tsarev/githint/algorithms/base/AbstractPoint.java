package com.tsarev.githint.algorithms.base;

/**
 * Intermediate result identification.
 */
public interface AbstractPoint<PointType extends AbstractPoint> {

    /**
     * Get shifted index.
     *
     * @param point how much to shift
     */
    PointType shiftThis(PointType point);
}
