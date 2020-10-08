package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import org.jetbrains.annotations.NotNull;

/**
 * Solver implementation using the CP-SAT solver from OR-Tools.
 */
public class OrToolsSolver implements Solver {

    /**
     * Create a new OR-Tools solver.
     */
    public OrToolsSolver() {
    }

    @Override
    public @NotNull VarMap solve(@NotNull Model model) throws SolveException {
        OrToolsVisitor visitor = new OrToolsVisitor();
        model.visitAll(visitor);
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(visitor.getModel());
        switch (status) {
            case UNKNOWN:
            case UNRECOGNIZED:
            case MODEL_INVALID:
            case INFEASIBLE:
                throw new SolveException("Model could not be solved, status: %s", status);
        }
        VarMap result = new VarMap();
        visitor.getBoolVars().forEach((name, var) -> result.addVar(toJaCoMoBoolVar(solver, name, var)));
        visitor.getIntVars().forEach((name, var) -> result.addVar(toJaCoMoIntVar(solver, name, var)));
        return result;
    }

    private Var toJaCoMoBoolVar(CpSolver solver, String name, IntVar var) {
        long value = solver.value(var);
        if (value != 0L && value != 1L) {
            throw new UnexpectedTypeException("Invalid boolean value returned by: %s", name);
        }
        return new ConstantBoolVar(name, value == 1L);
    }

    private Var toJaCoMoIntVar(CpSolver solver, String name, IntVar var) {
        return new ConstantIntVar(name, (int) solver.value(var));
    }
}
