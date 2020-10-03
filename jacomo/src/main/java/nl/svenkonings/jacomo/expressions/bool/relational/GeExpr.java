package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a Greater-or-equals expression.
 */
@SuppressWarnings("ConstantConditions")
public class GeExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Greater-or-equals boolean expression.
     *
     * @param left  the left-hand side of the Greater-or-equals expression
     * @param right the right-hand side of the Greater-or-equals expression
     */
    public GeExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.GeExpr;
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
        return left.hasLowerBound() && right.hasUpperBound();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (hasValue()) {
            return left.getLowerBound() >= right.getUpperBound();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " >= " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeExpr geExpr = (GeExpr) o;
        return Objects.equals(left, geExpr.left) &&
                Objects.equals(right, geExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
