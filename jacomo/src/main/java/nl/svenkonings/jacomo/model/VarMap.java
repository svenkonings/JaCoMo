/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.model;

import nl.svenkonings.jacomo.elem.variables.Var;
import nl.svenkonings.jacomo.util.ListUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An insertion-ordered map of {@link Var}s.
 * The vars are mapped based on their name.
 */
public class VarMap extends AbstractCollection<Var> {
    private final @NotNull LinkedHashMap<String, Var> vars;

    /**
     * Constructs an empty insertion-ordered {@code VarMap}.
     */
    public VarMap() {
        super();
        vars = new LinkedHashMap<>();
    }

    /**
     * Returns {@code true} if this map contains a var with the specified name,
     * {@code false} otherwise.
     *
     * @param name the specified name
     * @return {@code true} if this map contains a var with the specified name,
     * {@code false} otherwise
     */
    public boolean containsVar(@NotNull String name) {
        return vars.containsKey(name);
    }

    /**
     * Returns {@code true} if this map contains all vars with the specified names,
     * {@code false} otherwise.
     *
     * @param names the specified collection of names
     * @return {@code true} if this map contains all vars with the specified names,
     * {@code false} otherwise
     */
    public boolean containsVars(@NotNull Collection<String> names) {
        return names.stream().allMatch(this::containsVar);
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
        return vars.get(name);
    }

    /**
     * Returns an unmodifiable list view of the vars in this map.
     *
     * @return an unmodifiable list view of the vars in this map
     */
    public @NotNull List<Var> listVars() {
        return ListUtil.copyOf(vars.values());
    }

    /**
     * Add the specified var to this map.
     *
     * @param var the var to be added
     * @return the previous var with the given name,
     * or {@code null} if there was none
     */
    public Var addVar(@NotNull Var var) {
        return vars.put(var.getName(), var);
    }

    /**
     * Add the specified collection of vars to this map.
     * All existing vars with the same names will be replaced.
     *
     * @param vars the specified collection of vars
     * @return the list of replaced vars
     */
    public List<Var> addVars(@NotNull Collection<? extends Var> vars) {
        return vars.stream().map(this::addVar).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Remove the var with the specified name from this map.
     *
     * @param name the specified name
     * @return the removed var, or {@code null} if there was none
     */
    public Var removeVar(@NotNull String name) {
        return vars.remove(name);
    }

    /**
     * Remove the vars with the specified names from this map.
     *
     * @param names the specified collection of names
     * @return the list of removed vars
     */
    public List<Var> removeVars(@NotNull Collection<String> names) {
        return names.stream().map(this::removeVar).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean add(Var var) {
        addVar(var);
        return true;
    }

    @Override
    public @NotNull Iterator<Var> iterator() {
        return vars.values().iterator();
    }

    @Override
    public int size() {
        return vars.size();
    }

    @Override
    public String toString() {
        return vars.values().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarMap varMap = (VarMap) o;
        return Objects.equals(vars, varMap.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vars);
    }
}
