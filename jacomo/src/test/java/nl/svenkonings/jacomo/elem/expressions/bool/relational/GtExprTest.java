package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GtExprTest {

    @Test
    public void lowLow() {
        testGtExpr(-1, -1, false);
    }

    @Test
    public void highLow() {
        testGtExpr(1, -1, true);
    }

    @Test
    public void lowHigh() {
        testGtExpr(-1, 1, false);
    }

    @Test
    public void highHigh() {
        testGtExpr(1, 1, false);
    }

    @Test
    public void varLow() {
        testGtExpr(null, -1, null);
    }

    @Test
    public void varHigh() {
        testGtExpr(null, 1, null);
    }

    @Test
    public void lowVar() {
        testGtExpr(-1, null, null);
    }

    @Test
    public void highVar() {
        testGtExpr(1, null, null);
    }

    public static void testGtExpr(Integer left, Integer right, Boolean result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        GtExpr gtExpr = leftExpr.gt(rightExpr);
        assertEquals(result != null, gtExpr.hasValue());
        assertEquals(result, gtExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedGtExpr(null, -1, 1, null, false);
    }

    @Test
    public void leBound() {
        testBoundedGtExpr(null, 0, 0, null, false);
    }

    @Test
    public void geBound() {
        testBoundedGtExpr(0, null, null, 0, null);
    }

    @Test
    public void gtBound() {
        testBoundedGtExpr(1, null, null, -1, true);
    }

    public static void testBoundedGtExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Boolean result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        GtExpr gtExpr = leftVar.gt(rightVar);
        assertEquals(result != null, gtExpr.hasValue());
        assertEquals(result, gtExpr.getValue());
    }
}
