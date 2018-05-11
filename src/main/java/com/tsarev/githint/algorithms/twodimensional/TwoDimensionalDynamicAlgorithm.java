package com.tsarev.githint.algorithms.twodimensional;

import com.tsarev.githint.algorithms.base.BaseDynamicAlgorithm;
import com.tsarev.githint.algorithms.base.BaseUnionable;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Two dimensional specialization for {@link BaseDynamicAlgorithm}.
 */
public class TwoDimensionalDynamicAlgorithm<ResultType>
        extends BaseDynamicAlgorithm<ResultType, TwoDimensionalPoint> {

    private final Object[][] data;

    private final int xSize;

    private final int ySize;

    /**
     * Конструктор.
     */
    public TwoDimensionalDynamicAlgorithm(Collection<BaseUnionable<ResultType, TwoDimensionalPoint>> baseUnionables,
                                          int xSize,
                                          int ySize) {
        super(baseUnionables);
        this.data = new Object[xSize][ySize];
        this.xSize = xSize;
        this.ySize = ySize;
    }

    /**
     * Конструктор.
     */
    @SafeVarargs
    public TwoDimensionalDynamicAlgorithm(int xSize,
                                          int ySize,
                                          BaseUnionable<ResultType, TwoDimensionalPoint>... baseUnionables) {
        this(Arrays.asList(baseUnionables), xSize, ySize);
    }

    @Override
    public ResultHolder getForPoint(TwoDimensionalPoint point) {
        if (violatesBorders(point)) {
            return null;
        } else {
            return (ResultHolder) data[point.x][point.y];
        }
    }

    @Override
    protected boolean violatesBorders(TwoDimensionalPoint point) {
        return point.x >= xSize || point.x < 0 || point.y >= ySize || point.y < 0;
    }

    @Override
    protected void setForPoint(TwoDimensionalPoint point, ResultHolder holder) {
        data[point.x][point.y] = holder;
    }

    @Override
    protected Iterable<TwoDimensionalPoint> getPointIterator() {
        return TwoDimensionalIterator::new;
    }

    /**
     * Простой итератор, перебирающий все значения по двумерному массиву.
     */
    // TODO Add incrementation policy declaration.
    private class TwoDimensionalIterator implements Iterator<TwoDimensionalPoint> {

        private int currentX = 0;

        private int currentY = 0;

        @Override
        public boolean hasNext() {
            return currentY < ySize && currentX < xSize;
        }

        @Override
        public TwoDimensionalPoint next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            TwoDimensionalPoint currentPoint = new TwoDimensionalPoint(currentX, currentY);
            if (currentX >= xSize - 1) {
                currentX = 0;
                currentY++;
            } else {
                currentX++;
            }
            return currentPoint;
        }
    }

    @Deprecated
    public void printArray(PrintStream stream,
                           Function<Integer, String> rows,
                           Function<Integer, String> columns,
                           String postfix) {
        stream.println("Contents of array:");
        stream.print("  ");
        for (int j = 0; j < data[0].length; j++) {
            stream.print(columns.apply(j) + " ");
        }
        stream.println();
        for (int i = 0; i < data.length; i++) {
            stream.print(rows.apply(i) + " ");
            for (int j = 0; j < data[i].length; j++) {
                stream.print(((ResultHolder) data[i][j]).data + postfix + " ");
            }
            stream.println();
        }
        stream.println();
    }
}
