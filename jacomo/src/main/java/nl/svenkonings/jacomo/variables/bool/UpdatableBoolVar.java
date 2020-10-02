package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.exceptions.ContradictionException;
import org.jetbrains.annotations.NotNull;

public interface UpdatableBoolVar extends BoolVar {
    @Override
    default @NotNull Type getType() {
        return Type.UpdatableBoolVar;
    }

    void instantiateValue(boolean value) throws ContradictionException;
}
