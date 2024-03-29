/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.integer;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;

/**
 * Represents an updatable integer variable.
 */
public interface UpdatableIntVar extends IntVar {
    /**
     * Instantiate this integer variable with the specified value.
     *
     * @param value the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void instantiateValue(int value) throws ContradictionException;

    /**
     * Update the lower-bound of this variable with the specified value.
     *
     * @param lowerBound the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void updateLowerBound(int lowerBound) throws ContradictionException;

    /**
     * Update the upper-bound of this variable with the specified value.
     *
     * @param upperBound the specified value
     * @throws ContradictionException if the specified value is outside
     *                                the current bounds of this variable
     */
    void updateUpperBound(int upperBound) throws ContradictionException;

    /**
     * Update both bounds of this variable with the specified values.
     *
     * @param lowerBound the specified lower-bound value
     * @param upperBound the specified upper-bound value
     * @throws ContradictionException if one of the specified value is
     *                                outside the bounds of this variable
     */
    void updateBounds(int lowerBound, int upperBound) throws ContradictionException;
}
