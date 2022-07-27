/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a binary expression with two sub-expressions.
 */
public interface BiExpr extends Expr {
    @Override
    default @NotNull List<? extends Expr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    /**
     * Returns the left-hand expression.
     *
     * @return the left-hand expression
     */
    @NotNull Expr getLeft();

    /**
     * Returns the right-hand expression.
     *
     * @return the right-hand expression
     */
    @NotNull Expr getRight();
}
