/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions;

import nl.svenkonings.jacomo.elem.Elem;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a expression.
 */
public interface Expr extends Elem {
    /**
     * Returns whether this expression has a (instantiated) value.
     *
     * @return {@code true} if this expression has a value
     */
    boolean hasValue();

    /**
     * Returns the value of this expression
     *
     * @return the value of this expression, or {@code null} if it is uninstantiated
     */
    @Nullable Object getValue();
}
