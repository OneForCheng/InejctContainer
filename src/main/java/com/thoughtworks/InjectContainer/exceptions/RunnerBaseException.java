package com.thoughtworks.InjectContainer.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RunnerBaseException extends RuntimeException {
    private final String message;

    public RunnerBaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public RunnerBaseException(String message) {
        super(message);
        this.message = message;
    }
}
