package nl.svenkonings.jacomo.solver.chocosolver;

import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.chocosolver.ChocoSolver;
import nl.svenkonings.jacomo.variables.integer.BoundedIntVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ChocoTest {

    @Test
    void basicSolve() throws SolveException {
        Model model = new Model();
        IntVar var1 = new ConstantIntVar("var1", 3);
        model.addVar(var1);
        IntVar var2 = new BoundedIntVar("var2", 1, 10);
        model.addVar(var2);
        Constraint constraint = new BoolExprConstraint(var1.lt(var2));
        model.addConstraint(constraint);
        Solver solver = new ChocoSolver();
        VarMap result = solver.solve(model);
        var1 = (IntVar) result.getVar("var1");
        assertEquals(var1.getValue(), 3);
        var2 = (IntVar) result.getVar("var2");
        assertTrue(var2.getValue() > 3);
    }
}
