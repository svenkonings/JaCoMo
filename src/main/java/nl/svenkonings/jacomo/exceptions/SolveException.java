package nl.svenkonings.jacomo.exceptions;

public class SolveException extends JaCoMoException {
    public SolveException(String message) {
        super(message);
    }

    public SolveException(String message, Object... args) {
        super(message, args);
    }
}
