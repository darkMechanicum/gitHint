package com.tsarev.githint.infrastructure;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания файлов, которые следует сравнить в тестах.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadFiles {

    /**
     * Имя первого файла.
     */
    String firstFile();

    /**
     * Имя второго файла.
     */
    String secondFile();
}
