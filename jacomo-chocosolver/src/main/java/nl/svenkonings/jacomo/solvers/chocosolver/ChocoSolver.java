package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.exceptions.SolveException;
import nl.svenkonings.jacomo.exceptions.UnknownTypeException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.variables.integer.ConstantIntVar;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChocoSolver implements Solver {
    @Override
    public @NotNull List<Var> solve(@NotNull Model model) throws SolveException {
        ChocoVisitor visitor = new ChocoVisitor();
        model.visitAll(visitor);
        if (!visitor.getModel().getSolver().solve()) {
            throw new SolveException("Could not find a solution");
        }
        return visitor.getVars().entrySet().stream().map(this::entryToVar).collect(Collectors.toList());
    }

    private Var entryToVar(Map.Entry<String, Variable> entry) {
        String name = entry.getKey();
        Variable solvedVar = entry.getValue();
        if (solvedVar instanceof BoolVar) {
            BoolVar boolVar = (BoolVar) solvedVar;
            boolean result = boolVar.getValue() == 1;
            return new ConstantBoolVar(name, result);
        } else if (solvedVar instanceof IntVar) {
            IntVar intVar = (IntVar) solvedVar;
            return new ConstantIntVar(name, intVar.getValue());
        } else {
            throw new UnknownTypeException("Unknown type: %s", solvedVar.getClass().getCanonicalName());
        }
    }
}
