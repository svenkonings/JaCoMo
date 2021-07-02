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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Creates a Or expression of this expression and the specified expression.
     *
     * @param other the specified expression
     * @return the created Or expression
     */
    default OrExpr or(BoolExpr other) {
        return new OrExpr(this, other);
    }
}
