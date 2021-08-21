package com.thoughtworks.InjectContainer.exception;

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
