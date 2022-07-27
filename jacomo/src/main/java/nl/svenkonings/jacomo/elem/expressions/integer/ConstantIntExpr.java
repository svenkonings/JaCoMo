/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.integer;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a constant integer expression.
 */
public class ConstantIntExpr implements IntExpr {

    private final int value;

    /**
     * Create a new constant integer expression with the specified value.
     *
     * @param value the specified value
     */
    public ConstantIntExpr(int value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public @NotNull Integer getValue() {
        return value;
    }

    @Override
    public boolean hasLowerBound() {
        return true;
    }

    @Override
    public @NotNull Integer getLowerBound() {
        return value;
    }

    @Override
    public boolean hasUpperBound() {
        return true;
    }

    @Override
    public @NotNull Integer getUpperBound() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantIntExpr that = (ConstantIntExpr) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash("ConstantIntExpr", value);
    }
}
