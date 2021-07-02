/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an Equals expression.
 */
@SuppressWarnings("ConstantConditions")
public class EqExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Equals boolean expression.
     *
     * @param left  the left-hand side of the Equals expression
     * @param right the right-hand side of the Equals expression
     */
    public EqExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.EqExpr;
    }

    @Override
    public @NotNull IntExpr getLeft() {
        return left;
    }

    @Override
    public @NotNull IntExpr getRight() {
        return right;
    }

    @Override
    public boolean hasValue() {
        return (left.hasValue() && right.hasValue()) ||
                (left.hasLowerBound() && right.hasUpperBound() && left.getLowerBound() > right.getUpperBound()) ||
                (left.hasUpperBound() && right.hasLowerBound() && left.getUpperBound() < right.getLowerBound());
    }

    @Override
    public @Nullable Boolean getValue() {
        if (left.hasValue() && right.hasValue()) {
            return left.getValue().intValue() == right.getValue().intValue();
        } else if (left.hasLowerBound() && right.hasUpperBound() && left.getLowerBound() > right.getUpperBound()) {
            return false;
        } else if (left.hasUpperBound() && right.hasLowerBound() && left.getUpperBound() < right.getLowerBound()) {
            return false;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " == " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EqExpr eqExpr = (EqExpr) o;
        return Objects.equals(left, eqExpr.left) &&
                Objects.equals(right, eqExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash("EqExpr", left, right);
    }
}
