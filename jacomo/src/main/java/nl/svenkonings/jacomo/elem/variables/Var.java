package nl.svenkonings.jacomo.elem.variables;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.Expr;
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
