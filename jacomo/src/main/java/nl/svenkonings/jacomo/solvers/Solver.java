package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.exceptions.checked.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import org.jetbrains.annotations.NotNull;

/**
 * A solver used to solve {@link Model}s.
 */
public interface Solver {
    /**
     * Attempt to solve the specified model. Returns {@code true} if the model
     * has been solved.
     *
     * @param model the specified model
     * @return {@code true} if the model has been solved, {@code false} otherwise
     */
    boolean solve(@NotNull Model model);
}
