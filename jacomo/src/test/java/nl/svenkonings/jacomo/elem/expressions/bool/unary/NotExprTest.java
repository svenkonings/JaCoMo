package nl.svenkonings.jacomo.elem.expressions.bool.unary;

import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotExprTest {

    @Test
    public void testFalse() {
        testNotExpr(false, true);
    }

    @Test
    public void testTrue() {
        testNotExpr(true, false);
    }

    @Test
    public void testVar() {
        testNotExpr(null, null);
    }

    public static void testNotExpr(Boolean value, Boolean result) {
        BoolExpr expr = value == null ? BoolVar.variable("value") : BoolExpr.constant(value);
        NotExpr notExpr = expr.not();
        assertEquals(result != null, notExpr.hasValue());
        assertEquals(result, notExpr.getValue());
    }
}
