package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrExprTest {

    @Test
    public void falseFalse() {
        testOrExpr(false, false, true, false);
    }

    @Test
    public void trueFalse() {
        testOrExpr(true, false, true, true);
    }

    @Test
    public void falseTrue() {
        testOrExpr(false, true, true, true);
    }

    @Test
    public void trueTrue() {
        testOrExpr(true, true, true, true);
    }

    @Test
    public void varFalse() {
        testOrExpr(null, false, false, null);
    }

    @Test
    public void varTrue() {
        testOrExpr(null, true, true, true);
    }

    @Test
    public void falseVar() {
        testOrExpr(false, null, false, null);
    }

    @Test
    public void trueVar() {
        testOrExpr(true, null, true, true);
    }

    public static void testOrExpr(Boolean left, Boolean right, boolean hasValue, Boolean getValue) {
        BoolExpr leftExpr = left == null ? BoolVar.variable("left") : BoolExpr.constant(left);
        BoolExpr rightExpr = right == null ? BoolVar.variable("right") : BoolExpr.constant(right);
        OrExpr orExpr = leftExpr.or(rightExpr);
        assertEquals(hasValue, orExpr.hasValue());
        assertEquals(getValue, orExpr.getValue());
    }
}
