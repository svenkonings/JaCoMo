package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeExprTest {

    @Test
    public void lowLow() {
        testLeExpr(-1, -1, true, true);
    }

    @Test
    public void highLow() {
        testLeExpr(1, -1, true, false);
    }

    @Test
    public void lowHigh() {
        testLeExpr(-1, 1, true, true);
    }

    @Test
    public void highHigh() {
        testLeExpr(1, 1, true, true);
    }

    @Test
    public void varLow() {
        testLeExpr(null, -1, false, null);
    }

    @Test
    public void varHigh() {
        testLeExpr(null, 1, false, null);
    }

    @Test
    public void lowVar() {
        testLeExpr(-1, null, false, null);
    }

    @Test
    public void highVar() {
        testLeExpr(1, null, false, null);
    }

    public static void testLeExpr(Integer left, Integer right, boolean hasValue, Boolean getValue) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        LeExpr leExpr = leftExpr.le(rightExpr);
        assertEquals(hasValue, leExpr.hasValue());
        assertEquals(getValue, leExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedLeExpr(null, -1, 1, null, true, true);
    }

    @Test
    public void leBound() {
        testBoundedLeExpr(null, 0, 0, null, true, true);
    }

    @Test
    public void geBound() {
        testBoundedLeExpr(0, null, null, 0, false, null);
    }

    @Test
    public void gtBound() {
        testBoundedLeExpr(1, null, null, -1, true, false);
    }

    public static void testBoundedLeExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, boolean hasValue, Boolean getValue) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        LeExpr leExpr = leftVar.le(rightVar);
        assertEquals(hasValue, leExpr.hasValue());
        assertEquals(getValue, leExpr.getValue());
    }
}
