package nl.svenkonings.jacomo.expressions.bool;

import nl.svenkonings.jacomo.Elem;
import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ConstantBoolExpr implements BoolExpr {

    private final boolean value;

    public ConstantBoolExpr(boolean value) {
        this.value = value;
    }

    @Override
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.ConstantBoolExpr;
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
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantBoolExpr that = (ConstantBoolExpr) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
