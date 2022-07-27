/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.unary;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a Not boolean expression
 */
@SuppressWarnings("ConstantConditions")
public class NotExpr implements UnBoolExpr {
    private final @NotNull BoolExpr expr;

    /**
     * Creates a new Not boolean expression.
     *
     * @param expr the expression to negate
     */
    public NotExpr(@NotNull BoolExpr expr) {
        this.expr = expr;
    }

    @Override
    public @NotNull BoolExpr getExpr() {
        return expr;
    }

    @Override
    public boolean hasValue() {
        return expr.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (expr.hasValue()) {
            return !expr.getValue();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "!" + expr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotExpr notExpr = (NotExpr) o;
        return Objects.equals(expr, notExpr.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash("NotExpr", expr);
    }
}
