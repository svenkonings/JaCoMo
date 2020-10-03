package nl.svenkonings.jacomo.expressions.bool.unary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represent a unary boolean expression applied to a boolean sub-expression.
 */
public interface UnBoolExpr extends BoolExpr {
    @Override
    default @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(getExpr());
    }

    @Override
    default @NotNull Type getType() {
        return Type.UnBoolExpr;
    }

    /**
     * Returns the boolean sub-expression.
     *
     * @return the boolean sub-expression
     */
    @NotNull BoolExpr getExpr();
}
