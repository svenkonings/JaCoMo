package nl.svenkonings.jacomo.expressions.integer.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an Minimum expression.
 */
@SuppressWarnings("ConstantConditions")
public class MinExpr implements BiIntExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Minimum integer expression.
     *
     * @param left  the left-hand side of the Minimum expression
     * @param right the right-hand side of the Minimum expression
     */
    public MinExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.MinExpr;
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
    public @Nullable Integer getValue() {
        if (hasValue()) {
            return Math.min(left.getValue(), right.getValue());
        } else {
            return null;
        }
    }

    @Override
    public boolean hasLowerBound() {
        return left.hasLowerBound() && right.hasLowerBound();
    }

    @Override
    public @Nullable Integer getLowerBound() {
        if (hasLowerBound()) {
            return Math.min(left.getLowerBound(), right.getLowerBound());
        } else {
            return null;
        }
    }

    @Override
    public boolean hasUpperBound() {
        return left.hasUpperBound() && right.hasUpperBound();
    }

    @Override
    public @Nullable Integer getUpperBound() {
        if (hasUpperBound()) {
            return Math.min(left.getUpperBound(), right.getUpperBound());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "min(" + left + ", " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinExpr minExpr = (MinExpr) o;
        return Objects.equals(left, minExpr.left) &&
                Objects.equals(right, minExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
