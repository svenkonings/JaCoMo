package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstantBoolVar implements BoolVar {
    private final @NotNull String name;
    private final boolean value;

    public ConstantBoolVar(@NotNull String name, boolean value) {
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
    public @Nullable BoolExpr getExpression() {
        return null;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public @Nullable Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return boolVarString();
    }
}
