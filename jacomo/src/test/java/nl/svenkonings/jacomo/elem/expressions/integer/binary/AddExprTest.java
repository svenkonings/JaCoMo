/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.integer.binary;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddExprTest {

    @Test
    public void valVal() {
        testAddExpr(1, 2, 3);
    }

    @Test
    public void valVar() {
        testAddExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testAddExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testAddExpr(null, null, null);
    }

    public static void testAddExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        AddExpr addExpr = leftExpr.add(rightExpr);
        assertEquals(result != null, addExpr.hasLowerBound());
        assertEquals(result, addExpr.getLowerBound());
        assertEquals(result != null, addExpr.hasUpperBound());
        assertEquals(result, addExpr.getUpperBound());
        assertEquals(result != null, addExpr.hasValue());
        assertEquals(result, addExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedAddExpr(5, null, 6, null, 11, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedAddExpr(7, null, null, 8, null, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedAddExpr(9, null, 10, 11, 19, null, null);
    }

    @Test
    public void highLow() {
        testBoundedAddExpr(null, 12, 13, null, null, null, null);
    }

    @Test
    public void highHigh() {
        testBoundedAddExpr(null, 14, null, 15, null, 29, null);
    }

    @Test
    public void highBoth() {
        testBoundedAddExpr(null, 16, 17, 18, null, 34, null);
    }

    @Test
    public void bothLow() {
        testBoundedAddExpr(19, 20, 21, null, 40, null, null);
    }

    @Test
    public void bothHigh() {
        testBoundedAddExpr(22, 23, null, 24, null, 47, null);
    }

    @Test
    public void bothBoth() {
        testBoundedAddExpr(25, 26, 27, 28, 52, 54, null);
    }

    public static void testBoundedAddExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        AddExpr addExpr = leftVar.add(rightVar);
        assertEquals(lowerBound != null, addExpr.hasLowerBound());
        assertEquals(lowerBound, addExpr.getLowerBound());
        assertEquals(upperBound != null, addExpr.hasUpperBound());
        assertEquals(upperBound, addExpr.getUpperBound());
        assertEquals(result != null, addExpr.hasValue());
        assertEquals(result, addExpr.getValue());
    }
}
