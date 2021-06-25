package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqExprTest {

    @Test
    public void lowLow() {
        testEqExpr(-1, -1, true, true);
    }

    @Test
    public void highLow() {
        testEqExpr(1, -1, true, false);
    }

    @Test
    public void lowHigh() {
        testEqExpr(-1, 1, true, false);
    }

    @Test
    public void highHigh() {
        testEqExpr(1, 1, true, true);
    }

    @Test
    public void varLow() {
        testEqExpr(null, -1, false, null);
    }

    @Test
    public void varHigh() {
        testEqExpr(null, 1, false, null);
    }

    @Test
    public void lowVar() {
        testEqExpr(-1, null, false, null);
    }

    @Test
    public void highVar() {
        testEqExpr(1, null, false, null);
    }

    public static void testEqExpr(Integer left, Integer right, boolean hasValue, Boolean getValue) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        EqExpr eqExpr = leftExpr.eq(rightExpr);
        assertEquals(hasValue, eqExpr.hasValue());
        assertEquals(getValue, eqExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedEqExpr(null, -1, 1, null, true, false);
    }

    @Test
    public void leBound() {
        testBoundedEqExpr(null, 0, 0, null, false, null);
    }

    @Test
    public void geBound() {
        testBoundedEqExpr(0, null, null, 0, false, null);
    }

    @Test
    public void gtBound() {
        testBoundedEqExpr(1, null, null, -1, true, false);
    }

    public static void testBoundedEqExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, boolean hasValue, Boolean getValue) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        EqExpr eqExpr = leftVar.eq(rightVar);
        assertEquals(hasValue, eqExpr.hasValue());
        assertEquals(getValue, eqExpr.getValue());
    }
}
