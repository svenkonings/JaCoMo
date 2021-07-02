/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.expressions.bool.binary;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AndExprTest {

    @Test
    public void falseFalse() {
        testAndExpr(false, false, false);
    }

    @Test
    public void trueFalse() {
        testAndExpr(true, false, false);
    }

    @Test
    public void falseTrue() {
        testAndExpr(false, true, false);
    }

    @Test
    public void trueTrue() {
        testAndExpr(true, true, true);
    }

    @Test
    public void varFalse() {
        testAndExpr(null, false, false);
    }

    @Test
    public void varTrue() {
        testAndExpr(null, true, null);
    }

    @Test
    public void falseVar() {
        testAndExpr(false, null, false);
    }

    @Test
    public void trueVar() {
        testAndExpr(true, null, null);
    }

    public static void testAndExpr(Boolean left, Boolean right, Boolean result) {
        BoolExpr leftExpr = left == null ? BoolVar.variable("left") : BoolExpr.constant(left);
        BoolExpr rightExpr = right == null ? BoolVar.variable("right") : BoolExpr.constant(right);
        AndExpr andExpr = leftExpr.and(rightExpr);
        assertEquals(result != null, andExpr.hasValue());
        assertEquals(result, andExpr.getValue());
    }
}
