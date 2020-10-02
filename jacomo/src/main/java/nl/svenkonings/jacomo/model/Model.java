package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.DuplicateNameException;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private final @NotNull List<Var> vars;
    private final @NotNull List<Constraint> constraints;

    public Model() {
        vars = new ArrayList<>();
        constraints = new ArrayList<>();
    }

    public @NotNull List<Var> getVars() {
        return Collections.unmodifiableList(vars);
    }

    public @NotNull List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public boolean containsVar(@NotNull String name) {
        return vars.stream().anyMatch(var -> var.getName().equals(name));
    }

    public void addVar(@NotNull Var var) throws DuplicateNameException {
        if (containsVar(var.getName())) {
            throw new DuplicateNameException("Var with name %s already exists", var.getName());
        }
        vars.add(var);
    }

    public void addConstraint(@NotNull Constraint constraint) {
        constraints.add(constraint);
    }

    public boolean removeVar(@NotNull String name) {
        return vars.removeIf(var -> var.getName().equals(name));
    }

    public boolean removeConstraint(@NotNull Constraint constraint) {
        return constraints.remove(constraint);
    }

    public <T> List<T> visitVars(@NotNull Visitor<T> visitor) {
        return vars.stream().map(visitor::visit).collect(Collectors.toList());
    }

    public <T> List<T> visitConstraints(@NotNull Visitor<T> visitor) {
        return constraints.stream().map(visitor::visit).collect(Collectors.toList());
    }

    public <T> List<T> visitAll(@NotNull Visitor<T> visitor) {
        List<T> result = visitVars(visitor);
        result.addAll(visitConstraints(visitor));
        return result;
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
