package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.EntryData;

/**
 * Most common aggregators enumeration.
 */
public enum CommonStatTypes {

    MAX_NEW_LINES(NumberData.class),

    MAX_OLD_LINES(NumberData.class),

    MAX_AVG_OLD_NEW_LINES_DIFF(NumberData.class);

    /**
     * Тип данных собираемой статистики.
     */
    private final Class<? extends EntryData> dataType;

    CommonStatTypes(Class<? extends EntryData> dataType) {
        this.dataType = dataType;
    }

    public Class<? extends EntryData> getDataType() {
        return dataType;
    }
}
