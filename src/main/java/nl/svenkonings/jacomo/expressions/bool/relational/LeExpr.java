package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class LeExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    public LeExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.LeExpr;
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
        return left.hasUpperBound() && right.hasLowerBound();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (hasValue()) {
            return left.getUpperBound() <= right.getLowerBound();
        } else {
            return null;
        }
    }
}
