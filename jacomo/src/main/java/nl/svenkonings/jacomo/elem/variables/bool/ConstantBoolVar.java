/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.elem.variables.bool;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a constant boolean variable.
 */
public class ConstantBoolVar implements BoolVar {
    private final @NotNull String name;
    private final boolean value;

    /**
     * Create a new constant boolean variable with the specified name and value.
     *
     * @param name  the specified name
     * @param value the specified value
     */
    public ConstantBoolVar(@NotNull String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public @NotNull Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return boolVarString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantBoolVar that = (ConstantBoolVar) o;
        return value == that.value &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash("ConstantBoolVar", name, value);
    }
}
