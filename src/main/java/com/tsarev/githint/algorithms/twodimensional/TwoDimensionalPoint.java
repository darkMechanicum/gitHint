package com.tsarev.githint.algorithms.twodimensional;

import com.tsarev.githint.algorithms.base.AbstractPoint;

/**
 * Индекс элемента двумерного массива.
 */
public class TwoDimensionalPoint implements AbstractPoint<TwoDimensionalPoint> {

    public final int x;

    public final int y;

    public TwoDimensionalPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public TwoDimensionalPoint shiftThis(TwoDimensionalPoint twoDimensionalPoint) {
        return new TwoDimensionalPoint(
                this.x + twoDimensionalPoint.x,
                this.y + twoDimensionalPoint.y);
    }
}
