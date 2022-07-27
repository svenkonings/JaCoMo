/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Represents a boolean variable defined by an expression.
 */
public class ExpressionBoolVar implements BoolVar {
    private final @NotNull String name;
    private final @NotNull BoolExpr expression;

    /**
     * Create a new boolean variable with the specified name and expression.
     *
     * @param name       the specified name
     * @param expression the specified expression
     */
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
    public boolean hasValue() {
        return expression.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        return expression.getValue();
    }

    @Override
    public String toString() {
        return String.format("bool %s = %s", name, expression);
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
        return Objects.hash("ExpressionBoolVar", name, expression);
    }
}
