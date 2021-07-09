/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.Expr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.AndExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.binary.OrExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.unary.NotExpr;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static nl.svenkonings.jacomo.util.ArrayUtil.foldLeft;

/**
 * Represents a boolean expression.
 */
public interface BoolExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.BoolExpr;
    }

    /**
     * Returns the value of this expression
     *
     * @return the value of this expression, or {@code null} if it is uninstantiated
     */
    @Nullable Boolean getValue();

    // Factory methods

    /**
     * Creates a new boolean constant with the specified value.
     *
     * @param value the specified value
     * @return the created boolean constant
     */
    static ConstantBoolExpr constant(boolean value) {
        return new ConstantBoolExpr(value);
    }

    // Unary bool expressions

    /**
     * Creates a Not expression that negates this expression.
     *
     * @return the created Not expression
     */
    default NotExpr not() {
        return new NotExpr(this);
    }

    // Binary bool expressions

    /**
     * Creates a And expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created And expression
     */
    default AndExpr and(BoolExpr other) {
        return new AndExpr(this, other);
    }

    /**
     * Creates an And expression of the specified elements.
     *
     * @param exprs the specified elements
     * @return the created And expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static AndExpr and(BoolExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, AndExpr::new);
    }

    /**
     * Creates a Or expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Or expression
     */
    default OrExpr or(BoolExpr other) {
        return new OrExpr(this, other);
    }

    /**
     * Creates an Or expression of the specified elements.
     *
     * @param exprs the specified elements
     * @return the created Or expression
     * @throws InvalidInputException when less than two elements are specified
     */
    static OrExpr or(BoolExpr... exprs) throws InvalidInputException {
        return foldLeft(exprs, OrExpr::new);
    }
}
