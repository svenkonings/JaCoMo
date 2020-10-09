package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represent an And boolean expression.
 */
@SuppressWarnings("ConstantConditions")
public class AndExpr implements BiBoolExpr {
    private final @NotNull BoolExpr left;
    private final @NotNull BoolExpr right;

    /**
     * Create a new And boolean expression.
     *
     * @param left  the left-hand side of the And expression
     * @param right the right-hand side of the And expression
     */
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
        return (left.hasValue() && right.hasValue()) ||
                (left.hasValue() && !left.getValue()) ||
                (right.hasValue() && !right.getValue());
    }

    @Override
    public @Nullable Boolean getValue() {
        if (left.hasValue() && right.hasValue()) {
            return left.getValue() && right.getValue();
        } else if (left.hasValue() && !left.getValue()) {
            return false;
        } else if (right.hasValue() && !right.getValue()) {
            return false;
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
