package com.dataguard.superherochallenge.service.exception;

import java.io.Serializable;

public class BadRequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
