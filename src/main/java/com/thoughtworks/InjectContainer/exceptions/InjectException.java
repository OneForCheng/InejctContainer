package com.thoughtworks.InjectContainer.exceptions;

public class InjectException extends RunnerBaseException {
    public InjectException(String message, Throwable e) {
        super(message, e);
    }

    public InjectException(String message) {
        super(message);
    }
}
