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
     * Attempt to solve the specified model. Returns a
     * {@link VarMap} containing the resolved variables.
     * All variables present in the model should be included in the map.
     *
     * @param model the specified model
     * @return A {@link VarMap} containing the resolved variables
     * @throws SolveException if the model could not be solved
     */
    @NotNull VarMap solve(@NotNull Model model) throws SolveException;
}
