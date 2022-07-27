/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;

/**
 * Represents a boolean variable which can be updated.
 */
public interface UpdatableBoolVar extends BoolVar {
    /**
     * Instantiate this boolean variable with the specified value
     *
     * @param value the specified value
     * @throws ContradictionException if this boolean variable has already been instantiated
     */
    void instantiateValue(boolean value) throws ContradictionException;
}
