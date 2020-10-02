package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.exceptions.DuplicateNameException;
import nl.svenkonings.jacomo.exceptions.NameNotFoundException;
import nl.svenkonings.jacomo.variables.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VarList {
    protected final @NotNull List<Var> vars;

    public VarList() {
        vars = new ArrayList<>();
    }

    public boolean containsVar(@NotNull String name) {
        return vars.stream().anyMatch(var -> var.getName().equals(name));
    }

    public @Nullable Var getVar(@NotNull String name) {
        return vars.stream().filter(var -> var.getName().equals(name)).findAny().orElse(null);
    }

    public @NotNull List<Var> getVars() {
        return Collections.unmodifiableList(vars);
    }

    public void addVar(@NotNull Var var) throws DuplicateNameException {
        if (containsVar(var.getName())) {
            throw new DuplicateNameException("Var with name %s already exists", var.getName());
        }
        vars.add(var);
    }

    public boolean removeVar(@NotNull String name) {
        return vars.removeIf(var -> var.getName().equals(name));
    }

    public void updateVar(Var newVar) throws NameNotFoundException {
        if (!removeVar(newVar.getName())) {
            throw new NameNotFoundException("Var with name %s does not exist", newVar.getName());
        }
        addVar(newVar);
    }

    @Override
    public String toString() {
        return vars.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarList varList = (VarList) o;
        return Objects.equals(vars, varList.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vars);
    }
}
