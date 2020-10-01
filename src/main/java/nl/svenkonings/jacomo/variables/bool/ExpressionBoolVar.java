package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpressionBoolVar implements BoolVar {
    private final @NotNull String name;
    private final @NotNull BoolExpr expression;

    public ExpressionBoolVar(@NotNull String name, @NotNull BoolExpr expression) {
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
    public @NotNull BoolExpr getExpression() {
        return expression;
    }

    @Override
    public boolean hasValue() {
        return expression.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        return expression.getValue();
    }

    @Override
    public String toString() {
        return boolVarString();
    }
}
