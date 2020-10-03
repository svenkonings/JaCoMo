package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Unchecked exception thrown when an unknown type is encountered.
 */
public class UnknownTypeException extends JaCoMoRuntimeException {
    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(String message, Object... args) {
        super(message, args);
    }

    public UnknownTypeException(Throwable cause, String message) {
        super(cause, message);
    }

    public UnknownTypeException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
