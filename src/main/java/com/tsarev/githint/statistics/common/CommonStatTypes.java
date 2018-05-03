package com.tsarev.githint.statistics.common;

/**
 * Перечисление наиболее частых типов статистики.
 */
public enum CommonStatTypes {

    MAX_NEW_LINES(Long.class),

    MAX_OLD_LINES(Long.class),

    MAX_AVG_OLD_NEW_LINES_DIFF(Long.class);

    /**
     * Тип данных собираемой статистики.
     */
    private final Class<?> dataType;

    CommonStatTypes(Class<?> dataType) {
        this.dataType = dataType;
    }

    public Class<?> getDataType() {
        return dataType;
    }
}
