/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.cpsolver;

import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.SolverTest;

public class OrToolsCpSolverTest implements SolverTest {

    @Override
    public Solver getSolver() {
        return new OrToolsCpSolver();
    }
}
