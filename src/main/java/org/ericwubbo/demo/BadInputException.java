package org.ericwubbo.demo;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}
