package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeExprTest {

    @Test
    public void lowLow() {
        testGeExpr(-1, -1, true, true);
    }

    @Test
    public void highLow() {
        testGeExpr(1, -1, true, true);
    }

    @Test
    public void lowHigh() {
        testGeExpr(-1, 1, true, false);
    }

    @Test
    public void highHigh() {
        testGeExpr(1, 1, true, true);
    }

    @Test
    public void varLow() {
        testGeExpr(null, -1, false, null);
    }

    @Test
    public void varHigh() {
        testGeExpr(null, 1, false, null);
    }

    @Test
    public void lowVar() {
        testGeExpr(-1, null, false, null);
    }

    @Test
    public void highVar() {
        testGeExpr(1, null, false, null);
    }

    public static void testGeExpr(Integer left, Integer right, boolean hasValue, Boolean getValue) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        GeExpr geExpr = leftExpr.ge(rightExpr);
        assertEquals(hasValue, geExpr.hasValue());
        assertEquals(getValue, geExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedGeExpr(null, -1, 1, null, true, false);
    }

    @Test
    public void leBound() {
        testBoundedGeExpr(null, 0, 0, null, false, null);
    }

    @Test
    public void geBound() {
        testBoundedGeExpr(0, null, null, 0, true, true);
    }

    @Test
    public void gtBound() {
        testBoundedGeExpr(1, null, null, -1, true, true);
    }

    public static void testBoundedGeExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, boolean hasValue, Boolean getValue) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        GeExpr geExpr = leftVar.ge(rightVar);
        assertEquals(hasValue, geExpr.hasValue());
        assertEquals(getValue, geExpr.getValue());
    }
}
