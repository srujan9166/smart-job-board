package com.globalco.jobboard.exception;

/**
 * Exception thrown when an action violates business workflow or constraints.
 */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
