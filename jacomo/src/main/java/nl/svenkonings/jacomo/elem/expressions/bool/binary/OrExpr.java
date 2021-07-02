/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.binary;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represent an Or boolean expression.
 */
@SuppressWarnings("ConstantConditions")
public class OrExpr implements BiBoolExpr {
    private final @NotNull BoolExpr left;
    private final @NotNull BoolExpr right;

    /**
     * Create a new Or boolean expression.
     *
     * @param left  the left-hand side of the Or expression
     * @param right the right-hand side of the Or expression
     */
    public OrExpr(@NotNull BoolExpr left, @NotNull BoolExpr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public @NotNull Type getType() {
        return Type.OrExpr;
    }

    @Override
    public @NotNull BoolExpr getLeft() {
        return left;
    }

    @Override
    public @NotNull BoolExpr getRight() {
        return right;
    }

    @Override
    public boolean hasValue() {
        return (left.hasValue() && right.hasValue()) ||
                (left.hasValue() && left.getValue()) ||
                (right.hasValue() && right.getValue());
    }

    @Override
    public @Nullable Boolean getValue() {
        if (left.hasValue() && right.hasValue()) {
            return left.getValue() || right.getValue();
        } else if (left.hasValue() && left.getValue()) {
            return true;
        } else if (right.hasValue() && right.getValue()) {
            return true;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " || " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrExpr orExpr = (OrExpr) o;
        return Objects.equals(left, orExpr.left) &&
                Objects.equals(right, orExpr.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash("OrExpr", left, right);
    }
}
