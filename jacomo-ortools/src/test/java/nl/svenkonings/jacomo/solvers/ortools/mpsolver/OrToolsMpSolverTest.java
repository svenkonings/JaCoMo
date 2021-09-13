/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.InvalidInputException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.solvers.SolverTest;
import nl.svenkonings.jacomo.solvers.ortools.cpsolver.OrToolsCpSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrToolsMpSolverTest implements SolverTest {

    @Override
    public Solver getSolver() {
        return new OrToolsMpSolver();
    }

    @Test
    @Override
    public void solveZeroDiv() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(5).div(IntExpr.constant(0)));
        assertThrows(InvalidInputException.class, () -> getSolver().solveAndUpdate(model));
    }

    @Test
    @Override
    public void solveMul() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(1, 2).mul(IntExpr.constant(5).mul(IntExpr.constant(3))));
        boolean result = getSolver().solveAndUpdate(model);

        assertTrue(result);
        assertTrue(15 == var1.getValue() || var1.getValue() == 30);
    }

    @Test
    @Override
    public void solveDiv() {
        Model model = new Model();
        IntVar var1 = model.intVar(model.intVar(40, 100).div(IntExpr.constant(5)).div(IntExpr.constant(2)));
        boolean result = getSolver().solveAndUpdate(model);

        assertTrue(result);
        assertTrue(4 <= var1.getValue() && var1.getValue() <= 10);
    }
}
