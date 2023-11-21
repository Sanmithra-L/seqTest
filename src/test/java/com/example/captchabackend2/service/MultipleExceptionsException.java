package com.example.captchabackend2.service;

import java.util.List;

public class MultipleExceptionsException extends Exception {
    private List<Exception> exceptions;

    public MultipleExceptionsException(List<Exception> exceptions) {
        super("Multiple exceptions occurred.");
        this.exceptions = exceptions;
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }
}

