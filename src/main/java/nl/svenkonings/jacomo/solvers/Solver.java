package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.exceptions.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Solver {
    @NotNull List<Var> solve(@NotNull Model model) throws SolveException;
}
