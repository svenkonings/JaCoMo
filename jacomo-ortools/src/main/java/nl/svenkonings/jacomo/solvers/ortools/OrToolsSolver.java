/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Solver implementation using the CP-SAT solver from OR-Tools.
 */
public class OrToolsSolver implements Solver {

    private boolean parallel;

    /**
     * Create a new OR-Tools solver.
     */
    public OrToolsSolver() {
        parallel = true;
    }

    /**
     * Returns whether the solving process is parallelized or not.
     * If enabled the solving process will use the number of logical cores.
     * Parallel solving causes the solving process to be non-deterministic.
     *
     * @return {@code true} if the solving process is parallelized.
     */
    public boolean isParallel() {
        return parallel;
    }

    /**
     * Set whether the solving process is parallelized or not.
     * If enabled the solving process will use the number of logical cores.
     * Parallel solving causes the solving process to be non-deterministic.
     *
     * @param parallel {@code true} if the solving process should be parallelized.
     */
    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    @Override
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        OrToolsVisitor visitor = new OrToolsVisitor();
        model.visit(visitor);
        CpSolver solver = new CpSolver();
        solver.getParameters().setNumSearchWorkers(parallel ? 0 : 1);
        CpSolverStatus status = solver.solve(visitor.getModel());
        switch (status) {
            case UNKNOWN:
            case UNRECOGNIZED:
            case MODEL_INVALID:
            case INFEASIBLE:
                return null;
        }
        VarMap result = new VarMap();
        visitor.getBoolVars().forEach((name, var) -> {
            long value = solver.value(var);
            if (value != 0L && value != 1L) {
                throw new UnexpectedTypeException("Invalid boolean value returned by: %s", name);
            }
            result.add(BoolVar.constant(name, value == 1L));
        });
        visitor.getIntVars().forEach((name, var) -> {
            long value = solver.value(var);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throw new UnexpectedTypeException("Invalid integer value returned by: %s", name);
            }
            result.add(IntVar.constant(name, (int) value));
        });
        return result;
    }
}
