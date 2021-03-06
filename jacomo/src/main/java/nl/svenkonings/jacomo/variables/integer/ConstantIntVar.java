package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a constant integer variable.
 */
public class ConstantIntVar implements IntVar {

    private final @NotNull String name;
    private final int value;

    /**
     * Create a new constant integer variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     */
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
    public @NotNull Integer getValue() {
        return value;
    }

    @Override
    public boolean hasLowerBound() {
        return true;
    }

    @Override
    public @NotNull Integer getLowerBound() {
        return value;
    }

    @Override
    public boolean hasUpperBound() {
        return true;
    }

    @Override
    public @NotNull Integer getUpperBound() {
        return value;
    }

    @Override
    public String toString() {
        return intVarString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantIntVar that = (ConstantIntVar) o;
        return value == that.value &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
