package nl.svenkonings.jacomo.constraints;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
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
