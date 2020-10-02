package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class OrExpr implements BiBoolExpr {
    private final @NotNull BoolExpr left;
    private final @NotNull BoolExpr right;

    public OrExpr(@NotNull BoolExpr left, @NotNull BoolExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.OrExpr;
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
                (left.hasValue() && left.getValue()) ||
                (right.hasValue() && right.getValue());
    }

    @Override
    public @Nullable Boolean getValue() {
        if (left.hasValue() && right.hasValue()) {
            return left.getValue() || right.getValue();
        } else if (left.hasValue() && left.getValue()) {
            return true;
        } else if (right.hasValue() && right.getValue()) {
            return true;
        } else {
            return null;
        }
    }
}
