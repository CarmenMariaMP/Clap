package com.clap.exceptions;

public class NotAuthorizedExcepcion extends Exception {
    public NotAuthorizedExcepcion(String errorMessage) {
        super(errorMessage);
    }
}
