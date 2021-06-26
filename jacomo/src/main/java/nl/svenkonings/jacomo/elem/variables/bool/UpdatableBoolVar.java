package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a boolean variable which can be updated.
 */
public interface UpdatableBoolVar extends BoolVar {
    @Override
    default @NotNull Type getType() {
        return Type.UpdatableBoolVar;
    }

    /**
     * Instantiate this boolean variable with the specified value
     *
     * @param value the specified value
     * @throws ContradictionException if this boolean variable has already been instantiated
     */
    void instantiateValue(boolean value) throws ContradictionException;
}
