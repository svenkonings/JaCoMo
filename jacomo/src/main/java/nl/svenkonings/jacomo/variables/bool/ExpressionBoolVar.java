package nl.svenkonings.jacomo.variables.bool;

import nl.svenkonings.jacomo.Type;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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

    public @NotNull BoolExpr getExpression() {
        return expression;
    }

    @Override
    public @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(expression);
    }

    @Override
    public @NotNull Type getType() {
        return Type.ExpressionBoolVar;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionBoolVar that = (ExpressionBoolVar) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expression);
    }
}
