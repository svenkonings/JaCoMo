package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LtExprTest {

    @Test
    public void lowLow() {
        testLtExpr(-1, -1, false);
    }

    @Test
    public void highLow() {
        testLtExpr(1, -1, false);
    }

    @Test
    public void lowHigh() {
        testLtExpr(-1, 1, true);
    }

    @Test
    public void highHigh() {
        testLtExpr(1, 1, false);
    }

    @Test
    public void varLow() {
        testLtExpr(null, -1, null);
    }

    @Test
    public void varHigh() {
        testLtExpr(null, 1, null);
    }

    @Test
    public void lowVar() {
        testLtExpr(-1, null, null);
    }

    @Test
    public void highVar() {
        testLtExpr(1, null, null);
    }

    public static void testLtExpr(Integer left, Integer right, Boolean result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        LtExpr ltExpr = leftExpr.lt(rightExpr);
        assertEquals(result != null, ltExpr.hasValue());
        assertEquals(result, ltExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedLtExpr(null, -1, 1, null, true);
    }

    @Test
    public void leBound() {
        testBoundedLtExpr(null, 0, 0, null, null);
    }

    @Test
    public void geBound() {
        testBoundedLtExpr(0, null, null, 0, false);
    }

    @Test
    public void gtBound() {
        testBoundedLtExpr(1, null, null, -1, false);
    }

    public static void testBoundedLtExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Boolean result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        LtExpr ltExpr = leftVar.lt(rightVar);
        assertEquals(result != null, ltExpr.hasValue());
        assertEquals(result, ltExpr.getValue());
    }
}
