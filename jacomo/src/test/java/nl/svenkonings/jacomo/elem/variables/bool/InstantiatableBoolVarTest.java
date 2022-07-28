/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstantiatableBoolVarTest {

    @Test
    public void testUninstantiatedBoolVar() {
        InstantiatableBoolVar var = new InstantiatableBoolVar("test");
        assertFalse(var.hasValue());
        assertNull(var.getValue());
        var.instantiateValue(true);
        assertTrue(var.hasValue());
        assertNotNull(var.getValue());
        assertTrue(var.getValue());
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(false));


    }

    @Test
    public void testInstantiatedBoolVar() {
        InstantiatableBoolVar var = new InstantiatableBoolVar("test", false);
        assertTrue(var.hasValue());
        assertNotNull(var.getValue());
        assertFalse(var.getValue());
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(true));
    }
}
