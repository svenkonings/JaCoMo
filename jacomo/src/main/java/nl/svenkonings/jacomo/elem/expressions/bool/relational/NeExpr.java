/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a Not-equals expression.
 */
@SuppressWarnings("ConstantConditions")
public class NeExpr implements ReBoolExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Not-equals boolean expression.
     *
     * @param left  the left-hand side of the Not-equals expression
     * @param right the right-hand side of the Not-equals expression
     */
    public NeExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
        this.left = left;
        this.right = right;
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
            return left.getValue().intValue() != right.getValue().intValue();
        } else if (left.hasLowerBound() && right.hasUpperBound() && left.getLowerBound() > right.getUpperBound()) {
            return true;
        } else if (left.hasUpperBound() && right.hasLowerBound() && left.getUpperBound() < right.getLowerBound()) {
            return true;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " != " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeExpr neExpr = (NeExpr) o;
        return Objects.equals(left, neExpr.left) &&
                Objects.equals(right, neExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash("NeExpr", left, right);
    }
}
