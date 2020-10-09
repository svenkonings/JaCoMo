package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.BiExpr;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a relational boolean expression between to integer expressions.
 */
public interface ReBoolExpr extends BoolExpr, BiExpr {
    @Override
    default @NotNull List<IntExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @Override
    default @NotNull Type getType() {
        return Type.ReBoolExpr;
    }

    /**
     * Returns the left-hand integer expression.
     *
     * @return the left-hand integer expression
     */
    @Override
    @NotNull IntExpr getLeft();

    /**
     * Returns the right-hand integer expression.
     *
     * @return the right-hand integer expression
     */
    @Override
    @NotNull IntExpr getRight();
}
