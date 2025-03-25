package ee.taltech.inbankbackend.exception;

/**
 * Thrown when the applicant age is under 18 or over expected life expectancy.
 */
public class InvalidAgeException extends RuntimeException {
    private final String message;
    private final Throwable cause;

    public InvalidAgeException(String message) {
        this(message, null);
    }

    public InvalidAgeException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
