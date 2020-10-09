package nl.svenkonings.jacomo.variables;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.Expr;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a named variable.
 */
public interface Var extends Elem, Expr {
    /**
     * Returns the name of this variable.
     *
     * @return the name of this variable
     */
    @NotNull String getName();

    @Override
    default @NotNull Type getType() {
        return Type.Var;
    }
}
