package nl.svenkonings.jacomo.exceptions.checked;

/**
 * Parent exception of all checked JaCoMo exceptions.
 */
public class JaCoMoException extends Exception {
    protected JaCoMoException(String message) {
        super(message);
    }

    protected JaCoMoException(String message, Object... args) {
        this(String.format(message, args));
    }

    protected JaCoMoException(Throwable cause, String message) {
        super(message, cause);
    }

    protected JaCoMoException(Throwable cause, String message, Object... args) {
        this(String.format(message, args), cause);
    }
}
