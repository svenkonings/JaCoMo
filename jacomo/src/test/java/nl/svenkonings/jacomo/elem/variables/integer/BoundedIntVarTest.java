/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.integer;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoundedIntVarTest {

    @Test
    public void testUnboundedIntVar() {
        BoundedIntVar var = new BoundedIntVar("test");
        assertFalse(var.hasValue());
        assertFalse(var.hasLowerBound());
        assertFalse(var.hasUpperBound());
        assertNull(var.getValue());
        assertNull(var.getLowerBound());
        assertNull(var.getUpperBound());
        var.updateLowerBound(1);
        assertFalse(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertFalse(var.hasUpperBound());
        assertNull(var.getValue());
        assertEquals(1, var.getLowerBound());
        assertNull(var.getUpperBound());
        var.updateUpperBound(1);
        assertTrue(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertEquals(1, var.getValue());
        assertEquals(1, var.getLowerBound());
        assertEquals(1, var.getUpperBound());
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(0));
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(2));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(0));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(2));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(0, 1));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(2, 1));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(1, 0));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(1, 2));
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(0));
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(2));
    }

    @Test
    public void testInstantiatedIntVar() {
        BoundedIntVar var = new BoundedIntVar("test", 10);
        assertTrue(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertEquals(10, var.getValue());
        assertEquals(10, var.getLowerBound());
        assertEquals(10, var.getUpperBound());
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(9));
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(11));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(9));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(11));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(0, 10));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(20, 10));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(10, 0));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(10, 20));
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(0));
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(20));
    }

    @Test
    public void testContradictedConstructor() {
        assertThrowsExactly(ContradictionException.class, () -> new BoundedIntVar("test", 1, 0));
        assertThrowsExactly(ContradictionException.class, () -> new BoundedIntVar("test", 0, -1));
    }

    @Test
    public void testBelowBounds() {
        BoundedIntVar var = new BoundedIntVar("test", -100, null);
        assertFalse(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertFalse(var.hasUpperBound());
        assertNull(var.getValue());
        assertEquals(-100, var.getLowerBound());
        assertNull(var.getUpperBound());
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(-101));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(-101));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(0, -101));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(-101, 0));
        assertDoesNotThrow(() -> var.updateLowerBound(-100));
        assertDoesNotThrow(() -> var.updateUpperBound(-100));
        assertTrue(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertEquals(-100, var.getValue());
        assertEquals(-100, var.getLowerBound());
        assertEquals(-100, var.getUpperBound());
    }

    @Test
    public void testAboveBounds() {
        BoundedIntVar var = new BoundedIntVar("test", null, 20);
        assertFalse(var.hasValue());
        assertFalse(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertNull(var.getValue());
        assertNull(var.getLowerBound());
        assertEquals(20, var.getUpperBound());
        assertThrowsExactly(ContradictionException.class, () -> var.updateLowerBound(21));
        assertThrowsExactly(ContradictionException.class, () -> var.updateUpperBound(21));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(21, 0));
        assertThrowsExactly(ContradictionException.class, () -> var.updateBounds(0, 21));
        assertDoesNotThrow(() -> var.updateLowerBound(20));
        assertDoesNotThrow(() -> var.updateUpperBound(20));
        assertTrue(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertEquals(20, var.getValue());
        assertEquals(20, var.getLowerBound());
        assertEquals(20, var.getUpperBound());
    }

    @Test
    public void testInstantiateBounds() {
        BoundedIntVar var = new BoundedIntVar("test", 10, 20);
        assertFalse(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertNull(var.getValue());
        assertEquals(10, var.getLowerBound());
        assertEquals(20, var.getUpperBound());
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(9));
        assertThrowsExactly(ContradictionException.class, () -> var.instantiateValue(21));
        assertDoesNotThrow(() -> var.instantiateValue(10));
        assertTrue(var.hasValue());
        assertTrue(var.hasLowerBound());
        assertTrue(var.hasUpperBound());
        assertEquals(10, var.getValue());
        assertEquals(10, var.getLowerBound());
        assertEquals(10, var.getUpperBound());
    }
}
