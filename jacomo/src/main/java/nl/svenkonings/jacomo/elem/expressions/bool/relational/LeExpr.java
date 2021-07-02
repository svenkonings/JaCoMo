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
 * Represents a Lesser-or-equals expression.
 */
@SuppressWarnings("ConstantConditions")
public class LeExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Lesser-or-equals boolean expression.
     *
     * @param left  the left-hand side of the Lesser-or-equals expression
     * @param right the right-hand side of the Lesser-or-equals expression
     */
    public LeExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.LeExpr;
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
                (left.hasUpperBound() && right.hasLowerBound() && left.getUpperBound() <= right.getLowerBound()) ||
                (left.hasLowerBound() && right.hasUpperBound() && !(left.getLowerBound() <= right.getUpperBound()));
    }

    @Override
    public @Nullable Boolean getValue() {
        if (left.hasValue() && right.hasValue()) {
            return left.getValue() <= right.getValue();
        } else if (left.hasUpperBound() && right.hasLowerBound() && left.getUpperBound() <= right.getLowerBound()) {
            return true;
        } else if (left.hasLowerBound() && right.hasUpperBound() && !(left.getLowerBound() <= right.getUpperBound())) {
            return false;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " <= " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeExpr leExpr = (LeExpr) o;
        return Objects.equals(left, leExpr.left) &&
                Objects.equals(right, leExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
