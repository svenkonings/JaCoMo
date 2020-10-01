package nl.svenkonings.jacomo.expressions.integer.binary;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;

public interface BiIntExpr extends IntExpr {
    @NotNull IntExpr getLeft();

    @NotNull IntExpr getRight();
}
