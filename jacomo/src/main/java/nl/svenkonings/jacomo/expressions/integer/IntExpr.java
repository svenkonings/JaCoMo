package nl.svenkonings.jacomo.expressions.integer;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.Expr;
import nl.svenkonings.jacomo.expressions.bool.relational.*;
import nl.svenkonings.jacomo.expressions.integer.binary.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.IntExpr;
    }

    boolean hasValue();

    @Nullable Integer getValue();

    boolean hasLowerBound();

    @Nullable Integer getLowerBound();

    boolean hasUpperBound();

    @Nullable Integer getUpperBound();

    // Binary int expressions
    default AddExpr add(IntExpr other) {
        return new AddExpr(this, other);
    }

    default SubExpr sub(IntExpr other) {
        return new SubExpr(this, other);
    }

    default MulExpr mul(IntExpr other) {
        return new MulExpr(this, other);
    }

    default DivExpr div(IntExpr other) {
        return new DivExpr(this, other);
    }

    default MinExpr min(IntExpr other) {
        return new MinExpr(this, other);
    }

    default MaxExpr max(IntExpr other) {
        return new MaxExpr(this, other);
    }

    // Relational bool expressions
    default EqExpr eq(IntExpr other) {
        return new EqExpr(this, other);
    }

    default NeExpr ne(IntExpr other) {
        return new NeExpr(this, other);
    }

    default LtExpr lt(IntExpr other) {
        return new LtExpr(this, other);
    }

    default LeExpr le(IntExpr other) {
        return new LeExpr(this, other);
    }

    default GtExpr gt(IntExpr other) {
        return new GtExpr(this, other);
    }

    default GeExpr ge(IntExpr other) {
        return new GeExpr(this, other);
    }
}
