package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstantIntVar implements IntVar {

    private final @NotNull String name;
    private final int value;

    public ConstantIntVar(@NotNull String name, int value) {
        this.name = name;
        this.value = value;
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
        return true;
    }

    @Override
    public @Nullable Integer getValue() {
        return value;
    }

    @Override
    public boolean hasLowerBound() {
        return true;
    }

    @Override
    public @Nullable Integer getLowerBound() {
        return value;
    }

    @Override
    public boolean hasUpperBound() {
        return true;
    }

    @Override
    public @Nullable Integer getUpperBound() {
        return value;
    }

    @Override
    public String toString() {
        return intVarString();
    }
}
