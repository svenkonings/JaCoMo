package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
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
}
