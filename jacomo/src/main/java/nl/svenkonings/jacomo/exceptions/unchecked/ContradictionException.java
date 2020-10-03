package nl.svenkonings.jacomo.exceptions.unchecked;

/**
 * Unchecked exception thrown when a variable update caused a contradiction.
 */
public class ContradictionException extends JaCoMoRuntimeException {
    public ContradictionException(String message) {
        super(message);
    }

    public ContradictionException(String message, Object... args) {
        super(message, args);
    }

    public ContradictionException(Throwable cause, String message) {
        super(cause, message);
    }

    public ContradictionException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
