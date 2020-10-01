package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;

public interface ReBoolExpr extends BoolExpr {
    @NotNull IntExpr getLeft();

    @NotNull IntExpr getRight();
}
