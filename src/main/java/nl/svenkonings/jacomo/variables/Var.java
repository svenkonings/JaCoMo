package nl.svenkonings.jacomo.variables;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import org.jetbrains.annotations.NotNull;

public interface Var extends Elem {
    @NotNull String getName();

    @Override
    default @NotNull Type getType() {
        return Type.Var;
    }
}
