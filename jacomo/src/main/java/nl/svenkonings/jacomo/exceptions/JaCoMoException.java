package nl.svenkonings.jacomo.exceptions;

public class JaCoMoException extends RuntimeException {
    protected JaCoMoException(String message) {
        super(message);
    }

    protected JaCoMoException(String message, Object... args) {
        this(String.format(message, args));
    }
}
