package nl.svenkonings.jacomo.exceptions.unchecked;

import nl.svenkonings.jacomo.visitor.Checker;

/**
 * Unchecked exception thrown when the check of a {@link Checker} fails.
 */
public class CheckException extends JaCoMoRuntimeException {
    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Object... args) {
        super(message, args);
    }

    public CheckException(Throwable cause, String message) {
        super(cause, message);
    }

    public CheckException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
