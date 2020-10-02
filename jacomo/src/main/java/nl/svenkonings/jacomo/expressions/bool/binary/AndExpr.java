package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class AndExpr implements BiBoolExpr {
    private final @NotNull BoolExpr left;
    private final @NotNull BoolExpr right;

    public AndExpr(@NotNull BoolExpr left, @NotNull BoolExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.AndExpr;
    }

    @Override
    public @NotNull BoolExpr getLeft() {
        return left;
    }

    @Override
    public @NotNull BoolExpr getRight() {
        return right;
    }

    @Override
    public boolean hasValue() {
        return left.hasValue() && right.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (hasValue()) {
            return left.getValue() && right.getValue();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " && " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndExpr andExpr = (AndExpr) o;
        return Objects.equals(left, andExpr.left) &&
                Objects.equals(right, andExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
