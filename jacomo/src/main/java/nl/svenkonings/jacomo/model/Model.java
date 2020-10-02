package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.constraints.Constraint;
import nl.svenkonings.jacomo.exceptions.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.NameNotFoundException;
import nl.svenkonings.jacomo.variables.Var;
import nl.svenkonings.jacomo.visitor.Visitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Model {
    private final @NotNull VarList vars;
    private final @NotNull List<Constraint> constraints;

    public Model() {
        vars = new VarList();
        constraints = new ArrayList<>();
    }

    public boolean containsVar(@NotNull String name) {
        return vars.containsVar(name);
    }

    public @Nullable Var getVar(@NotNull String name) {
        return vars.getVar(name);
    }

    public @NotNull List<Var> getVars() {
        return vars.getVars();
    }

    public void addVar(@NotNull Var var) throws DuplicateNameException {
        vars.addVar(var);
    }

    public boolean removeVar(@NotNull String name) {
        return vars.removeVar(name);
    }

    public void addConstraint(@NotNull Constraint constraint) {
        constraints.add(constraint);
    }

    public void updateVar(Var newVar) throws NameNotFoundException {
        vars.updateVar(newVar);
    }

    public @NotNull List<Constraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    public boolean removeConstraint(@NotNull Constraint constraint) {
        return constraints.remove(constraint);
    }

    public <T> List<T> visitVars(@NotNull Visitor<T> visitor) {
        return getVars().stream().map(visitor::visit).collect(Collectors.toList());
    }

    public <T> List<T> visitConstraints(@NotNull Visitor<T> visitor) {
        return getConstraints().stream().map(visitor::visit).collect(Collectors.toList());
    }

    public <T> List<T> visitAll(@NotNull Visitor<T> visitor) {
        List<T> result = visitVars(visitor);
        result.addAll(visitConstraints(visitor));
        return result;
    }

    @Override
    public String toString() {
        return String.format("Model(vars: %d, constraints: %d)", getVars().size(), getConstraints().size());
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
