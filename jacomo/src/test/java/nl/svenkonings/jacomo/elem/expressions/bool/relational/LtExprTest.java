package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LtExprTest {

    @Test
    public void lowLow() {
        testLtExpr(-1, -1, true, false);
    }

    @Test
    public void highLow() {
        testLtExpr(1, -1, true, false);
    }

    @Test
    public void lowHigh() {
        testLtExpr(-1, 1, true, true);
    }

    @Test
    public void highHigh() {
        testLtExpr(1, 1, true, false);
    }

    @Test
    public void varLow() {
        testLtExpr(null, -1, false, null);
    }

    @Test
    public void varHigh() {
        testLtExpr(null, 1, false, null);
    }

    @Test
    public void lowVar() {
        testLtExpr(-1, null, false, null);
    }

    @Test
    public void highVar() {
        testLtExpr(1, null, false, null);
    }

    public static void testLtExpr(Integer left, Integer right, boolean hasValue, Boolean getValue) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        LtExpr ltExpr = leftExpr.lt(rightExpr);
        assertEquals(hasValue, ltExpr.hasValue());
        assertEquals(getValue, ltExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedLtExpr(null, -1, 1, null, true, true);
    }

    @Test
    public void leBound() {
        testBoundedLtExpr(null, 0, 0, null, false, null);
    }

    @Test
    public void geBound() {
        testBoundedLtExpr(0, null, null, 0, true, false);
    }

    @Test
    public void gtBound() {
        testBoundedLtExpr(1, null, null, -1, true, false);
    }

    public static void testBoundedLtExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, boolean hasValue, Boolean getValue) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        LtExpr ltExpr = leftVar.lt(rightVar);
        assertEquals(hasValue, ltExpr.hasValue());
        assertEquals(getValue, ltExpr.getValue());
    }
}
