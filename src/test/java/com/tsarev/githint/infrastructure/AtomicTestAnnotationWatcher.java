package com.tsarev.githint.infrastructure;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * Класс для анализа аннотаций над текущим тестом.
 */
public class AtomicTestAnnotationWatcher extends TestWatcher {

    /**
     * Набор аннотаций над телом метода.
     */
    private Collection<Annotation> methodAnnotations;

    @Override
    protected void starting(Description description) {
        if (description.isTest()) {
            methodAnnotations = description.getAnnotations();
        } else {
            clean();
        }
    }

    @Override
    protected void finished(Description description) {
        clean();
    }

    @Override
    protected void failed(Throwable e, Description description) {
        clean();
    }

    /**
     * Очистка данных.
     */
    private void clean() {
        methodAnnotations = Collections.emptyList();
    }

    /**
     * Получение аннотаций.
     */
    public Collection<Annotation> getMethodAnnotations() {
        return methodAnnotations;
    }
}
