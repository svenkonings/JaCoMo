package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.exceptions.ContradictionException;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.InstantiatableBoolVar;
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
    public void instantiateValue(boolean value) throws ContradictionException {
        if (this.value != null && !Objects.equals(this.value, value)) {
            throw new ContradictionException("Value already instantiated");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return boolVarString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstantiatableBoolVar that = (InstantiatableBoolVar) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
