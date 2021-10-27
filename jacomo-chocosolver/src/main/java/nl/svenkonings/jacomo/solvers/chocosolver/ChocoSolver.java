/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
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

    private int workers;
    private long timeLimit;

    /**
     * Create a new ChocoSolver solver.
     */
    public ChocoSolver() {
        workers = 0;
        timeLimit = 0;
    }

    /**
     * Returns the number of workers used to search for a solution.
     * A value of 0 (default) means the solver will try to use all logical processors on the machine.
     * A value of 1 means no parallelism will be used.
     * Parallelism will cause the solving process to be non-deterministic.
     *
     * @return the number of workers.
     */
    public int getWorkers() {
        return workers;
    }

    /**
     * Set the number of workers used to search for a solution.
     * A value of 0 (default) means the solver will try to use all logical processors on the machine.
     * A value of 1 means no parallelism will be used.
     * Parallelism will cause the solving process to be non-deterministic.
     *
     * @param workers the number of workers to use.
     */
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
     * @return the time limit in milliseconds
     */
    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Set time limit to find a solution.
     * A value of 0 (default) means no time-limit.
     *
     * @param timeLimit the time limit in milliseconds
     */
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        ChocoVisitor visitor;
        if (workers == 1) {
            visitor = new ChocoVisitor();
            model.visit(visitor);
            if (!visitor.getModel().getSolver().solve()) {
                return null;
            }
        } else {
            int threadCount = workers <= 0 ? Runtime.getRuntime().availableProcessors() : workers;
            ParallelPortfolio parallelPortfolio = new ParallelPortfolio();
            List<ChocoVisitor> parallelVisitors = new ArrayList<>(threadCount);
            for (int i = 0; i < threadCount; i++) {
                ChocoVisitor parallelVisitor = new ChocoVisitor();
                model.visit(parallelVisitor);
                org.chocosolver.solver.Model chocoModel = parallelVisitor.getModel();
                if (timeLimit > 0) chocoModel.getSolver().limitTime(timeLimit);
                parallelPortfolio.addModel(chocoModel);
                parallelVisitors.add(parallelVisitor);
            }
            parallelPortfolio.stealNogoodsOnRestarts();
            if (!parallelPortfolio.solve()) {
                return null;
            }
            org.chocosolver.solver.Model bestModel = parallelPortfolio.getBestModel();
            visitor = parallelVisitors.stream()
                    .filter(v -> v.getModel() == bestModel)
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("No visitor matching the solved model was found"));
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
