package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnknownTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
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
        visitor.getVars().forEach((name, variable) -> result.addVar(chocoToJaCoMo(name, variable)));
        return result;
    }

    private Var chocoToJaCoMo(String name, Variable variable) {
        if (variable instanceof BoolVar) {
            BoolVar boolVar = (BoolVar) variable;
            boolean result = boolVar.getValue() == 1;
            return new ConstantBoolVar(name, result);
        } else if (variable instanceof IntVar) {
            IntVar intVar = (IntVar) variable;
            return new ConstantIntVar(name, intVar.getValue());
        } else {
            throw new UnknownTypeException("Unknown type: %s", variable.getClass().getCanonicalName());
        }
    }
}
