package nl.svenkonings.jacomo.expressions.bool;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.Expr;
import nl.svenkonings.jacomo.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.expressions.bool.binary.OrExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BoolExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.BoolExpr;
    }

    boolean hasValue();

    @Nullable Boolean getValue();

    // Binary bool expressions
    default AndExpr and(BoolExpr other) {
        return new AndExpr(this, other);
    }

    default OrExpr or(BoolExpr other) {
        return new OrExpr(this, other);
    }
}
