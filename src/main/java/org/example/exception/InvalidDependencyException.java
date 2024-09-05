package org.example.exception;

public class InvalidDependencyException extends RuntimeException {

    public InvalidDependencyException(final String message) {
        super(message);
    }
}
