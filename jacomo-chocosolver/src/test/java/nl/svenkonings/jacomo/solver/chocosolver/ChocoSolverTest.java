package nl.svenkonings.jacomo.solver.chocosolver;

import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.chocosolver.ChocoSolver;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ChocoSolverTest {

    @Test
    void basicSolve() throws SolveException {
        Model model = new Model();
        IntVar var1 = model.intVar("var1", 3);
        IntVar var2 = model.intVar("var2", 1, 10);
        Constraint constraint = model.constraint(var1.lt(var2));

        Solver solver = new ChocoSolver();
        VarMap result = solver.solve(model);

        var1 = (IntVar) result.getVar("var1");
        assertEquals(var1.getValue(), 3);
        var2 = (IntVar) result.getVar("var2");
        assertTrue(var2.getValue() > 3);
    }
}
