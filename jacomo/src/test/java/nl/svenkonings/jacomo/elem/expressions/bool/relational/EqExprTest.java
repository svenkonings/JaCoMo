package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqExprTest {

    @Test
    public void lowLow() {
        testEqExpr(-1, -1, true);
    }

    @Test
    public void highLow() {
        testEqExpr(1, -1, false);
    }

    @Test
    public void lowHigh() {
        testEqExpr(-1, 1, false);
    }

    @Test
    public void highHigh() {
        testEqExpr(1, 1, true);
    }

    @Test
    public void varLow() {
        testEqExpr(null, -1, null);
    }

    @Test
    public void varHigh() {
        testEqExpr(null, 1, null);
    }

    @Test
    public void lowVar() {
        testEqExpr(-1, null, null);
    }

    @Test
    public void highVar() {
        testEqExpr(1, null, null);
    }

    public static void testEqExpr(Integer left, Integer right, Boolean result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        EqExpr eqExpr = leftExpr.eq(rightExpr);
        assertEquals(result != null, eqExpr.hasValue());
        assertEquals(result, eqExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedEqExpr(null, -1, 1, null, false);
    }

    @Test
    public void leBound() {
        testBoundedEqExpr(null, 0, 0, null, null);
    }

    @Test
    public void geBound() {
        testBoundedEqExpr(0, null, null, 0, null);
    }

    @Test
    public void gtBound() {
        testBoundedEqExpr(1, null, null, -1, false);
    }

    public static void testBoundedEqExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Boolean result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        EqExpr eqExpr = leftVar.eq(rightVar);
        assertEquals(result != null, eqExpr.hasValue());
        assertEquals(result, eqExpr.getValue());
    }
}
