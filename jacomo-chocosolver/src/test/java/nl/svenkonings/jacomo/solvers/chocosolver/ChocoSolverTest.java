package nl.svenkonings.jacomo.solvers.chocosolver;

import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.SolverTest;

public class ChocoSolverTest implements SolverTest {

    @Override
    public Solver getSolver() {
        return new ChocoSolver();
    }
}
