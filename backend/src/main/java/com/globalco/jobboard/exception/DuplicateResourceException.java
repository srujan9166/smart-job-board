package com.globalco.jobboard.exception;

/**
 * Exception thrown when a resource creation fails due to duplication constraints.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
