package dev.rutana.resumematcher.exceptions;

/**
 * A top-level custom exception for the ResumeMatcher project.
 */
public class ResumeMatcherException extends RuntimeException {

    /**
     * Constructs a new ResumeMatcherException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ResumeMatcherException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResumeMatcherException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public ResumeMatcherException(String message, Throwable cause) {
        super(message, cause);
    }
}
