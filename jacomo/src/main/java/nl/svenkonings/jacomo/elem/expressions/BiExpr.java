package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a binary expression with two sub-expressions.
 */
public interface BiExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.BiExpr;
    }

    /**
     * Returns the left-hand expression.
     *
     * @return the left-hand expression
     */
    @NotNull Expr getLeft();

    /**
     * Returns the right-hand expression.
     *
     * @return the right-hand expression
     */
    @NotNull Expr getRight();
}
