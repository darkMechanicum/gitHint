package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.EntryData;

import java.util.Objects;

/**
 * Simple data holder.
 */
public class NumberData implements EntryData {

    /**
     * Inner number.
     */
    private final Number data;

    /**
     * Constructor.
     */
    public NumberData(Number data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberData that = (NumberData) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
