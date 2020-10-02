package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.exceptions.SolveException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarList;
import org.jetbrains.annotations.NotNull;

public interface Solver {
    @NotNull VarList solve(@NotNull Model model) throws SolveException;
}
