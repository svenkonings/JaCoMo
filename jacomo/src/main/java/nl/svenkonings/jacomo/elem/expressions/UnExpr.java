/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a unary expression with a single sub-expressions.
 */
public interface UnExpr extends Expr {
    @Override
    default @NotNull Type getType() {
        return Type.UnExpr;
    }

    /**
     * Returns the sub-expression.
     *
     * @return the sub-expression
     */
    @NotNull Expr getExpr();
}
