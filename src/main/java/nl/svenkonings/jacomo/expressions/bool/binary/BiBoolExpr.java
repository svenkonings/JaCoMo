package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;

public interface BiBoolExpr extends BoolExpr {
    @NotNull BoolExpr getLeft();

    @NotNull BoolExpr getRight();
}
