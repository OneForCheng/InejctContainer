package com.thoughtworks.InjectContainer.exception;

public class InjectException extends RunnerBaseException {
    public InjectException(String message, Throwable e) {
        super(message, e);
    }

    public InjectException(String message) {
        super(message);
    }
}
