package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.UpdatableBoolVar;
import nl.svenkonings.jacomo.variables.integer.UpdatableIntVar;
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
    public boolean solveModel(@NotNull Model model) {
        OrToolsVisitor visitor = new OrToolsVisitor();
        model.visitAll(visitor);
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(visitor.getModel());
        switch (status) {
            case UNKNOWN:
            case UNRECOGNIZED:
            case MODEL_INVALID:
            case INFEASIBLE:
                return false;
        }
        visitor.getBoolVars().forEach((name, var) -> {
            Var original = model.getVar(name);
            if (original instanceof UpdatableBoolVar) {
                long value = solver.value(var);
                if (value != 0L && value != 1L) {
                    throw new UnexpectedTypeException("Invalid boolean value returned by: %s", name);
                }
                ((UpdatableBoolVar) original).instantiateValue(value == 1L);
            }
        });
        visitor.getIntVars().forEach((name, var) -> {
            Var original = model.getVar(name);
            if (original instanceof UpdatableIntVar) {
                long value = solver.value(var);
                if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                    throw new UnexpectedTypeException("Invalid integer value returned by: %s", name);
                }
                ((UpdatableIntVar) original).instantiateValue((int) value);
            }
        });
        return true;
    }
}
