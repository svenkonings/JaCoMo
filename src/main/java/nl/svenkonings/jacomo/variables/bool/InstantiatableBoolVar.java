package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.exceptions.ContradictionException;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InstantiatableBoolVar implements UpdatableBoolVar {
    private final @NotNull String name;
    private @Nullable Boolean value;

    public InstantiatableBoolVar(@NotNull String name) {
        this(name, null);
    }

    public InstantiatableBoolVar(@NotNull String name, @Nullable Boolean value) {
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
    public void updateValue(boolean value) throws ContradictionException {
        if (this.value != null && !Objects.equals(this.value, value)) {
            throw new ContradictionException("Value already instantiated");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return boolVarString();
    }
}
