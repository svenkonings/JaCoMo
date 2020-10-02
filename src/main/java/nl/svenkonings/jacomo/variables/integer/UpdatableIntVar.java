package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.exceptions.ContradictionException;
import org.jetbrains.annotations.NotNull;

public interface UpdatableIntVar extends IntVar {
    @Override
    default @NotNull Type getType() {
        return Type.UpdatableIntVar;
    }

    void instantiateValue(int value) throws ContradictionException;

    void updateLowerBound(int lowerBound) throws ContradictionException;

    void updateUpperBound(int upperBound) throws ContradictionException;

    default void updateBounds(int lowerBound, int upperBound) throws ContradictionException {
        updateLowerBound(lowerBound);
        updateUpperBound(upperBound);
    }
}
