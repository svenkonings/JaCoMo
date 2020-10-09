package nl.svenkonings.jacomo.expressions;

import nl.svenkonings.jacomo.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a unary expression with a single sub-expressions.
 */
public interface UnExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.UnExpr;
    }

    /**
     * Returns the sub-expression.
     *
     * @return the sub-expression
     */
    @NotNull Expr getExpr();
}
