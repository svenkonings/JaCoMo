package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.variables.bool.BoolVar;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantConditions")
public interface SolverTest {
    Solver getSolver();

    @Test
    default void solveEq() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(5, 10);
        IntVar var2 = model.intVar(0, 7);
        model.constraint(var1.eq(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        assertTrue(5 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 7);
        assertEquals(var1.getValue(), var2.getValue());
    }

    @Test
    default void solveNe() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(1, 2);
        IntVar var2 = model.intVar(1, 2);
        model.constraint(var1.ne(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        assertTrue(1 <= var1.getValue() && var1.getValue() <= 2);
        assertTrue(1 <= var2.getValue() && var2.getValue() <= 2);
        assertNotEquals(var1.getValue(), var2.getValue());
    }

    @Test
    default void solveLt() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(5);
        IntVar var2 = model.intVar(4, 7);
        model.constraint(var1.lt(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        assertEquals(var1.getValue(), 5);
        assertTrue(4 <= var2.getValue() && var2.getValue() <= 7);
        assertTrue(var1.getValue() < var2.getValue());
    }

    @Test
    default void solveLe() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(5);
        IntVar var2 = model.intVar(0, 5);
        model.constraint(var1.le(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        assertEquals(var1.getValue(), 5);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 5);
        assertTrue(var1.getValue() <= var2.getValue());
    }

    @Test
    default void solveGt() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        model.constraint(var1.gt(IntExpr.constant(9)));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(var1.getValue() > 9);
    }

    @Test
    default void solveGe() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(-5);
        IntVar var2 = model.intVar(-5, 10);
        model.constraint(var1.ge(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        assertEquals(var1.getValue(), -5);
        assertTrue(-5 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(var1.getValue() >= var2.getValue());
    }

    @Test
    default void solveAnd() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        IntVar var2 = model.intVar(0, 10);
        IntVar var3 = model.intVar(0, 10);
        IntVar var4 = model.intVar(0, 10);
        model.constraint(var1.gt(var2).and(var2.gt(var3).and(var3.gt(var4))));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        var4 = (IntVar) result.getVar(var4.getName());
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 10);
        assertTrue(0 <= var4.getValue() && var4.getValue() <= 10);
        assertTrue(var1.getValue() > var2.getValue());
        assertTrue(var2.getValue() > var3.getValue());
        assertTrue(var3.getValue() > var4.getValue());
    }

    @Test
    default void solveOr() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 1);
        IntVar var2 = model.intVar(0, 1);
        IntVar var3 = model.intVar(0, 1);
        IntVar var4 = model.intVar(0, 1);
        model.constraint(var1.gt(var2).or(var2.gt(var3).or(var3.gt(var4))));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        var4 = (IntVar) result.getVar(var4.getName());
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 1);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 1);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 1);
        assertTrue(0 <= var4.getValue() && var4.getValue() <= 1);
        assertTrue(var1.getValue() > var2.getValue() || var2.getValue() > var3.getValue() || var3.getValue() > var4.getValue());
    }

    @Test
    default void solveNot() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(0, 10);
        IntVar var2 = model.intVar(0, 10);
        IntVar var3 = model.intVar(0, 10);
        model.constraint(var1.gt(var2).not());
        model.constraint(var3.gt(var1).or(var3.gt(var2)).not());
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertTrue(0 <= var1.getValue() && var1.getValue() <= 10);
        assertTrue(0 <= var2.getValue() && var2.getValue() <= 10);
        assertTrue(0 <= var3.getValue() && var3.getValue() <= 10);
        assertFalse(var1.getValue() > var2.getValue());
        assertFalse(var3.getValue() > var1.getValue() || var3.getValue() > var2.getValue());
    }

    @Test
    default void solveBoolConstant() throws SolveException {
        Model model = new Model();
        BoolVar var1 = model.boolVar();
        BoolVar var2 = model.boolVar(IntExpr.constant(5).gt(IntExpr.constant(4)));
        BoolVar var3 = model.boolVar(false);
        model.constraint(var1.and(var3.or(BoolExpr.constant(true))));
        model.constraint(var1.or(BoolExpr.constant(false)));
        VarMap result = getSolver().solve(model);

        var1 = (BoolVar) result.getVar(var1.getName());
        var2 = (BoolVar) result.getVar(var2.getName());
        var3 = (BoolVar) result.getVar(var3.getName());
        assertTrue(var1.getValue());
        assertTrue(var2.getValue());
        assertFalse(var3.getValue());
    }

    @Test
    default void solveAdd() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(5).add(IntExpr.constant(4).add(IntExpr.constant(3))));
        IntVar var2 = model.intVar(3);
        IntVar var3 = model.intVar(var1.add(var2).add(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 12);
        assertEquals(var2.getValue(), 3);
        assertEquals(var3.getValue(), 18);
    }

    @Test
    default void solveSub() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(5).sub(IntExpr.constant(4).sub(IntExpr.constant(3))));
        IntVar var2 = model.intVar(3);
        IntVar var3 = model.intVar(var1.sub(var2).sub(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 4);
        assertEquals(var2.getValue(), 3);
        assertEquals(var3.getValue(), -2);
    }

    @Test
    default void solveMul() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(5).mul(IntExpr.constant(4).mul(IntExpr.constant(3))));
        IntVar var2 = model.intVar(3);
        IntVar var3 = model.intVar(var1.mul(var2).mul(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 60);
        assertEquals(var2.getValue(), 3);
        assertEquals(var3.getValue(), 540);
    }

    @Test
    default void solveDiv() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(100).div(IntExpr.constant(4)).div(IntExpr.constant(5)));
        IntVar var2 = model.intVar(25);
        IntVar var3 = model.intVar(var2.div(var1.div(var1)));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 5);
        assertEquals(var2.getValue(), 25);
        assertEquals(var3.getValue(), 25);
    }

    @Test
    default void solveMax() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(5).max(IntExpr.constant(4).max(IntExpr.constant(3))));
        IntVar var2 = model.intVar(3);
        IntVar var3 = model.intVar(var1.max(var2).max(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 5);
        assertEquals(var2.getValue(), 3);
        assertEquals(var3.getValue(), 5);
    }

    @Test
    default void solveMin() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar(IntExpr.constant(5).min(IntExpr.constant(4).min(IntExpr.constant(6))));
        IntVar var2 = model.intVar(3);
        IntVar var3 = model.intVar(var1.min(var2).min(var2));
        VarMap result = getSolver().solve(model);

        var1 = (IntVar) result.getVar(var1.getName());
        var2 = (IntVar) result.getVar(var2.getName());
        var3 = (IntVar) result.getVar(var3.getName());
        assertEquals(var1.getValue(), 4);
        assertEquals(var2.getValue(), 3);
        assertEquals(var3.getValue(), 3);
    }
}
