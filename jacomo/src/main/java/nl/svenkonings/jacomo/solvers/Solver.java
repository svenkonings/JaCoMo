/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers;

import nl.svenkonings.jacomo.elem.expressions.Expr;
import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.elem.variables.bool.BoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.UpdatableBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.IntVar;
import nl.svenkonings.jacomo.elem.variables.integer.UpdatableIntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.CheckException;
import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import nl.svenkonings.jacomo.model.Model;
import nl.svenkonings.jacomo.model.VarMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

/**
 * A solver used to solve {@link Model}s.
 */
@SuppressWarnings("ConstantConditions")
public interface Solver {
    /**
     * Check, optimize, attempt to solve and update the specified model.
     * Returns {@code true} if the model has been solved and updated.
     *
     * @param model the specified model
     * @return {@code true} if the model has been solved and updated,
     * {@code false} otherwise
     * @throws CheckException if one of the checks fails
     */
    default boolean solveAndUpdate(@NotNull Model model) throws CheckException {
        return solveAndUpdateUnchecked(model.check());
    }

    /**
     * Check, optimize and attempt to solve the specified model. Returns a
     * {@link VarMap} containing the resolved variables, or {@code null} if
     * the model couldn't be solved.
     * All variables present in the model should be included in the map.
     *
     * @param model the specified model
     * @return A {@link VarMap} containing the resolved variables, or
     * {@code null} if the model couldn't be solved
     * @throws CheckException if one of the checks fails
     */
    default @Nullable VarMap solve(@NotNull Model model) throws CheckException {
        return solveUnchecked(model.check());
    }

    /**
     * Attempt to solve and update the specified model. Returns {@code true}
     * if the model has been solved and updated. Does not check or optimize the
     * model.
     *
     * @param model the specified model
     * @return {@code true} if the model has been solved, {@code false} otherwise
     */
    default boolean solveAndUpdateUnchecked(@NotNull Model model) {
        VarMap result = solveUnchecked(model);
        if (result == null) {
            return false;
        }
        update(model, result);
        return true;
    }

    /**
     * Attempt to to solve the specified model. Returns a
     * {@link VarMap} containing the resolved variables, or {@code null} if
     * the model couldn't be solved. Does not check or optimize the
     * model. All variables present in the model should be included in the map.
     *
     * @param model the specified model
     * @return A {@link VarMap} containing the resolved variables, or
     * {@code null} if the model couldn't be solved
     * @throws CheckException if one of the checks fails
     */
    @Nullable VarMap solveUnchecked(@NotNull Model model);

    /**
     * Update the vars within the specified model with the results of the
     * specified map.
     *
     * @param model  the specified model
     * @param varMap the specified map
     * @throws CheckException         if the vars do not match or are unresolved
     * @throws ContradictionException if the values do not match
     */
    default void update(@NotNull Model model, @NotNull VarMap varMap) throws CheckException, ContradictionException {
        if (!model.getVarNames().equals(varMap.getVarNames())) {
            throw new CheckException("Var names within the model do not match var names within the map. Model: %s, VarMap: %s", model.getVarNames(), varMap.getVars());
        }
        if (!varMap.stream().allMatch(Expr::hasValue)) {
            throw new CheckException("Not all vars in the map have been resolved: %s", varMap.stream().filter(var -> !var.hasValue()).collect(Collectors.toList()));
        }
        for (Var mapVar : varMap) {
            String name = mapVar.getName();
            Var modelVar = model.getVar(name);
            if (modelVar instanceof UpdatableBoolVar) {
                if (!(mapVar instanceof BoolVar)) {
                    throw new CheckException("Expected boolean result for var %s, received: ", name, mapVar.getType());
                }
                ((UpdatableBoolVar) modelVar).instantiateValue(((BoolVar) mapVar).getValue());
            } else if (modelVar instanceof UpdatableIntVar) {
                if (!(mapVar instanceof IntVar)) {
                    throw new CheckException("Expected integer result for var %s, received: ", name, mapVar.getType());
                }
                ((UpdatableIntVar) modelVar).instantiateValue(((IntVar) mapVar).getValue());
            }
        }
        for (Var mapVar : varMap) {
            String name = mapVar.getName();
            Var modelVar = model.getVar(name);
            if (!mapVar.getValue().equals(modelVar.getValue())) {
                throw new ContradictionException("The original value (%s) for var %s does not match the solved value (%s)", modelVar.getValue(), name, mapVar.getValue());
            }
        }
    }
}
