package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.elem.variables.bool.UpdatableBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.UpdatableIntVar;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.solvers.Solver;
import org.jetbrains.annotations.NotNull;

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
    public boolean solveModel(@NotNull Model model) {
        ChocoVisitor visitor = new ChocoVisitor();
        model.visit(visitor);
        if (!visitor.getModel().getSolver().solve()) {
            return false;
        }
        visitor.getBoolVars().forEach((name, var) -> {
            Var original = model.getVar(name);
            if (original instanceof UpdatableBoolVar) {
                ((UpdatableBoolVar) original).instantiateValue(var.getValue() == 1);
            }
        });
        visitor.getIntVars().forEach((name, var) -> {
            Var original = model.getVar(name);
            if (original instanceof UpdatableIntVar) {
                ((UpdatableIntVar) original).instantiateValue(var.getValue());
            }
        });
        return true;
    }
}
