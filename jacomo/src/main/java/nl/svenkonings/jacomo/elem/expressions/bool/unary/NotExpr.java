/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.unary;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Not boolean expression
 */
@SuppressWarnings("ConstantConditions")
public class NotExpr implements UnBoolExpr {
    private final @NotNull BoolExpr expr;

    /**
     * Creates a new Not boolean expression.
     *
     * @param expr the expression to negate
     */
    public NotExpr(@NotNull BoolExpr expr) {
        this.expr = expr;
    }

    @Override
    public @NotNull Type getType() {
        return Type.NotExpr;
    }

    @Override
    public @NotNull BoolExpr getExpr() {
        return expr;
    }

    @Override
    public boolean hasValue() {
        return expr.hasValue();
    }

    @Override
    public @Nullable Boolean getValue() {
        if (expr.hasValue()) {
            return !expr.getValue();
        } else {
            return null;
        }
    }
}
