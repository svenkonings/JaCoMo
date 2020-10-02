package nl.svenkonings.jacomo.solver.chocosolver;

import nl.svenkonings.jacomo.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.chocosolver.ChocoSolver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.integer.BoundedIntVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import nl.svenkonings.jacomo.variables.integer.IntVar;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ChocoTest {

    @Test
    void basicSolve() {
        Model model = new Model();
        IntVar var1 = new ConstantIntVar("var1", 3);
        model.addVar(var1);
        IntVar var2 = new BoundedIntVar("var2", 1, 10);
        model.addVar(var2);
        Constraint constraint = new BoolExprConstraint(var1.lt(var2));
        model.addConstraint(constraint);
        Solver solver = new ChocoSolver();
        List<Var> result = solver.solve(model);
        assertEquals(result.size(), 2);
        for (Var var : result) {
            IntVar intVar = (IntVar) var;
            if (intVar.getName().equals("var1")) {
                assertEquals(intVar.getValue(), 3);
            } else if (intVar.getName().equals("var2")) {
                assertTrue(intVar.getValue() > 3);
            } else {
                fail();
            }
        }
    }
}
