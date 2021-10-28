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
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
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

    private int workers;
    private long timeLimit;

    /**
     * Create a new OR-Tools solver.
     */
    public OrToolsSolver() {
        workers = 0;
        timeLimit = 0;
    }

    /**
     * Returns the number of workers used to search for a solution.
     * A value of 0 (default) means the solver will try to use all cores on the machine.
     * A value of 1 means no parallelism will be used.
     * Parallelism will cause the solving process to be non-deterministic.
     *
     * @return the number of workers.
     */
    @Override
    public int getWorkers() {
        return workers;
    }

    /**
     * Set the number of workers used to search for a solution.
     * A value of 0 (default) means the solver will try to use all cores on the machine.
     * A value of 1 means no parallelism will be used.
     * Parallelism will cause the solving process to be non-deterministic.
     *
     * @param workers the number of workers to use.
     */
    @Override
    public void setWorkers(int workers) {
        if (workers < 0) {
            throw new InvalidInputException("Can't have a negative amount of workers");
        }
        this.workers = workers;
    }

    /**
     * Get the time limit to find a solution.
     * A value of 0 (default) means no time-limit.
     *
     * @return the time limit in milliseconds.
     */
    @Override
    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Set time limit to find a solution.
     * A value of 0 (default) means no time-limit.
     *
     * @param timeLimit the time limit in milliseconds.
     */
    @Override
    public void setTimeLimit(long timeLimit) {
        if (timeLimit < 0) {
            throw new InvalidInputException("Time limit can't be negative");
        }
        this.timeLimit = timeLimit;
    }

    @Override
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        OrToolsVisitor visitor = new OrToolsVisitor();
        model.visit(visitor);
        CpSolver solver = new CpSolver();
        solver.getParameters().setNumSearchWorkers(workers);
        if (timeLimit > 0) solver.getParameters().setMaxTimeInSeconds(timeLimit / 1000.0);
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
