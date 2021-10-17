/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.elem.Elem;
import nl.svenkonings.jacomo.elem.constraints.BoolExprConstraint;
import nl.svenkonings.jacomo.elem.constraints.Constraint;
import nl.svenkonings.jacomo.elem.expressions.bool.BoolExpr;
import nl.svenkonings.jacomo.elem.expressions.integer.IntExpr;
import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.elem.variables.bool.ConstantBoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.ExpressionBoolVar;
import nl.svenkonings.jacomo.elem.variables.bool.InstantiatableBoolVar;
import nl.svenkonings.jacomo.elem.variables.integer.BoundedIntVar;
import nl.svenkonings.jacomo.elem.variables.integer.ConstantIntVar;
import nl.svenkonings.jacomo.elem.variables.integer.ExpressionIntVar;
import nl.svenkonings.jacomo.exceptions.unchecked.ReservedNameException;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.util.ListUtil;
import nl.svenkonings.jacomo.visitor.ElemCopier;
import nl.svenkonings.jacomo.visitor.SimpleElemPrinter;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A constraint model containing a set of variables and constraints.
 * The model can be visited using a {@link Visitor} implementation
 * and solved using a {@link Solver} implementation.
 */
public class Model {
    private static final String GEN_CONST_PREFIX = "_const_";
    private static final String GEN_BOOL_PREFIX = "_bool_";
    private static final String GEN_INT_PREFIX = "_int_";

    private final @NotNull VarMap vars;
    private final @NotNull LinkedHashSet<Constraint> constraints;

    /**
     * Constructs an empty model
     */
    public Model() {
        vars = new VarMap();
        constraints = new LinkedHashSet<>();
    }

    // Variable methods

    /**
     * Returns {@code true} if this model contains a var with the specified name,
     * {@code false} otherwise.
     *
     * @param name the specified name
     * @return {@code true} if this model contains a var with the specified name,
     * {@code false} otherwise
     */
    public boolean containsVar(@NotNull String name) {
        return vars.containsVar(name);
    }

    /**
     * Returns {@code true} if this model contains all vars with the specified names,
     * {@code false} otherwise.
     *
     * @param names the specified collection of names
     * @return {@code true} if this model contains all vars with the specified names,
     * {@code false} otherwise
     */
    public boolean containsVars(@NotNull Collection<String> names) {
        return vars.containsVars(names);
    }

    /**
     * Returns the var with the specified name,
     * or {@code null} if there was none.
     *
     * @param name the specified name
     * @return the var with the specified name,
     * or {@code null} if there was none
     */
    public @Nullable Var getVar(@NotNull String name) {
        return vars.getVar(name);
    }

    /**
     * Returns an unmodifiable list view of the vars in this model.
     *
     * @return an unmodifiable list view of the vars in this model
     */
    public @NotNull List<Var> getVars() {
        return vars.getVars();
    }

    /**
     * Returns an unmodifiable set view of the var names in this model.
     *
     * @return an unmodifiable set view of the var names in this model
     */
    public @NotNull Set<String> getVarNames() {
        return vars.getVarNames();
    }

    /**
     * Returns a stream of the vars in this model.
     *
     * @return a stream of the vars in this model.
     */
    public @NotNull Stream<Var> varStream() {
        return vars.stream();
    }

    /**
     * Add the specified var to this model.
     *
     * @param var the var to be added
     * @return the previous var with the given name,
     * or {@code null} if there was none
     * @throws ReservedNameException if the variable name starts with underscore
     *                               (reserved for generated names)
     */
    public Var addVar(@NotNull Var var) throws ReservedNameException {
        if (var.getName().startsWith("_")) {
            throw new ReservedNameException(var);
        }
        return addVarUnchecked(var);
    }

    /**
     * Add the specified var to this model without checking for generated names.
     *
     * @param var the var to be added
     * @return the previous var with the given name,
     * or {@code null} if there was none
     */
    public Var addVarUnchecked(@NotNull Var var) {
        return vars.addVar(var);
    }

    /**
     * Add the specified collection of vars to this model.
     * All existing vars with the same names will be replaced.
     *
     * @param vars the specified collection of vars
     * @return the list of replaced vars
     * @throws ReservedNameException if one of the variable name starts with underscore
     *                               (reserved for generated names)
     */
    public List<Var> addVars(@NotNull Collection<? extends Var> vars) throws ReservedNameException {
        Optional<? extends Var> invalidVar = vars.stream().filter(var -> var.getName().startsWith("_")).findAny();
        if (invalidVar.isPresent()) {
            throw new ReservedNameException(invalidVar.get());
        }
        return addVarsUnchecked(vars);
    }

    /**
     * Add the specified collection of vars to this model without checking f00or generated names.
     * All existing vars with the same names will be replaced.
     *
     * @param vars the specified collection of vars
     * @return the list of replaced vars
     */
    public List<Var> addVarsUnchecked(@NotNull Collection<? extends Var> vars) {
        return this.vars.addVars(vars);
    }

    /**
     * Remove the var with the specified name from this model.
     *
     * @param name the specified name
     * @return the removed var, or {@code null} if there was none
     */
    public Var removeVar(@NotNull String name) {
        return vars.removeVar(name);
    }

    /**
     * Remove the vars with the specified names from this model.
     *
     * @param names the specified collection of names
     * @return the list of removed vars
     */
    public List<Var> removeVars(@NotNull Collection<String> names) {
        return vars.removeVars(names);
    }

    // Constraint methods

    /**
     * Returns {@code true} if this model contains the specified constraint,
     * {@code false} otherwise.
     *
     * @param constraint the specified constraint
     * @return {@code true} if this model contains the specified constraint,
     * {@code false} otherwise
     */
    public boolean containsConstraint(@NotNull Constraint constraint) {
        return constraints.contains(constraint);
    }

    /**
     * Returns {@code true} if this model contains all of the constraints in the specified collection,
     * {@code false} otherwise.
     *
     * @param constraints the specified collection of constraints
     * @return {@code true} if this model contains all of the constraints in the specified collection,
     * {@code false} otherwise
     */
    public boolean containsConstraints(@NotNull Collection<? extends Constraint> constraints) {
        return this.constraints.containsAll(constraints);
    }

    /**
     * Returns the list of constraints in this model.
     *
     * @return the list of constraints in this model
     */
    public @NotNull List<Constraint> getConstraints() {
        return ListUtil.copyOf(constraints);
    }

    /**
     * Returns a stream of the constrains in this model.
     *
     * @return a stream of the constrains in this model.
     */
    public @NotNull Stream<Constraint> constraintStream() {
        return constraints.stream();
    }

    /**
     * Add the specified constraint to this model.
     *
     * @param constraint the specified constraint
     * @return {@code true} if this model did not already contain the constraint,
     * {@code false} otherwise
     */
    public boolean addConstraint(@NotNull Constraint constraint) {
        return constraints.add(constraint);
    }

    /**
     * Add the specified collection of constraints to this model.
     *
     * @param constraints the specified collection of constraints
     * @return {@code true} if the set of constraint changed as a result of this call,
     * {@code false} otherwise
     */
    public boolean addConstraints(@NotNull Collection<? extends Constraint> constraints) {
        return this.constraints.addAll(constraints);
    }

    /**
     * Remove the specified constraint from this model.
     *
     * @param constraint the specified constraint
     * @return {@code true} if this model contained the specified constraint,
     * {@code false} otherwise
     */
    public boolean removeConstraint(@NotNull Constraint constraint) {
        return constraints.remove(constraint);
    }

    /**
     * Remove the specified collection of constraints from this model.
     *
     * @param constraints the specified collection of constraints
     * @return {@code true} if set of constraint changed as a result of this call,
     * {@code false} otherwise
     */
    public boolean removeConstraints(@NotNull Collection<? extends Constraint> constraints) {
        return this.constraints.removeAll(constraints);
    }

    // Element methods

    /**
     * Returns a stream of the elements in this model.
     * The variables come before the constraints.
     *
     * @return a stream of the elements in this model.
     */
    public @NotNull Stream<Elem> stream() {
        return Stream.concat(varStream(), constraintStream());
    }

    /**
     * Create a copy of this model.
     *
     * @return the copy.
     */
    public Model copy() {
        ElemCopier copier = new ElemCopier();
        Model model = new Model();
        varStream().map(copier::copy).forEachOrdered(model::addVarUnchecked);
        constraintStream().map(copier::copy).forEachOrdered(model::addConstraint);
        return model;
    }

    //Add to model methods

    private String genVarName(@NotNull String prefix) {
        int i = 0;
        String name = prefix + i;
        while (containsVar(name)) {
            i++;
            name = prefix + i;
        }
        return name;
    }

    /**
     * Create an uninstantiated boolean variable with a generated name
     * and add it to this model.
     *
     * @return the resulting variable
     */
    public InstantiatableBoolVar boolVar() {
        String name = genVarName(GEN_BOOL_PREFIX);
        InstantiatableBoolVar var = new InstantiatableBoolVar(name);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create an uninstantiated boolean variable with the specified name
     * and add it to this model.
     *
     * @param name the specified name
     * @return the resulting variable
     */
    public InstantiatableBoolVar boolVar(@NotNull String name) {
        InstantiatableBoolVar var = new InstantiatableBoolVar(name);
        addVar(var);
        return var;
    }

    /**
     * Create a boolean constant with the specified value and a generated name,
     * and add it to this model. If this model already contains the constant,
     * no constant is created and the existing constant is returned instead.
     *
     * @param value the specified value
     * @return the resulting constant
     */
    public ConstantBoolVar boolVar(boolean value) {
        String name = GEN_CONST_PREFIX + value;
        if (containsVar(name)) {
            return (ConstantBoolVar) getVar(name);
        } else {
            ConstantBoolVar var = new ConstantBoolVar(name, value);
            addVarUnchecked(var);
            return var;
        }
    }

    /**
     * Create a boolean variable with the specified name and value,
     * and add it to this model.
     *
     * @param name  the specified name
     * @param value the specified value
     * @return the resulting variable
     */
    public ConstantBoolVar boolVar(@NotNull String name, boolean value) {
        ConstantBoolVar var = new ConstantBoolVar(name, value);
        addVar(var);
        return var;
    }


    /**
     * Create a boolean variable with the specified expression and a generated name,
     * and add it to this model.
     *
     * @param expr the specified expression
     * @return the resulting variable
     */
    public ExpressionBoolVar boolVar(@NotNull BoolExpr expr) {
        String name = genVarName(GEN_BOOL_PREFIX);
        ExpressionBoolVar var = new ExpressionBoolVar(name, expr);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create a boolean variable with the specified name and expression,
     * and add it to this model.
     *
     * @param name the specified name
     * @param expr the specified expression
     * @return the resulting variable
     */
    public ExpressionBoolVar boolVar(@NotNull String name, @NotNull BoolExpr expr) {
        ExpressionBoolVar var = new ExpressionBoolVar(name, expr);
        addVar(var);
        return var;
    }

    /**
     * Create an uninstantiated integer variable with a generated name
     * and add it to this model.
     *
     * @return the resulting variable
     */
    public BoundedIntVar intVar() {
        String name = genVarName(GEN_INT_PREFIX);
        BoundedIntVar var = new BoundedIntVar(name);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create an uninstantiated integer variable with the specified name
     * and add it to this model.
     *
     * @param name the specified name
     * @return the resulting variable
     */
    public BoundedIntVar intVar(@NotNull String name) {
        BoundedIntVar var = new BoundedIntVar(name);
        addVar(var);
        return var;
    }

    /**
     * Create a integer constant with the specified value and a generated name,
     * and add it to this model. If this model already contains the constant,
     * no constant is created and the existing constant is returned instead.
     *
     * @param value the specified value
     * @return the resulting constant
     */
    public ConstantIntVar intVar(int value) {
        String name = GEN_CONST_PREFIX + value;
        if (containsVar(name)) {
            return (ConstantIntVar) getVar(name);
        } else {
            ConstantIntVar var = new ConstantIntVar(name, value);
            addVarUnchecked(var);
            return var;
        }
    }

    /**
     * Create a integer variable with the specified name and value,
     * and add it to this model.
     *
     * @param name  the specified name
     * @param value the specified value
     * @return the resulting variable
     */
    public ConstantIntVar intVar(@NotNull String name, int value) {
        ConstantIntVar var = new ConstantIntVar(name, value);
        addVar(var);
        return var;
    }

    /**
     * Create a integer variable with the specified expression and a generated name,
     * and add it to this model.
     *
     * @param expr the specified expression
     * @return the resulting variable
     */
    public ExpressionIntVar intVar(@NotNull IntExpr expr) {
        String name = genVarName(GEN_INT_PREFIX);
        ExpressionIntVar var = new ExpressionIntVar(name, expr);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create a integer variable with the specified name and expression,
     * and add it to this model.
     *
     * @param name the specified name
     * @param expr the specified expression
     * @return the resulting variable
     */
    public ExpressionIntVar intVar(@NotNull String name, @NotNull IntExpr expr) {
        ExpressionIntVar var = new ExpressionIntVar(name, expr);
        addVar(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified lower- and upper-bound
     * and a generated name, and add it to this model.
     *
     * @param lb the specified lower-bound
     * @param ub the specified upper-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVar(@Nullable Integer lb, @Nullable Integer ub) {
        String name = genVarName(GEN_INT_PREFIX);
        BoundedIntVar var = new BoundedIntVar(name, lb, ub);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified name, lower- and upper-bound,
     * and add it to this model.
     *
     * @param name the specified name
     * @param lb   the specified lower-bound
     * @param ub   the specified upper-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVar(@NotNull String name, @Nullable Integer lb, @Nullable Integer ub) {
        BoundedIntVar var = new BoundedIntVar(name, lb, ub);
        addVar(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified lower-bound
     * and a generated name, and add it to this model.
     *
     * @param lb the specified lower-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVarLb(@Nullable Integer lb) {
        String name = genVarName(GEN_INT_PREFIX);
        BoundedIntVar var = new BoundedIntVar(name, lb, null);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified name and lower-bound,
     * and add it to this model.
     *
     * @param name the specified name
     * @param lb   the specified lower-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVarLb(String name, @Nullable Integer lb) {
        BoundedIntVar var = new BoundedIntVar(name, lb, null);
        addVar(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified lower-bound
     * and a generated name, and add it to this model.
     *
     * @param ub the specified upper-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVarUb(@Nullable Integer ub) {
        String name = genVarName(GEN_INT_PREFIX);
        BoundedIntVar var = new BoundedIntVar(name, null, ub);
        addVarUnchecked(var);
        return var;
    }

    /**
     * Create a bounded integer variable with the specified name and upper-bound,
     * and add it to this model.
     *
     * @param name the specified name
     * @param ub   the specified upper-bound
     * @return the resulting variable
     */
    public BoundedIntVar intVarUb(String name, @Nullable Integer ub) {
        BoundedIntVar var = new BoundedIntVar(name, null, ub);
        addVar(var);
        return var;
    }

    /**
     * Create a new constraint with the specified boolean expression
     * and add it to this model.
     *
     * @param expr the specified expression
     * @return the resulting constraint
     */
    public Constraint constraint(@NotNull BoolExpr expr) {
        BoolExprConstraint constraint = new BoolExprConstraint(expr);
        addConstraint(constraint);
        return constraint;
    }

    // Visit methods

    /**
     * Visits all vars in this model with the specified visitor.
     *
     * @param visitor the specified visitor
     * @param <T>     the return type of the visitor
     * @return the list of results returned by the visitor
     */
    public <T> List<T> visitVars(@NotNull Visitor<T> visitor) {
        return vars.stream().map(visitor::visit).collect(Collectors.toList());
    }

    /**
     * Visits all constraints in this model with the specified visitor.
     *
     * @param visitor the specified visitor
     * @param <T>     the return type of the visitor
     * @return the list of results returned by the visitor
     */
    public <T> List<T> visitConstraints(@NotNull Visitor<T> visitor) {
        return constraints.stream().map(visitor::visit).collect(Collectors.toList());
    }

    /**
     * Visits all elements in this model with the specified visitor.
     *
     * @param visitor the specified visitor
     * @param <T>     the return type of the visitor
     * @return the list of results returned by the visitor
     */
    public <T> List<T> visit(@NotNull Visitor<T> visitor) {
        List<T> results = visitVars(visitor);
        results.addAll(visitConstraints(visitor));
        return results;
    }

    // General methods

    @Override
    public String toString() {
        return String.format("Model(vars: %d, constraints: %d)", vars.size(), constraints.size());
    }

    /**
     * Returns a string representation of this model,
     * including all vars and constraints in the model.
     *
     * @return the full string representation
     */
    public String toFullString() {
        SimpleElemPrinter printer = new SimpleElemPrinter();
        return "Model {\n" +
                "    vars {\n" +
                varStream().map(var -> "        " + printer.printVar(var)).distinct().collect(Collectors.joining("\n")) + "\n" +
                "    }\n" +
                "    constraints {\n" +
                constraintStream().map(constraint -> "        " + printer.printConstraint(constraint)).distinct().collect(Collectors.joining("\n")) + "\n" +
                "    }\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(vars, model.vars) &&
                Objects.equals(constraints, model.constraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vars, constraints);
    }
}
