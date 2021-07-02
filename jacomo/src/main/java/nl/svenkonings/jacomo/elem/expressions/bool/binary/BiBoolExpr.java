/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.binary;

import nl.svenkonings.jacomo.elem.Type;
import nl.svenkonings.jacomo.elem.expressions.BiExpr;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represent a binary boolean expression between two boolean expressions.
 */
public interface BiBoolExpr extends BoolExpr, BiExpr {
    @Override
    default @NotNull List<BoolExpr> getChildren() {
        return ListUtil.of(getLeft(), getRight());
    }

    @Override
    default @NotNull Type getType() {
        return Type.BiBoolExpr;
    }

    /**
     * Returns the left-hand boolean expression.
     *
     * @return the left-hand boolean expression
     */
    @Override
    @NotNull BoolExpr getLeft();

    /**
     * Returns the right-hand boolean expression.
     *
     * @return the right-hand boolean expression
     */
    @Override
    @NotNull BoolExpr getRight();
}
