/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.integer.binary;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an Division expression.
 */
@SuppressWarnings({"ConstantConditions", "UnnecessaryUnboxing"})
public class DivExpr implements BiIntExpr {

    private final @NotNull IntExpr left;
    private final @NotNull IntExpr right;

    /**
     * Create a new Division integer expression.
     *
     * @param left  the left-hand side of the Division expression
     * @param right the right-hand side of the Division expression
     */
    public DivExpr(@NotNull IntExpr left, @NotNull IntExpr right) {
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
        return (hasUpperBound() && getUpperBound().intValue() == 0) ||
                (left.hasValue() && right.hasValue() && right.getValue().intValue() != 0);
    }

    @Override
    public @Nullable Integer getValue() {
        if (hasUpperBound() && getUpperBound().intValue() == 0) {
            return 0;
        } else if (hasValue()) {
            return left.getValue() / right.getValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasLowerBound() {
        return (hasUpperBound() && getUpperBound().intValue() == 0) ||
                (left.hasLowerBound() && right.hasUpperBound() && right.getUpperBound().intValue() != 0);
    }

    @Override
    public @Nullable Integer getLowerBound() {
        if (hasUpperBound() && getUpperBound().intValue() == 0) {
            return 0;
        } else if (hasLowerBound()) {
            return left.getLowerBound() / right.getUpperBound();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasUpperBound() {
        return left.hasUpperBound() && right.hasLowerBound() && right.getLowerBound().intValue() != 0;
    }

    @Override
    public @Nullable Integer getUpperBound() {
        if (hasUpperBound()) {
            return left.getUpperBound() / right.getLowerBound();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " / " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DivExpr divExpr = (DivExpr) o;
        return Objects.equals(left, divExpr.left) &&
                Objects.equals(right, divExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash("DivExpr", left, right);
    }
}
