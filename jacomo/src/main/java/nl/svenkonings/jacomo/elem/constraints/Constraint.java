package nl.svenkonings.jacomo.elem.constraints;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a constraint.
 */
public interface Constraint extends Elem {
    @Override
    default @NotNull Type getType() {
        return Type.Constraint;
    }
}
