package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.ConstantIntVar;
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
