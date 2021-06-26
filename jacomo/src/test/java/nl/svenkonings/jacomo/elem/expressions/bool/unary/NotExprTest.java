package nl.svenkonings.jacomo.elem.expressions.bool.unary;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotExprTest {

    @Test
    public void testFalse() {
        testNotExpr(false, true, true);
    }

    @Test
    public void testTrue() {
        testNotExpr(true, true, false);
    }

    @Test
    public void testVar() {
        testNotExpr(null, false, null);
    }

    public static void testNotExpr(Boolean value, boolean hasValue, Boolean getValue) {
        BoolExpr expr = value == null ? BoolVar.variable("value") : BoolExpr.constant(value);
        NotExpr notExpr = expr.not();
        assertEquals(hasValue, notExpr.hasValue());
        assertEquals(getValue, notExpr.getValue());
    }
}
