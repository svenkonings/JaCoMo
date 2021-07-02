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

public class DivExprTest {

    @Test
    public void valVal() {
        testDivExpr(4, 2, 2);
    }

    @Test
    public void valVar() {
        testDivExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testDivExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testDivExpr(null, null, null);
    }

    public static void testDivExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        DivExpr divExpr = leftExpr.div(rightExpr);
        assertEquals(result != null, divExpr.hasLowerBound());
        assertEquals(result, divExpr.getLowerBound());
        assertEquals(result != null, divExpr.hasUpperBound());
        assertEquals(result, divExpr.getUpperBound());
        assertEquals(result != null, divExpr.hasValue());
        assertEquals(result, divExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedDivExpr(5, null, 6, null, null, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedDivExpr(15, null, null, 5, 3, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedDivExpr(9, null, 10, 11, 0, null, null);
    }

    @Test
    public void highLow() {
        testBoundedDivExpr(null, 5, 3, null, null, 1, null);
    }

    @Test
    public void highHigh() {
        testBoundedDivExpr(null, 14, null, 15, null, null, null);
    }

    @Test
    public void highBoth() {
        testBoundedDivExpr(null, 16, 17, 18, 0, 0, 0);
    }

    @Test
    public void bothLow() {
        testBoundedDivExpr(19, 22, 11, null, null, 2, null);
    }

    @Test
    public void bothHigh() {
        testBoundedDivExpr(22, 23, null, 24, 0, null, null);
    }

    @Test
    public void bothBoth() {
        testBoundedDivExpr(10, 30, 2, 5, 2, 15, null);
    }

    public static void testBoundedDivExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        DivExpr divExpr = leftVar.div(rightVar);
        assertEquals(lowerBound != null, divExpr.hasLowerBound());
        assertEquals(lowerBound, divExpr.getLowerBound());
        assertEquals(upperBound != null, divExpr.hasUpperBound());
        assertEquals(upperBound, divExpr.getUpperBound());
        assertEquals(result != null, divExpr.hasValue());
        assertEquals(result, divExpr.getValue());
    }
}
