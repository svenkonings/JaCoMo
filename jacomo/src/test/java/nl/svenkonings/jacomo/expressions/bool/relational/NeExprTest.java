package nl.svenkonings.jacomo.expressions.bool.relational;

import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NeExprTest {

    @Test
    public void lowLow() {
        testNeExpr(-1, -1, true, false);
    }

    @Test
    public void highLow() {
        testNeExpr(1, -1, true, true);
    }

    @Test
    public void lowHigh() {
        testNeExpr(-1, 1, true, true);
    }

    @Test
    public void highHigh() {
        testNeExpr(1, 1, true, false);
    }

    @Test
    public void varLow() {
        testNeExpr(null, -1, false, null);
    }

    @Test
    public void varHigh() {
        testNeExpr(null, 1, false, null);
    }

    @Test
    public void lowVar() {
        testNeExpr(-1, null, false, null);
    }

    @Test
    public void highVar() {
        testNeExpr(1, null, false, null);
    }

    public static void testNeExpr(Integer left, Integer right, boolean hasValue, Boolean getValue) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        NeExpr neExpr = leftExpr.ne(rightExpr);
        assertEquals(hasValue, neExpr.hasValue());
        assertEquals(getValue, neExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedNeExpr(null, -1, 1, null, true, true);
    }

    @Test
    public void leBound() {
        testBoundedNeExpr(null, 0, 0, null, false, null);
    }

    @Test
    public void geBound() {
        testBoundedNeExpr(0, null, null, 0, false, null);
    }

    @Test
    public void gtBound() {
        testBoundedNeExpr(1, null, null, -1, true, true);
    }

    public static void testBoundedNeExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, boolean hasValue, Boolean getValue) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        NeExpr neExpr = leftVar.ne(rightVar);
        assertEquals(hasValue, neExpr.hasValue());
        assertEquals(getValue, neExpr.getValue());
    }
}
