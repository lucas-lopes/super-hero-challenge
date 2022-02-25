package com.dataguard.superherochallenge.controller.exception;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StandardError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer status;
    private final String message;
    private final String error;
    private final Long timestamp;

}
