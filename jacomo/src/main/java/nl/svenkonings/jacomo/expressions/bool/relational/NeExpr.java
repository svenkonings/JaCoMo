package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a Not-equals expression.
 */
public class NeExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Not-equals boolean expression.
     *
     * @param left  the left-hand side of the Not-equals expression
     * @param right the right-hand side of the Not-equals expression
     */
    public NeExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.NeExpr;
    }

    @Override
    public @NotNull IntExpr getLeft() {
        return left;
    }

    @Override
    public @NotNull IntExpr getRight() {
        return right;
    }

    @Override
    public boolean hasValue() {
        return left.hasValue() && right.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (hasValue()) {
            return !Objects.equals(left.getValue(), right.getValue());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " != " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeExpr neExpr = (NeExpr) o;
        return Objects.equals(left, neExpr.left) &&
                Objects.equals(right, neExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
