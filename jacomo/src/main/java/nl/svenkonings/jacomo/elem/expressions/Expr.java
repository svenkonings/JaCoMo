/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a expression.
 */
public interface Expr extends Elem {
    @Override
    default @NotNull Type getType() {
        return Type.Expr;
    }

    /**
     * Returns whether this expression has a (instantiated) value.
     *
     * @return {@code true} if this expression has a value
     */
    boolean hasValue();
}
