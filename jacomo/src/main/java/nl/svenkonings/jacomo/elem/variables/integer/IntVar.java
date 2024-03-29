/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.integer;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a named integer variable.
 */
public interface IntVar extends Var, IntExpr {
    /**
     * Returns a string representation of this variable.
     *
     * @return a string representation of this variable
     */
    default String intVarString() {
        String name = getName();
        Integer value = getValue();
        Integer lowerBound = getLowerBound();
        Integer upperBound = getUpperBound();
        if (value != null) {
            return String.format("int %s = %d", name, value);
        } else if (lowerBound != null && upperBound != null) {
            return String.format("int %s = [%d..%d]", name, lowerBound, upperBound);
        } else if (lowerBound != null) {
            return String.format("int %s = [%d..]", name, lowerBound);
        } else if (upperBound != null) {
            return String.format("int %s = [..%d]", name, upperBound);
        } else {
            return String.format("int %s", name);
        }
    }

    // Factory methods

    /**
     * Creates a new integer variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     * @return the created integer variable
     */
    static ConstantIntVar constant(@NotNull String name, int value) {
        return new ConstantIntVar(name, value);
    }

    /**
     * Creates a new integer variable with the specified name.
     *
     * @param name the specified name
     * @return the created integer variable
     */
    static BoundedIntVar variable(@NotNull String name) {
        return new BoundedIntVar(name);
    }

    /**
     * Creates a new integer variable with the specified name and lower bound.
     *
     * @param name       the specified name
     * @param lowerBound the specified lower bound
     * @return the created integer variable
     */
    static BoundedIntVar lowerBound(@NotNull String name, @Nullable Integer lowerBound) {
        return new BoundedIntVar(name, lowerBound, null);
    }

    /**
     * Creates a new integer variable with the specified name and upper bound.
     *
     * @param name       the specified name
     * @param upperBound the specified upper bound
     * @return the created integer variable
     */
    static BoundedIntVar upperBound(@NotNull String name, @Nullable Integer upperBound) {
        return new BoundedIntVar(name, null, upperBound);
    }

    /**
     * Creates a new integer variable with the specified name and bounds.
     *
     * @param name       the specified name
     * @param lowerBound the specified lower bound
     * @param upperBound the specified upper bound
     * @return the created integer variable
     * @throws ContradictionException if the lower bound is higher than the upper bound
     */
    static BoundedIntVar bounds(@NotNull String name, @Nullable Integer lowerBound, @Nullable Integer upperBound) throws ContradictionException {
        return new BoundedIntVar(name, lowerBound, upperBound);
    }

    /**
     * Creates a new integer variable with the specified name and expression.
     *
     * @param name the specified name
     * @param expr the specified expression
     * @return the created integer variable
     */
    static ExpressionIntVar expression(@NotNull String name, @NotNull IntExpr expr) {
        return new ExpressionIntVar(name, expr);
    }
}
