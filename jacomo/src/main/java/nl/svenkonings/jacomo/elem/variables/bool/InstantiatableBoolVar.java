/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import nl.svenkonings.jacomo.exceptions.unchecked.ContradictionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represent a boolean variable which can be left undefined or instantiated later.
 */
public class InstantiatableBoolVar implements UpdatableBoolVar {
    private final @NotNull String name;
    private @Nullable Boolean value;

    /**
     * Creates a new undefined boolean variable with the specified name.
     *
     * @param name the specified name
     */
    public InstantiatableBoolVar(@NotNull String name) {
        this(name, null);
    }

    /**
     * Creates a new boolean variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     */
    public InstantiatableBoolVar(@NotNull String name, @Nullable Boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    public @Nullable Boolean getValue() {
        return value;
    }

    @Override
    public void instantiateValue(boolean value) throws ContradictionException {
        if (this.value != null && !this.value.equals(value)) {
            throw new ContradictionException("Value already instantiated");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return boolVarString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstantiatableBoolVar that = (InstantiatableBoolVar) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash("InstantiatableBoolVar", name, value);
    }
}
