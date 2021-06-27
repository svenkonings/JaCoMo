package nl.svenkonings.jacomo.elem.expressions.integer.binary;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxExprTest {

    @Test
    public void valVal() {
        testMaxExpr(1, 2, 2);
    }

    @Test
    public void valVar() {
        testMaxExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testMaxExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testMaxExpr(null, null, null);
    }

    public static void testMaxExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        MaxExpr maxExpr = leftExpr.max(rightExpr);
        assertEquals(result != null, maxExpr.hasLowerBound());
        assertEquals(result, maxExpr.getLowerBound());
        assertEquals(result != null, maxExpr.hasUpperBound());
        assertEquals(result, maxExpr.getUpperBound());
        assertEquals(result != null, maxExpr.hasValue());
        assertEquals(result, maxExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedMaxExpr(5, null, 6, null, 6, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedMaxExpr(7, null, null, 8, null, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedMaxExpr(9, null, 10, 11, 10, null, null);
    }

    @Test
    public void highLow() {
        testBoundedMaxExpr(null, 12, 13, null, null, null, null);
    }

    @Test
    public void highHigh() {
        testBoundedMaxExpr(null, 14, null, 15, null, 15, null);
    }

    @Test
    public void highBoth() {
        testBoundedMaxExpr(null, 16, 17, 18, null, 18, null);
    }

    @Test
    public void bothLow() {
        testBoundedMaxExpr(19, 20, 21, null, 21, null, null);
    }

    @Test
    public void bothHigh() {
        testBoundedMaxExpr(22, 23, null, 24, null, 24, null);
    }

    @Test
    public void bothBoth() {
        testBoundedMaxExpr(25, 26, 27, 28, 27, 28, null);
    }

    public static void testBoundedMaxExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        MaxExpr maxExpr = leftVar.max(rightVar);
        assertEquals(lowerBound != null, maxExpr.hasLowerBound());
        assertEquals(lowerBound, maxExpr.getLowerBound());
        assertEquals(upperBound != null, maxExpr.hasUpperBound());
        assertEquals(upperBound, maxExpr.getUpperBound());
        assertEquals(result != null, maxExpr.hasValue());
        assertEquals(result, maxExpr.getValue());
    }
}
