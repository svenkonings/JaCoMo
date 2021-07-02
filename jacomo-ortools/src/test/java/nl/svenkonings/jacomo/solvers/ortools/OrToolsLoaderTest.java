/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrToolsLoaderTest {

    @Test
    public void loaderTest() {
        OrToolsLoader.loadLibrary();
        CpSolver solver = new CpSolver();
        CpModel model = new CpModel();
        CpSolverStatus status = solver.solve(model);
        assertEquals(status, CpSolverStatus.OPTIMAL);
    }
}
