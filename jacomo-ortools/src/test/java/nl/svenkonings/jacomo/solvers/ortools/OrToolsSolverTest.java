package nl.svenkonings.jacomo.solvers.ortools;

import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.SolverTest;

public class OrToolsSolverTest implements SolverTest {

    @Override
    public Solver getSolver() {
        return new OrToolsSolver();
    }
}
