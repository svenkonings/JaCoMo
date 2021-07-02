/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeExprTest {

    @Test
    public void lowLow() {
        testGeExpr(-1, -1, true);
    }

    @Test
    public void highLow() {
        testGeExpr(1, -1, true);
    }

    @Test
    public void lowHigh() {
        testGeExpr(-1, 1, false);
    }

    @Test
    public void highHigh() {
        testGeExpr(1, 1, true);
    }

    @Test
    public void varLow() {
        testGeExpr(null, -1, null);
    }

    @Test
    public void varHigh() {
        testGeExpr(null, 1, null);
    }

    @Test
    public void lowVar() {
        testGeExpr(-1, null, null);
    }

    @Test
    public void highVar() {
        testGeExpr(1, null, null);
    }

    public static void testGeExpr(Integer left, Integer right, Boolean result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        GeExpr geExpr = leftExpr.ge(rightExpr);
        assertEquals(result != null, geExpr.hasValue());
        assertEquals(result, geExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedGeExpr(null, -1, 1, null, false);
    }

    @Test
    public void leBound() {
        testBoundedGeExpr(null, 0, 0, null, null);
    }

    @Test
    public void geBound() {
        testBoundedGeExpr(0, null, null, 0, true);
    }

    @Test
    public void gtBound() {
        testBoundedGeExpr(1, null, null, -1, true);
    }

    public static void testBoundedGeExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Boolean result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        GeExpr geExpr = leftVar.ge(rightVar);
        assertEquals(result != null, geExpr.hasValue());
        assertEquals(result, geExpr.getValue());
    }
}
