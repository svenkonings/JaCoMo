package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a named boolean variable.
 */
public interface BoolVar extends Var, BoolExpr {
    @Override
    default @NotNull Type getType() {
        return Type.BoolVar;
    }

    /**
     * Returns a string representation of this variable.
     *
     * @return a string representation of this variable
     */
    default String boolVarString() {
        String name = getName();
        if (hasValue()) {
            return String.format("bool %s = %b", name, getValue());
        } else {
            return String.format("bool %s", name);
        }
    }
}
