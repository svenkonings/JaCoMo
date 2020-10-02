package nl.svenkonings.jacomo.constraints;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BoolExprConstraint implements Constraint {
    private final @NotNull BoolExpr expr;

    public BoolExprConstraint(@NotNull BoolExpr expr) {
        this.expr = expr;
    }

    public @NotNull BoolExpr getExpr() {
        return expr;
    }

    @Override
    public @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(expr);
    }

    @Override
    public @NotNull Type getType() {
        return Type.BoolExprConstraint;
    }

    @Override
    public String toString() {
        return "constraint " + expr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolExprConstraint that = (BoolExprConstraint) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
}
