package nl.svenkonings.jacomo.exceptions;

public class NameNotFoundException extends JaCoMoException {
    public NameNotFoundException(String message) {
        super(message);
    }

    public NameNotFoundException(String message, Object... args) {
        super(message, args);
    }
}
