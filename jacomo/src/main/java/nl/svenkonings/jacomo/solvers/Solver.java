package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.model.Checker;
import nl.svenkonings.jacomo.exceptions.unchecked.CheckException;
import nl.svenkonings.jacomo.model.Model;
import org.jetbrains.annotations.NotNull;

/**
 * A solver used to solve {@link Model}s.
 */
public interface Solver {
    /**
     * Check, optimize and attempt to solve the specified model. Returns
     * {@code true} if the model has been solved.
     *
     * @param model the specified model
     * @return {@code true} if the model has been solved, {@code false} otherwise
     * @throws CheckException if one og the checks fails
     */
    default boolean solve(@NotNull Model model) throws CheckException {
        return solveModel(new Checker().check(model));
    }

    /**
     * Attempt to solve the specified model. Returns {@code true} if the model
     * has been solved.
     *
     * @param model the specified model
     * @return {@code true} if the model has been solved, {@code false} otherwise
     */
    boolean solveModel(@NotNull Model model);
}
