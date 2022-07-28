/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.integer;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a bounded integer variable. The variable has an optional lower and upper bound.
 * If both bounds are equal, the variable represents a single value.
 * Boundaries can only be tightened and never loosened.
 */
public class BoundedIntVar implements UpdatableIntVar {

    private final @NotNull String name;

    private @Nullable Integer lowerBound;

    private @Nullable Integer upperBound;

    /**
     * Create a new integer variable with the specified name.
     * The bounds are left undefined.
     *
     * @param name the specified name
     */
    public BoundedIntVar(@NotNull String name) {
        this.name = name;
        this.lowerBound = null;
        this.upperBound = null;
    }

    /**
     * Create a new integer variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     */
    public BoundedIntVar(@NotNull String name, @Nullable Integer value) {
        this.name = name;
        this.lowerBound = value;
        this.upperBound = value;
    }

    /**
     * Create a new integer variable with the specified name, lower- and upper-bound.
     *
     * @param name       the specified name
     * @param lowerBound the specified lower-bound
     * @param upperBound the specified upper-bound
     * @throws ContradictionException if the lower bound is higher than the upper bound
     */
    public BoundedIntVar(@NotNull String name, @Nullable Integer lowerBound, @Nullable Integer upperBound) throws ContradictionException {
        this.name = name;
        checkBounds(lowerBound, upperBound);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasValue() {
        return lowerBound != null && lowerBound.equals(upperBound);
    }

    @Override
    public @Nullable Integer getValue() {
        if (hasValue()) {
            return lowerBound;
        } else {
            return null;
        }
    }

    @Override
    public void instantiateValue(int value) throws ContradictionException {
        checkValue(value);
        lowerBound = value;
        upperBound = value;
    }

    @Override
    public boolean hasLowerBound() {
        return lowerBound != null;
    }

    @Override
    public @Nullable Integer getLowerBound() {
        return lowerBound;
    }

    @Override
    public void updateLowerBound(int lowerBound) throws ContradictionException {
        checkLowerBound(lowerBound);
        this.lowerBound = lowerBound;
    }

    @Override
    public boolean hasUpperBound() {
        return upperBound != null;
    }

    @Override
    public @Nullable Integer getUpperBound() {
        return upperBound;
    }

    @Override
    public void updateUpperBound(int upperBound) throws ContradictionException {
        checkUpperBound(upperBound);
        this.upperBound = upperBound;
    }

    @Override
    public void updateBounds(int lowerBound, int upperBound) throws ContradictionException {
        checkBounds(lowerBound, upperBound);
        checkLowerBound(lowerBound);
        checkUpperBound(upperBound);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private void checkValue(int value) throws ContradictionException {
        if (lowerBound != null && value < lowerBound) {
            throw new ContradictionException("New value %d is lower than lower bound %d", value, lowerBound);
        }
        if (upperBound != null && value > upperBound) {
            throw new ContradictionException("New value %d is higher than upper bound %d", value, upperBound);
        }
    }

    private void checkBounds(Integer lowerBound, Integer upperBound) throws ContradictionException {
        if (lowerBound != null && upperBound != null && lowerBound > upperBound) {
            throw new ContradictionException("Lower bound %d is higher than upper bound %d", lowerBound, upperBound);
        }
    }

    private void checkLowerBound(int lowerBound) throws ContradictionException {
        if (this.lowerBound != null && lowerBound < this.lowerBound) {
            throw new ContradictionException("New lower bound %d is lower than current lower bound %d", lowerBound, this.lowerBound);
        }
        if (this.upperBound != null && lowerBound > this.upperBound) {
            throw new ContradictionException("New lower bound %d is higher than current upper bound %d", lowerBound, this.upperBound);
        }
    }

    private void checkUpperBound(int upperBound) throws ContradictionException {
        if (this.upperBound != null && upperBound > this.upperBound) {
            throw new ContradictionException("New upper bound %d is higher than current upper bound %d", upperBound, this.upperBound);
        }
        if (this.lowerBound != null && upperBound < this.lowerBound) {
            throw new ContradictionException("New upper bound %d is lower than current lower bound %d", upperBound, this.lowerBound);
        }
    }

    @Override
    public String toString() {
        return intVarString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundedIntVar that = (BoundedIntVar) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(lowerBound, that.lowerBound) &&
                Objects.equals(upperBound, that.upperBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash("BoundedIntVar", name, lowerBound, upperBound);
    }
}
