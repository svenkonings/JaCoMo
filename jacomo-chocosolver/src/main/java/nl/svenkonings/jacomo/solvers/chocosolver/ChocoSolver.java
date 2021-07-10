/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.UnexpectedTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        ChocoVisitor visitor = new ChocoVisitor();
        model.visit(visitor);
        if (!visitor.getModel().getSolver().solve()) {
            return null;
        }
        VarMap result = new VarMap();
        visitor.getBoolVars().forEach((name, var) -> {
            if (var.getValue() != 0 && var.getValue() != 1) {
                throw new UnexpectedTypeException("Invalid boolean value returned by: %s", name);
            }
            result.add(BoolVar.constant(name, var.getValue() == 1));
        });
        visitor.getIntVars().forEach((name, var) -> result.add(IntVar.constant(name, var.getValue())));
        return result;
    }
}
