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
import org.chocosolver.solver.ParallelPortfolio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Solver implementation using ChocoSolver.
 */
public class ChocoSolver implements Solver {

    private boolean parallel;

    /**
     * Create a new ChocoSolver solver.
     */
    public ChocoSolver() {
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
        ChocoVisitor visitor;
        if (parallel) {
            int threadCount = Runtime.getRuntime().availableProcessors();
            ParallelPortfolio parallelPortfolio = new ParallelPortfolio();
            List<ChocoVisitor> parallelVisitors = new ArrayList<>(threadCount);
            for (int i = 0; i < threadCount; i++) {
                ChocoVisitor parallelVisitor = new ChocoVisitor();
                model.visit(parallelVisitor);
                parallelPortfolio.addModel(parallelVisitor.getModel());
                parallelVisitors.add(parallelVisitor);
            }
            if (!parallelPortfolio.solve()) {
                return null;
            }
            org.chocosolver.solver.Model bestModel = parallelPortfolio.getBestModel();
            visitor = parallelVisitors.stream()
                    .filter(v -> v.getModel() == bestModel)
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("No visitor matching the solved model was found"));
        } else {
            visitor = new ChocoVisitor();
            model.visit(visitor);
            if (!visitor.getModel().getSolver().solve()) {
                return null;
            }
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
