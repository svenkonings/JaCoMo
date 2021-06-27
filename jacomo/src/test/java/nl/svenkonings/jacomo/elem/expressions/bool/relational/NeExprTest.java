package nl.svenkonings.jacomo.elem.expressions.bool.relational;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NeExprTest {

    @Test
    public void lowLow() {
        testNeExpr(-1, -1, false);
    }

    @Test
    public void highLow() {
        testNeExpr(1, -1, true);
    }

    @Test
    public void lowHigh() {
        testNeExpr(-1, 1, true);
    }

    @Test
    public void highHigh() {
        testNeExpr(1, 1, false);
    }

    @Test
    public void varLow() {
        testNeExpr(null, -1, null);
    }

    @Test
    public void varHigh() {
        testNeExpr(null, 1, null);
    }

    @Test
    public void lowVar() {
        testNeExpr(-1, null, null);
    }

    @Test
    public void highVar() {
        testNeExpr(1, null, null);
    }

    public static void testNeExpr(Integer left, Integer right, Boolean result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        NeExpr neExpr = leftExpr.ne(rightExpr);
        assertEquals(result != null, neExpr.hasValue());
        assertEquals(result, neExpr.getValue());
    }

    @Test
    public void ltBound() {
        testBoundedNeExpr(null, -1, 1, null, true);
    }

    @Test
    public void leBound() {
        testBoundedNeExpr(null, 0, 0, null, null);
    }

    @Test
    public void geBound() {
        testBoundedNeExpr(0, null, null, 0, null);
    }

    @Test
    public void gtBound() {
        testBoundedNeExpr(1, null, null, -1, true);
    }

    public static void testBoundedNeExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Boolean result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        NeExpr neExpr = leftVar.ne(rightVar);
        assertEquals(result != null, neExpr.hasValue());
        assertEquals(result, neExpr.getValue());
    }
}
