/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrToolsLoaderTest {

    @Test
    public void loaderCpTest() {
        OrToolsLoader.loadLibrary();
        CpSolver solver = new CpSolver();
        CpModel model = new CpModel();
        CpSolverStatus status = solver.solve(model);
        assertEquals(status, CpSolverStatus.OPTIMAL);
    }

    @Test
    public void loaderMpTest() {
        OrToolsLoader.loadLibrary();
        MPSolver mpSolver = MPSolver.createSolver("GLOP");
        MPSolver.ResultStatus resultStatus = mpSolver.solve();
        assertEquals(resultStatus, MPSolver.ResultStatus.OPTIMAL);
    }
}
