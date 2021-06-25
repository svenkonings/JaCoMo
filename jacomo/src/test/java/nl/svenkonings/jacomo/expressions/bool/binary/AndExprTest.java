package nl.svenkonings.jacomo.expressions.bool.binary;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AndExprTest {

    @Test
    public void falseFalse() {
        testAndExpr(false, false, true, false);
    }

    @Test
    public void trueFalse() {
        testAndExpr(true, false, true, false);
    }

    @Test
    public void falseTrue() {
        testAndExpr(false, true, true, false);
    }

    @Test
    public void trueTrue() {
        testAndExpr(true, true, true, true);
    }

    @Test
    public void varFalse() {
        testAndExpr(null, false, true, false);
    }

    @Test
    public void varTrue() {
        testAndExpr(null, true, false, null);
    }

    @Test
    public void falseVar() {
        testAndExpr(false, null, true, false);
    }

    @Test
    public void trueVar() {
        testAndExpr(true, null, false, null);
    }

    public static void testAndExpr(Boolean left, Boolean right, boolean hasValue, Boolean getValue) {
        BoolExpr leftExpr = left == null ? BoolVar.variable("left") : BoolExpr.constant(left);
        BoolExpr rightExpr = right == null ? BoolVar.variable("right") : BoolExpr.constant(right);
        AndExpr andExpr = leftExpr.and(rightExpr);
        assertEquals(hasValue, andExpr.hasValue());
        assertEquals(getValue, andExpr.getValue());
    }
}
