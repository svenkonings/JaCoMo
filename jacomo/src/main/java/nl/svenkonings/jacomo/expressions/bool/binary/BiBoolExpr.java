package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BiBoolExpr extends BoolExpr {
    @Override
    default @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @Override
    default @NotNull Type getType() {
        return Type.BiBoolExpr;
    }

    @NotNull BoolExpr getLeft();

    @NotNull BoolExpr getRight();
}
