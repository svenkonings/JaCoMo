package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.solvers.Solver;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A constraint model containing a set of variables and constraints.
 * The model can be visited using a {@link Visitor} implementation
 * and solved using a {@link Solver} implementation.
 */
public class Model {
    private final @NotNull VarMap vars;
    private final @NotNull LinkedHashSet<Constraint> constraints;

    /**
     * Constructs an empty model
     */
    public Model() {
        vars = new VarMap();
        constraints = new LinkedHashSet<>();
    }

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
        return vars.listVars();
    }

    /**
     * Add the specified var to this model.
     *
     * @param var the var to be added
     * @return the previous var with the given name,
     * or {@code null} if there was none
     */
    public Var addVar(@NotNull Var var) {
        return vars.addVar(var);
    }

    /**
     * Add the specified collection of vars to this model.
     * All existing vars with the same names will be replaced.
     *
     * @param vars the specified collection of vars
     * @return the list of replaced vars
     */
    public List<Var> addVars(@NotNull Collection<? extends Var> vars) {
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
     * Returns the list of constraints of this model.
     *
     * @return the list of constraints of this model
     */
    public @NotNull List<Constraint> getConstraints() {
        return constraints.stream().collect(Collectors.toUnmodifiableList());
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
    public <T> List<T> visitAll(@NotNull Visitor<T> visitor) {
        List<T> results = visitVars(visitor);
        results.addAll(visitConstraints(visitor));
        return results;
    }

    @Override
    public String toString() {
        return String.format("Model(vars: %d, constraints: %d)", vars.size(), constraints.size());
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
