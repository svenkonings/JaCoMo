package nl.svenkonings.jacomo.exceptions.checked;

/**
 * Checked exception thrown when a model could not be solved.
 */
public class SolveException extends JaCoMoException {
    public SolveException(String message) {
        super(message);
    }

    public SolveException(String message, Object... args) {
        super(message, args);
    }

    public SolveException(Throwable cause, String message) {
        super(cause, message);
    }

    public SolveException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
