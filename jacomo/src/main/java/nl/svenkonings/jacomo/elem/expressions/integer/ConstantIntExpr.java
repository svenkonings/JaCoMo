package nl.svenkonings.jacomo.elem.expressions.integer;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a constant integer expression.
 */
public class ConstantIntExpr implements IntExpr {

    private final int value;

    /**
     * Create a new constant integer expression with the specified value.
     *
     * @param value the specified value
     */
    public ConstantIntExpr(int value) {
        this.value = value;
    }

    @Override
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.ConstantIntExpr;
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
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantIntExpr that = (ConstantIntExpr) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
