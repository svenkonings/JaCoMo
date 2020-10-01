package nl.svenkonings.jacomo.variables.integer;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public boolean hasExpression() {
        return true;
    }

    @Override
    public @NotNull IntExpr getExpression() {
        return expression;
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
