package nl.svenkonings.jacomo.exceptions;

public class ContradictionException extends JaCoMoException {
    public ContradictionException(String message) {
        super(message);
    }

    public ContradictionException(String message, Object... args) {
        super(message, args);
    }
}
