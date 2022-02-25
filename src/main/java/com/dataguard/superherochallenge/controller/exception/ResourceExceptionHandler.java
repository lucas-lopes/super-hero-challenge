package com.dataguard.superherochallenge.controller.exception;

import com.dataguard.superherochallenge.service.exception.BadRequestException;
import com.dataguard.superherochallenge.service.exception.ConflictException;
import com.dataguard.superherochallenge.service.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> throwObjectNotFoundException(ObjectNotFoundException e) {
        StandardError err = buildStandardError(HttpStatus.NOT_FOUND, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> throwConflictException(ConflictException e) {
        StandardError err = buildStandardError(HttpStatus.CONFLICT, e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> throwBadRequestException(BadRequestException e) {
        StandardError err = buildStandardError(HttpStatus.BAD_REQUEST, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    private StandardError buildStandardError(HttpStatus httpStatus, RuntimeException e) {
        return StandardError.builder()
            .status(httpStatus.value())
            .message(e.getMessage())
            .error(httpStatus.name())
            .timestamp(System.currentTimeMillis())
            .build();
    }

}
