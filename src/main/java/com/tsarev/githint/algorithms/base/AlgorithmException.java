package com.tsarev.githint.algorithms.base;

/**
 * Исключение, возникающее во время работы алгоритма.
 */
public class AlgorithmException extends Exception {
    public AlgorithmException(String message) {
        super(message);
    }
}
