package com.clap.exceptions;

public class DuplicateActionException extends Exception {
    public DuplicateActionException(String errorMessage) {
        super(errorMessage);
    }
}
