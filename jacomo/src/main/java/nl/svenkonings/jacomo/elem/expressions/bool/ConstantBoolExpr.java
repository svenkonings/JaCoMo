/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a constant boolean expression.
 */
public class ConstantBoolExpr implements BoolExpr {

    private final boolean value;

    /**
     * Create a new constant boolean expression with the specified value.
     *
     * @param value the specified value
     */
    public ConstantBoolExpr(boolean value) {
        this.value = value;
    }

    @Override
    public @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    @Override
    public @NotNull Type getType() {
        return Type.ConstantBoolExpr;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public @NotNull Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantBoolExpr that = (ConstantBoolExpr) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash("ConstantBoolExpr", value);
    }
}
