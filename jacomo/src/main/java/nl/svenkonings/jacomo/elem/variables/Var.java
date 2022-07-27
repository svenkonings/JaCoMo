/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.expressions.Expr;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a named variable.
 */
public interface Var extends Elem, Expr {
    /**
     * Returns the name of this variable.
     *
     * @return the name of this variable
     */
    @NotNull String getName();
}
