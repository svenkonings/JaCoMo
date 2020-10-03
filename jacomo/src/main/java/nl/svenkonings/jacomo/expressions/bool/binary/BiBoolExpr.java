package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represent a binary boolean expression between two boolean expressions.
 */
public interface BiBoolExpr extends BoolExpr {
    @Override
    default @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @Override
    default @NotNull Type getType() {
        return Type.BiBoolExpr;
    }

    /**
     * Returns the left-hand boolean expression.
     *
     * @return the left-hand boolean expression
     */
    @NotNull BoolExpr getLeft();

    /**
     * Returns the right-hand boolean expression.
     *
     * @return the right-hand boolean expression
     */
    @NotNull BoolExpr getRight();
}
