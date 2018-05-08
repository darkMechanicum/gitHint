package com.tsarev.githint.algorithms.base;

public abstract class IndentifiableUnionable<ResultType, PointType extends AbstractPoint<PointType>>
        implements BaseUnionable<ResultType, PointType> {

    /**
     * Base identifier for abstract dynamic algorithm step.
     */
    private final long id;

    protected IndentifiableUnionable(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
