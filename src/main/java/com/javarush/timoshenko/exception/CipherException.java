package com.javarush.timoshenko.exception;

/**
 * Базовое исключение для приложения шифра Цезаря
 */
public class CipherException extends Exception {

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }
}
