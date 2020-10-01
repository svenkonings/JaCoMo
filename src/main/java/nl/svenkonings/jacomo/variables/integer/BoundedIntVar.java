package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.exceptions.ContradictionException;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a integer variable. The variable has an optional lower and upper bound.
 * If both bounds are equal, the variable represents a single value.
 * Boundaries can only be tightened and never loosened.
 */
public class BoundedIntVar implements UpdatableIntVar {

    private final @NotNull String name;

    private @Nullable Integer lowerBound;

    private @Nullable Integer upperBound;

    public BoundedIntVar(@NotNull String name) {
        this.name = name;
        this.lowerBound = null;
        this.upperBound = null;
    }

    public BoundedIntVar(@NotNull String name, @Nullable Integer value) {
        this.name = name;
        this.lowerBound = value;
        this.upperBound = value;
    }

    public BoundedIntVar(@NotNull String name, @Nullable Integer lowerBound, @Nullable Integer upperBound) throws ContradictionException {
        this.name = name;
        if (lowerBound != null && upperBound != null && lowerBound > upperBound) {
            throw new ContradictionException("Lower bound %d is higher than upper bound %d", lowerBound, upperBound);
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasExpression() {
        return false;
    }

    @Override
    public @Nullable IntExpr getExpression() {
        return null;
    }

    @Override
    public boolean hasValue() {
        return Objects.equals(lowerBound, upperBound);
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
    public void updateValue(int value) throws ContradictionException {
        if (lowerBound != null && value < lowerBound) {
            throw new ContradictionException("New value %d is lower than lower bound %d", value, lowerBound);
        }
        if (upperBound != null && value > upperBound) {
            throw new ContradictionException("New value %d is higher than upper bound %d", value, upperBound);
        }
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
        if (this.lowerBound != null && lowerBound < this.lowerBound) {
            throw new ContradictionException("New lower bound %d is lower than current lower bound %d", lowerBound, this.lowerBound);
        }
        if (this.upperBound != null && lowerBound > this.upperBound) {
            throw new ContradictionException("New lower bound %d is higher than current upper bound %d", lowerBound, this.upperBound);
        }
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
        if (this.upperBound != null && upperBound > this.upperBound) {
            throw new ContradictionException("New upper bound %d is higher than current upper bound %d", upperBound, this.upperBound);
        }
        if (this.lowerBound != null && upperBound < this.lowerBound) {
            throw new ContradictionException("New upper bound %d is lower than current lower bound %d", upperBound, this.lowerBound);
        }
        this.upperBound = upperBound;
    }

    @Override
    public String toString() {
        return intVarString();
    }
}
