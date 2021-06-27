package nl.svenkonings.jacomo.elem.expressions.integer.binary;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinExprTest {

    @Test
    public void valVal() {
        testMinExpr(1, 2, 1);
    }

    @Test
    public void valVar() {
        testMinExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testMinExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testMinExpr(null, null, null);
    }

    public static void testMinExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        MinExpr minExpr = leftExpr.min(rightExpr);
        assertEquals(result != null, minExpr.hasLowerBound());
        assertEquals(result, minExpr.getLowerBound());
        assertEquals(result != null, minExpr.hasUpperBound());
        assertEquals(result, minExpr.getUpperBound());
        assertEquals(result != null, minExpr.hasValue());
        assertEquals(result, minExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedMinExpr(5, null, 6, null, 5, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedMinExpr(7, null, null, 8, null, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedMinExpr(9, null, 10, 11, 9, null, null);
    }

    @Test
    public void highLow() {
        testBoundedMinExpr(null, 12, 13, null, null, null, null);
    }

    @Test
    public void highHigh() {
        testBoundedMinExpr(null, 14, null, 15, null, 14, null);
    }

    @Test
    public void highBoth() {
        testBoundedMinExpr(null, 16, 17, 18, null, 16, null);
    }

    @Test
    public void bothLow() {
        testBoundedMinExpr(19, 20, 21, null, 19, null, null);
    }

    @Test
    public void bothHigh() {
        testBoundedMinExpr(22, 23, null, 24, null, 23, null);
    }

    @Test
    public void bothBoth() {
        testBoundedMinExpr(25, 26, 27, 28, 25, 26, null);
    }

    public static void testBoundedMinExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        MinExpr minExpr = leftVar.min(rightVar);
        assertEquals(lowerBound != null, minExpr.hasLowerBound());
        assertEquals(lowerBound, minExpr.getLowerBound());
        assertEquals(upperBound != null, minExpr.hasUpperBound());
        assertEquals(upperBound, minExpr.getUpperBound());
        assertEquals(result != null, minExpr.hasValue());
        assertEquals(result, minExpr.getValue());
    }
}
