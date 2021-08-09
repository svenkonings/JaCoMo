/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import nl.svenkonings.jacomo.solvers.ortools.OrToolsLoader;

public class Test {
    private static final double MinValue = -1.7976931348623157E+308;
    private static final double MaxValue = 1.7976931348623157E+308;;

    public static void main(String[] args) {
        OrToolsLoader.loadLibrary();
        MPSolver mpSolver = MPSolver.createSolver("SCIP");

        // Create variables
        MPVariable x = mpSolver.makeNumVar(2, 2, "x");
        MPVariable y = mpSolver.makeNumVar(3, 3, "y");

        MPVariable gt = mpSolver.makeBoolVar("gt");
        MPVariable le = mpSolver.makeBoolVar("le");

        MPConstraint inverse = mpSolver.makeConstraint(1.0, 1.0);
        inverse.setCoefficient(gt, 1.0);
        inverse.setCoefficient(le, 1.0);

        MPConstraint positiveConstraint = mpSolver.makeConstraint(Integer.MIN_VALUE / 1000, 0.0);
        positiveConstraint.setCoefficient(x, 1.0);
        positiveConstraint.setCoefficient(y, -1.0);
        positiveConstraint.setCoefficient(gt, Integer.MIN_VALUE / 1000);

        MPConstraint negativeConstraint = mpSolver.makeConstraint(Integer.MIN_VALUE / 1000, 0.0);
        negativeConstraint.setCoefficient(y, 1.0);
        negativeConstraint.setCoefficient(x, -1.0);
        negativeConstraint.setCoefficient(le, Integer.MIN_VALUE / 1000);

//        // Constraint
//        // x = y / 2
//        MPConstraint div = mpSolver.makeConstraint(0, 0, "div");
//        div.setCoefficient(y, 1);
//        div.setCoefficient(x, -3);

        // z = 2 * (y - x)
//        MPVariable z = mpSolver.makeNumVar(0, 10, "z");
//        MPConstraint zDiv = mpSolver.makeConstraint(0, 0, "zDiv");
//        zDiv.setCoefficient(y, 1);
//        zDiv.setCoefficient(x, -2);

//        MPObjective objective = mpSolver.objective();
//        objective.setCoefficient(x, 1);
//        objective.setCoefficient(y, 1);
//        objective.setMinimization();

        MPSolver.ResultStatus resultStatus = mpSolver.solve();
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution:");
//            System.out.println("Objective value = " + objective.value());
            System.out.println("x = " + x.solutionValue());
            System.out.println("y = " + y.solutionValue());
            System.out.println("gt = " + gt.solutionValue());
            System.out.println("le = " + le.solutionValue());
        } else {
            System.err.println("The problem does not have an optimal solution!");
        }

        System.out.println("\nAdvanced usage:");
        System.out.println("Problem solved in " + mpSolver.wallTime() + " milliseconds");
        System.out.println("Problem solved in " + mpSolver.iterations() + " iterations");
    }
}
