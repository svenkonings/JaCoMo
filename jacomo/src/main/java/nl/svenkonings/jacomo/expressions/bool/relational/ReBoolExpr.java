package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ReBoolExpr extends BoolExpr {
    @Override
    default @NotNull List<IntExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @Override
    default @NotNull Type getType() {
        return Type.ReBoolExpr;
    }

    @NotNull IntExpr getLeft();

    @NotNull IntExpr getRight();
}
