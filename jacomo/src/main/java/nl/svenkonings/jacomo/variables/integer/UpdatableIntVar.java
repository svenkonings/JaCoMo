package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an updatable integer variable.
 */
public interface UpdatableIntVar extends IntVar {
    @Override
    default @NotNull Type getType() {
        return Type.UpdatableIntVar;
    }

    /**
     * Instantiate this integer variable with the specified value.
     *
     * @param value the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void instantiateValue(int value) throws ContradictionException;

    /**
     * Update the lower-bound of this variable with the specified value.
     *
     * @param lowerBound the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void updateLowerBound(int lowerBound) throws ContradictionException;

    /**
     * Update the upper-bound of this variable with the specified value.
     *
     * @param upperBound the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void updateUpperBound(int upperBound) throws ContradictionException;

    /**
     * Update both bounds of this variable with the specified values.
     *
     * @param lowerBound the specified lower-bound value
     * @param upperBound the specified upper-bound value
     * @throws ContradictionException if one of the specified value is
     *                                outside the bounds of this variable
     */
    default void updateBounds(int lowerBound, int upperBound) throws ContradictionException {
        updateLowerBound(lowerBound);
        updateUpperBound(upperBound);
    }
}
