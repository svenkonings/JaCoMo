/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.constraints;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Represent a constraint using a boolean expression.
 */
public class BoolExprConstraint implements Constraint {
    private final @NotNull BoolExpr expr;

    /**
     * Create a constraint with the specified expression.
     *
     * @param expr the specified expression
     */
    public BoolExprConstraint(@NotNull BoolExpr expr) {
        this.expr = expr;
    }

    /**
     * Returns the expression of this constraint.
     *
     * @return the expression of this constraint
     */
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
        return Objects.hash("BoolExprConstraint", expr);
    }
}
