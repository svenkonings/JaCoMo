package nl.svenkonings.jacomo.elem.expressions.integer.binary;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubExprTest {

    @Test
    public void valVal() {
        testSubExpr(1, 2, -1);
    }

    @Test
    public void valVar() {
        testSubExpr(3, null, null);
    }

    @Test
    public void varVal() {
        testSubExpr(null, 4, null);
    }

    @Test
    public void varVar() {
        testSubExpr(null, null, null);
    }

    public static void testSubExpr(Integer left, Integer right, Integer result) {
        IntExpr leftExpr = left == null ? IntVar.variable("left") : IntExpr.constant(left);
        IntExpr rightExpr = right == null ? IntVar.variable("right") : IntExpr.constant(right);
        SubExpr subExpr = leftExpr.sub(rightExpr);
        assertEquals(result != null, subExpr.hasLowerBound());
        assertEquals(result, subExpr.getLowerBound());
        assertEquals(result != null, subExpr.hasUpperBound());
        assertEquals(result, subExpr.getUpperBound());
        assertEquals(result != null, subExpr.hasValue());
        assertEquals(result, subExpr.getValue());
    }

    @Test
    public void lowLow() {
        testBoundedSubExpr(5, null, 6, null, null, null, null);
    }

    @Test
    public void lowHigh() {
        testBoundedSubExpr(7, null, null, 8, -1, null, null);
    }

    @Test
    public void lowBoth() {
        testBoundedSubExpr(9, null, 10, 11, -2, null, null);
    }

    @Test
    public void highLow() {
        testBoundedSubExpr(null, 12, 13, null, null, -1, null);
    }

    @Test
    public void highHigh() {
        testBoundedSubExpr(null, 14, null, 15, null, null, null);
    }

    @Test
    public void highBoth() {
        testBoundedSubExpr(null, 16, 17, 18, null, -1, null);
    }

    @Test
    public void bothLow() {
        testBoundedSubExpr(19, 20, 21, null, null, -1, null);
    }

    @Test
    public void bothHigh() {
        testBoundedSubExpr(22, 23, null, 24, -2, null, null);
    }

    @Test
    public void bothBoth() {
        testBoundedSubExpr(25, 26, 27, 28, -3, -1, null);
    }

    public static void testBoundedSubExpr(Integer lLeft, Integer uLeft, Integer lRight, Integer uRight, Integer lowerBound, Integer upperBound, Integer result) {
        IntVar leftVar = IntVar.bounds("left", lLeft, uLeft);
        IntVar rightVar = IntVar.bounds("right", lRight, uRight);
        SubExpr subExpr = leftVar.sub(rightVar);
        assertEquals(lowerBound != null, subExpr.hasLowerBound());
        assertEquals(lowerBound, subExpr.getLowerBound());
        assertEquals(upperBound != null, subExpr.hasUpperBound());
        assertEquals(upperBound, subExpr.getUpperBound());
        assertEquals(result != null, subExpr.hasValue());
        assertEquals(result, subExpr.getValue());
    }
}
