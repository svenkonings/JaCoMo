/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.constraints;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.Type;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a constraint.
 */
public interface Constraint extends Elem {
    @Override
    default @NotNull Type getType() {
        return Type.Constraint;
    }
}
