package nl.svenkonings.jacomo.expressions.bool.unary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Not boolean expression
 */
@SuppressWarnings("ConstantConditions")
public class NotExpr implements UnBoolExpr {
    private final @NotNull BoolExpr expr;

    /**
     * Creates a new Not boolean expression.
     *
     * @param expr the expression to negate
     */
    public NotExpr(@NotNull BoolExpr expr) {
        this.expr = expr;
    }

    @Override
    public @NotNull Type getType() {
        return Type.NotExpr;
    }

    @Override
    public @NotNull BoolExpr getExpr() {
        return expr;
    }

    @Override
    public boolean hasValue() {
        return expr.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (expr.hasValue()) {
            return !expr.getValue();
        } else {
            return null;
        }
    }
}
