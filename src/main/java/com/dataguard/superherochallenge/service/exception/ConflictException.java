package com.dataguard.superherochallenge.service.exception;

import java.io.Serializable;

public class ConflictException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
