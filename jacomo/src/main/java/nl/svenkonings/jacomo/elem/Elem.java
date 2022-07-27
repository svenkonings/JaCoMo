/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem;

import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a basic element with a tree-like structure.
 */
public interface Elem {
    /**
     * Get the children elements of this element.
     * The list cannot be modified.
     *
     * @return the list of children.
     */
    default @NotNull List<? extends Elem> getChildren() {
        return ListUtil.of();
    }

    /**
     * Get the type of this element.
     *
     * @return the type.
     */
    default @NotNull String getType() {
        return getClass().getSimpleName();
    }
}
