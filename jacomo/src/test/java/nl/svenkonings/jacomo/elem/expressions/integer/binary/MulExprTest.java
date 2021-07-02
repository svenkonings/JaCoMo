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

public class MulExprTest {

    @Test
    public void valVal() {
        testMulExpr(1, 2, 2);
    }

    @Test
    public void valVar() {
        testMulExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testMulExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testMulExpr(null, null, null);
    }

    public static void testMulExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        MulExpr mulExpr = leftExpr.mul(rightExpr);
        assertEquals(result != null, mulExpr.hasLowerBound());
        assertEquals(result, mulExpr.getLowerBound());
        assertEquals(result != null, mulExpr.hasUpperBound());
        assertEquals(result, mulExpr.getUpperBound());
        assertEquals(result != null, mulExpr.hasValue());
        assertEquals(result, mulExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedMulExpr(5, null, 6, null, 30, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedMulExpr(7, null, null, 8, null, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedMulExpr(9, null, 10, 11, 90, null, null);
    }

    @Test
    public void highLow() {
        testBoundedMulExpr(null, 12, 13, null, null, null, null);
    }

    @Test
    public void highHigh() {
        testBoundedMulExpr(null, 14, null, 15, null, 210, null);
    }

    @Test
    public void highBoth() {
        testBoundedMulExpr(null, 16, 17, 18, null, 288, null);
    }

    @Test
    public void bothLow() {
        testBoundedMulExpr(19, 20, 21, null, 399, null, null);
    }

    @Test
    public void bothHigh() {
        testBoundedMulExpr(22, 23, null, 24, null, 552, null);
    }

    @Test
    public void bothBoth() {
        testBoundedMulExpr(25, 26, 27, 28, 675, 728, null);
    }

    public static void testBoundedMulExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        MulExpr mulExpr = leftVar.mul(rightVar);
        assertEquals(lowerBound != null, mulExpr.hasLowerBound());
        assertEquals(lowerBound, mulExpr.getLowerBound());
        assertEquals(upperBound != null, mulExpr.hasUpperBound());
        assertEquals(upperBound, mulExpr.getUpperBound());
        assertEquals(result != null, mulExpr.hasValue());
        assertEquals(result, mulExpr.getValue());
    }
}
