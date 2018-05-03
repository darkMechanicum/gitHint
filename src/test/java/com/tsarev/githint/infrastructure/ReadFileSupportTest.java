package com.tsarev.githint.infrastructure;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Тест с поддержкой чтения файлов, указанных в аннотации {@link LoadFiles}.
 * Каждый тестовый метод должен быть помечен этой аннотацией.
 */
public abstract class ReadFileSupportTest {

    @Rule
    public AtomicTestAnnotationWatcher annotationWatcher = new AtomicTestAnnotationWatcher();

    @Rule
    public TestName testNameWatcher = new TestName();

    /**
     * Содержимое первого файла.
     */
    private String firstContent;

    /**
     * Содержимое второго файла.
     */
    private String secondContent;

    @Before
    public void prepareData() {
        Collection<Annotation> annotations = annotationWatcher.getMethodAnnotations();

        if (annotations.isEmpty()) {
            Assert.fail("В этом пакете тестов каждый тест должен быть помечен аннотацией.\n" +
                            "Метод: " + testNameWatcher.getMethodName());
        }

        Collection<LoadFiles> loadFilesAnnotations = annotations.stream()
                .filter(LoadFiles.class::isInstance)
                .map(LoadFiles.class::cast)
                .collect(Collectors.toList());

        if (loadFilesAnnotations.isEmpty() || loadFilesAnnotations.size() != 1) {
            Assert.fail("В этом пакете тестов каждый тест должен быть помечен аннотацией LoadFiles.\n" +
                            "Метод: " + testNameWatcher.getMethodName());
        }

        LoadFiles loadFilesAnnotation = loadFilesAnnotations.iterator().next();
        String firstFileName = loadFilesAnnotation.firstFile();
        String secondFileName = loadFilesAnnotation.secondFile();

        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        URL firstUrl = thisClassLoader.getResource(firstFileName);
        URL secondUrl = thisClassLoader.getResource(secondFileName);

        if (firstUrl == null || secondUrl == null) {
            Assert.fail("Не найден один из следующих ресурсов:\n" +
                    "Первый: " + firstFileName + "\n" +
                    "Второй: " + secondFileName);
        }

        try {
            firstContent = readFileContents(firstUrl);
            secondContent = readFileContents(secondUrl);
        } catch (Exception any) {
            // Очистка контекста, ксли что-то пошло не так.
            firstContent = null;
            secondContent = null;
            throw any;
        }
    }

    /**
     * Чтение содержимого файла.
     */
    private String readFileContents(URL fileUrl) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileUrl.toURI()));
            return new String(encoded, "UTF-8");
        } catch (IOException | URISyntaxException cause) {
            cause.printStackTrace();
            Assert.fail("Возникла ошибка во время чтения файла.\n" +
                    "Файл: " + fileUrl);
        }
        // Сюда приходить не должны, так как Assert.fail
        // генерирует исключение.
        return null;
    }

    protected String getFirstContent() {
        return firstContent;
    }

    protected String getSecondContent() {
        return secondContent;
    }
}
