package nl.svenkonings.jacomo.exceptions;

public class UnexpectedTypeException extends JaCoMoException {
    public UnexpectedTypeException() {
        super("Unexpected expression type");
    }
}
