/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.mpsolver;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.DuplicateNameException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import nl.svenkonings.jacomo.solvers.Solver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class OrToolsMpSolver implements Solver {

    public OrToolsMpSolver() {
    }

    @Override
    public @Nullable VarMap solveUnchecked(@NotNull Model model) {
        OrToolsMpVisitor visitor = new OrToolsMpVisitor();
        model.visit(visitor);
        MPSolver solver = visitor.getSolver();
        MPObjective objective = solver.objective();
        Set<String> objectiveVars = new HashSet<>();
        for (MPVariable variable : visitor.getMinimizeVars()) {
            String name = variable.name();
            if (!objectiveVars.contains(name)) {
                objectiveVars.add(name);
                objective.setCoefficient(variable, 1.0);
            } else {
                throw new DuplicateNameException(name);
            }
        }
        for (MPVariable variable : visitor.getMaximizeVars()) {
            String name = variable.name();
            if (!objectiveVars.contains(name)) {
                objectiveVars.add(name);
                objective.setCoefficient(variable, -1.0);
            } else {
                throw new DuplicateNameException(name);
            }
        }
//        for (MPVariable variable : visitor.getRealVars().values()) {
//            String name = variable.name();
//            if (!objectiveVars.contains(name)) {
//                objectiveVars.add(name);
//                objective.setCoefficient(variable, 1.0);
//            }
//        }
        objective.setMinimization();
        MPSolver.ResultStatus resultStatus = solver.solve();
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            return null;
        }
        VarMap result = new VarMap();
        visitor.getBoolVars().forEach((name, var) -> {
            result.add(BoolVar.constant(name, var.solutionValue() == 1.0));
        });
        visitor.getRealVars().forEach((name, var) -> {
            result.add(IntVar.constant(name, (int) var.solutionValue()));
        });
        return result;
    }
}
