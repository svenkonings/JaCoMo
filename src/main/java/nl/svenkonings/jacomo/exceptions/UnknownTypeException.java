package nl.svenkonings.jacomo.exceptions;

public class UnknownTypeException extends JaCoMoException {
    public UnknownTypeException(String message) {
        super(message);
    }

    public UnknownTypeException(String message, Object... args) {
        super(message, args);
    }
}
