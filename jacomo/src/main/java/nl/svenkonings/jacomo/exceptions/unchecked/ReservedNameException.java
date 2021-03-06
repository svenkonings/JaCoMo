package nl.svenkonings.jacomo.exceptions.unchecked;

import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.variables.Var;

/**
 * Unchecked exception thrown when a {@link Model} receives a reserved name.
 */
public class ReservedNameException extends JaCoMoRuntimeException {
    public ReservedNameException(Var var) {
        super("Invalid name: %s. Names starting with underscore are reserved for generated names", var.getName());
    }
}
