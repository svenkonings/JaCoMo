package nl.svenkonings.jacomo.elem.expressions.bool.binary;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrExprTest {

    @Test
    public void falseFalse() {
        testOrExpr(false, false, false);
    }

    @Test
    public void trueFalse() {
        testOrExpr(true, false, true);
    }

    @Test
    public void falseTrue() {
        testOrExpr(false, true, true);
    }

    @Test
    public void trueTrue() {
        testOrExpr(true, true, true);
    }

    @Test
    public void varFalse() {
        testOrExpr(null, false, null);
    }

    @Test
    public void varTrue() {
        testOrExpr(null, true, true);
    }

    @Test
    public void falseVar() {
        testOrExpr(false, null, null);
    }

    @Test
    public void trueVar() {
        testOrExpr(true, null, true);
    }

    public static void testOrExpr(Boolean left, Boolean right, Boolean result) {
        BoolExpr leftExpr = left == null ? BoolVar.variable("left") : BoolExpr.constant(left);
        BoolExpr rightExpr = right == null ? BoolVar.variable("right") : BoolExpr.constant(right);
        OrExpr orExpr = leftExpr.or(rightExpr);
        assertEquals(result != null, orExpr.hasValue());
        assertEquals(result, orExpr.getValue());
    }
}
