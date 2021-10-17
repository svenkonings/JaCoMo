/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.Var;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a named boolean variable.
 */
public interface BoolVar extends Var, BoolExpr {
    @Override
    default @NotNull Type getType() {
        return Type.BoolVar;
    }

    /**
     * Returns a string representation of this variable.
     *
     * @return a string representation of this variable
     */
    default String boolVarString() {
        String name = getName();
        Boolean value = getValue();
        if (value != null) {
            return String.format("bool %s = %b", name, value);
        } else {
            return String.format("bool %s", name);
        }
    }

    // Factory methods

    /**
     * Creates a new boolean variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     * @return the created boolean variable
     */
    static ConstantBoolVar constant(@NotNull String name, boolean value) {
        return new ConstantBoolVar(name, value);
    }

    /**
     * Creates a new boolean variable with the specified name.
     *
     * @param name the specified name
     * @return the created boolean variable
     */
    static InstantiatableBoolVar variable(@NotNull String name) {
        return new InstantiatableBoolVar(name);
    }

    /**
     * Creates a new boolean variable with the specified name and expression.
     *
     * @param name the specified name
     * @param expr the specified expression
     * @return the created boolean variable
     */
    static ExpressionBoolVar expression(@NotNull String name, @NotNull BoolExpr expr) {
        return new ExpressionBoolVar(name, expr);
    }
}
