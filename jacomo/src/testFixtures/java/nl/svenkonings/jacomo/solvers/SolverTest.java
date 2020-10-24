package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.variables.bool.BoolVar;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantConditions")
public interface SolverTest {
    Solver getSolver();

    @Test
    default void solveEq() {
        Model model = new Model();
        IntVar var1 = model.intVar(5, 10);
        IntVar var2 = model.intVar(0, 7);
        model.constraint(var1.eq(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(5 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 7);
        assertEquals(var1.getValue(), var2.getValue());
    }

    @Test
    default void solveNe() {
        Model model = new Model();
        IntVar var1 = model.intVar(1, 2);
        IntVar var2 = model.intVar(1, 2);
        model.constraint(var1.ne(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(1 <= var1.getValue() && var1.getValue() <= 2);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 2);
        assertNotEquals(var1.getValue(), var2.getValue());
    }

    @Test
    default void solveLt() {
        Model model = new Model();
        IntVar var1 = model.intVar(5);
        IntVar var2 = model.intVar(4, 7);
        model.constraint(var1.lt(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertEquals(5, var1.getValue());
        assertTrue(4 <= var2.getValue() && var2.getValue() <= 7);
        assertTrue(var1.getValue() < var2.getValue());
    }

    @Test
    default void solveLe() {
        Model model = new Model();
        IntVar var1 = model.intVar(5);
        IntVar var2 = model.intVar(0, 5);
        model.constraint(var1.le(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertEquals(5, var1.getValue());
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 5);
        assertTrue(var1.getValue() <= var2.getValue());
    }

    @Test
    default void solveGt() {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        model.constraint(var1.gt(IntExpr.constant(9)));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(var1.getValue() > 9);
    }

    @Test
    default void solveGe() {
        Model model = new Model();
        IntVar var1 = model.intVar(-5);
        IntVar var2 = model.intVar(-5, 10);
        model.constraint(var1.ge(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertEquals(-5, var1.getValue());
        assertTrue(-5 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(var1.getValue() >= var2.getValue());
    }

    @Test
    default void solveAnd() {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        IntVar var2 = model.intVar(0, 10);
        IntVar var3 = model.intVar(0, 10);
        IntVar var4 = model.intVar(0, 10);
        model.constraint(var1.gt(var2).and(var2.gt(var3).and(var3.gt(var4))));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 10);
        assertTrue(0 <= var4.getValue() && var4.getValue() <= 10);
        assertTrue(var1.getValue() > var2.getValue());
        assertTrue(var2.getValue() > var3.getValue());
        assertTrue(var3.getValue() > var4.getValue());
    }

    @Test
    default void solveOr() {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 1);
        IntVar var2 = model.intVar(0, 1);
        IntVar var3 = model.intVar(0, 1);
        IntVar var4 = model.intVar(0, 1);
        model.constraint(var1.gt(var2).or(var2.gt(var3).or(var3.gt(var4))));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 1);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 1);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 1);
        assertTrue(0 <= var4.getValue() && var4.getValue() <= 1);
        assertTrue(var1.getValue() > var2.getValue() || var2.getValue() > var3.getValue() || var3.getValue() > var4.getValue());
    }

    @Test
    default void solveNot() {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        IntVar var2 = model.intVar(0, 10);
        IntVar var3 = model.intVar(0, 10);
        model.constraint(var1.gt(var2).not());
        model.constraint(var3.gt(var1).or(var3.gt(var2)).not());
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 10);
        assertFalse(var1.getValue() > var2.getValue());
        assertFalse(var3.getValue() > var1.getValue() || var3.getValue() > var2.getValue());
    }

    @Test
    default void solveBoolConstant() {
        Model model = new Model();
        BoolVar var1 = model.boolVar();
        BoolVar var2 = model.boolVar(IntExpr.constant(5).gt(IntExpr.constant(4)));
        BoolVar var3 = model.boolVar(false);
        model.constraint(var1.and(var3.or(BoolExpr.constant(true))));
        model.constraint(var1.or(BoolExpr.constant(false)));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(var1.getValue());
        assertTrue(var2.getValue());
        assertFalse(var3.getValue());
    }

    @Test
    default void solveAdd() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 5).add(model.intVar(1, 4).add(model.intVar(1, 3))));
        IntVar var2 = model.intVar(1, 3);
        IntVar var3 = model.intVar(var1.add(var2).add(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(3 <= var1.getValue() && var1.getValue() <= 12);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 3);
        assertTrue(5 <= var3.getValue() && var3.getValue() <= 18);
    }

    @Test
    default void solveSub() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 5).sub(model.intVar(1, 4).sub(model.intVar(1, 3))));
        IntVar var2 = model.intVar(1, 3);
        IntVar var3 = model.intVar(var1.sub(var2).sub(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(-7 <= var1.getValue() && var1.getValue() <= 3);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 3);
        assertTrue(-13 <= var3.getValue() && var3.getValue() <= 1);
    }

    @Test
    default void solveMul() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 5).mul(model.intVar(1, 4).mul(model.intVar(1, 3))));
        IntVar var2 = model.intVar(1, 3);
        IntVar var3 = model.intVar(var1.mul(var2).mul(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(1 <= var1.getValue() && var1.getValue() <= 60);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 3);
        assertTrue(1 <= var3.getValue() && var3.getValue() <= 540);
    }

    @Test
    default void solveDiv() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(40, 100).div(model.intVar(4, 5)).div(model.intVar(1, 2)));
        IntVar var2 = model.intVar(1, 2);
        IntVar var3 = model.intVar(var1.div(var2.div(var2)));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(4 <= var1.getValue() && var1.getValue() <= 25);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 2);
        assertTrue(1 <= var3.getValue() && var3.getValue() <= 25);
    }

    @Test
    default void solveMax() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 5).max(model.intVar(1, 4).max(model.intVar(1, 3))));
        IntVar var2 = model.intVar(1, 3);
        IntVar var3 = model.intVar(var1.max(var2).max(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(1 <= var1.getValue() && var1.getValue() <= 5);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 3);
        assertTrue(1 <= var3.getValue() && var3.getValue() <= 5);
    }

    @Test
    default void solveMin() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 5).min(model.intVar(1, 4).min(model.intVar(1, 6))));
        IntVar var2 = model.intVar(1, 3);
        IntVar var3 = model.intVar(var1.min(var2).min(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(1 <= var1.getValue() && var1.getValue() <= 4);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 3);
        assertTrue(1 <= var3.getValue() && var3.getValue() <= 3);
    }

    @Test
    default void solveZeroDiv() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(5).div(model.intVar(0)));
        boolean result = getSolver().solve(model);

        assertFalse(result);
    }

    @Test
    default void solveReusedConstraint() {
        Model model = new Model();
        BoolVar var1 = model.boolVar();
        BoolVar var2 = model.boolVar();
        BoolVar var3 = model.boolVar(var1.and(var2));
        model.constraint(var1.and(var2));
        boolean result = getSolver().solve(model);

        assertTrue(result);
        assertTrue(var1.getValue());
        assertTrue(var2.getValue());
        assertTrue(var3.getValue());
    }
}
