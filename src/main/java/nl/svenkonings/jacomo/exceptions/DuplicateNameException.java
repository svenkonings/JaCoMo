package nl.svenkonings.jacomo.exceptions;

public class DuplicateNameException extends JaCoMoException {
    public DuplicateNameException(String message) {
        super(message);
    }

    public DuplicateNameException(String message, Object... args) {
        super(message, args);
    }
}
