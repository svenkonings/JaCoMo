package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a expression.
 */
public interface Expr extends Elem {
    @Override
    default @NotNull Type getType() {
        return Type.Expr;
    }

    /**
     * Returns whether this expression has a (instantiated) value.
     *
     * @return {@code true} if this expression has a value
     */
    boolean hasValue();
}
