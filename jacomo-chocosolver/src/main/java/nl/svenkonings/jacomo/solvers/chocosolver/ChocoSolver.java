package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.jetbrains.annotations.NotNull;

/**
 * Solver implementation using ChocoSolver.
 */
public class ChocoSolver implements Solver {

    /**
     * Create a new ChocoSolver solver.
     */
    public ChocoSolver() {
    }

    @Override
    public @NotNull VarMap solve(@NotNull Model model) throws SolveException {
        ChocoVisitor visitor = new ChocoVisitor();
        model.visitAll(visitor);
        if (!visitor.getModel().getSolver().solve()) {
            throw new SolveException("Could not find a solution");
        }
        VarMap result = new VarMap();
        visitor.getBoolVars().forEach((name, var) -> result.addVar(toJaCoMoBoolVar(name, var)));
        visitor.getIntVars().forEach((name, var) -> result.addVar(toJaCoMoIntVar(name, var)));
        return result;
    }

    private Var toJaCoMoBoolVar(String name, BoolVar var) {
        boolean result = var.getValue() == 1;
        return new ConstantBoolVar(name, result);
    }

    private Var toJaCoMoIntVar(String name, IntVar var) {
        return new ConstantIntVar(name, var.getValue());
    }
}
