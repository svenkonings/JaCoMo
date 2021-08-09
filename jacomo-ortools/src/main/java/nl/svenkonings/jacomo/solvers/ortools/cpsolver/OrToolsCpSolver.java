/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.cpsolver;

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
public class OrToolsCpSolver implements Solver {

    /**
     * Create a new OR-Tools solver.
     */
    public OrToolsCpSolver() {
    }

    @Override
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        OrToolsCpVisitor visitor = new OrToolsCpVisitor();
        model.visit(visitor);
        CpSolver solver = new CpSolver();
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
