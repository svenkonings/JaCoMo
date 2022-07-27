/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.unary;

import nl.svenkonings.jacomo.elem.expressions.UnExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represent a unary boolean expression applied to a boolean sub-expression.
 */
public interface UnBoolExpr extends BoolExpr, UnExpr {
    @Override
    default @NotNull List<? extends BoolExpr> getChildren() {
        return ListUtil.of(getExpr());
    }

    /**
     * Returns the boolean sub-expression.
     *
     * @return the boolean sub-expression
     */
    @Override
    @NotNull BoolExpr getExpr();
}
