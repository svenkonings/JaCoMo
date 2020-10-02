package nl.svenkonings.jacomo.expressions.integer.binary;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BiIntExpr extends IntExpr {
    @Override
    default @NotNull List<IntExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @NotNull IntExpr getLeft();

    @NotNull IntExpr getRight();
}
