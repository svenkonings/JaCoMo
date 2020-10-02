package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.ConstantBoolVar;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantBoolVar that = (ConstantBoolVar) o;
        return value == that.value &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
