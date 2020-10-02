package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExpressionIntVar implements IntVar {

    private final @NotNull String name;
    private final @NotNull IntExpr expression;

    public ExpressionIntVar(@NotNull String name, @NotNull IntExpr expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    public @NotNull IntExpr getExpression() {
        return expression;
    }

    @Override
    public @NotNull List<IntExpr> getChildren() {
        return ListUtil.of(expression);
    }

    @Override
    public @NotNull Type getType() {
        return Type.ExpressionIntVar;
    }

    @Override
    public boolean hasValue() {
        return expression.hasValue();
    }

    @Override
    public @Nullable Integer getValue() {
        return expression.getValue();
    }

    @Override
    public boolean hasLowerBound() {
        return expression.hasLowerBound();
    }

    @Override
    public @Nullable Integer getLowerBound() {
        return expression.getLowerBound();
    }

    @Override
    public boolean hasUpperBound() {
        return expression.hasUpperBound();
    }

    @Override
    public @Nullable Integer getUpperBound() {
        return expression.getUpperBound();
    }
}
