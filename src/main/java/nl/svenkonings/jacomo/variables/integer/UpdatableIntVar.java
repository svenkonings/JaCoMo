package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.exceptions.ContradictionException;

public interface UpdatableIntVar extends IntVar {
    void updateValue(int value) throws ContradictionException;

    void updateLowerBound(int lowerBound) throws ContradictionException;

    void updateUpperBound(int upperBound) throws ContradictionException;

    default void updateBounds(int lowerBound, int upperBound) throws ContradictionException {
        updateLowerBound(lowerBound);
        updateUpperBound(upperBound);
    }
}
