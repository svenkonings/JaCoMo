/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.Literal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents the return value of visited elements using the {@link OrToolsVisitor}.
 * The return value can be empty, an {@link IntVar} or a {@link Constraint}.
 */
public class OrToolsType {
    private final @Nullable Literal boolVar;
    private final @Nullable IntVar intVar;
    private final @Nullable Constraint constraint;
    private final @Nullable Supplier<Constraint> inverseSupplier;
    private @Nullable Constraint inverseConstraint;

    /**
     * Create an empty return value.
     *
     * @return the created return value
     */
    public static OrToolsType none() {
        return new OrToolsType(null, null, null, null);
    }

    /**
     * Create an {@link Literal} return value.
     *
     * @param boolVar the value to encapsulate
     * @return the created return value
     */
    public static OrToolsType boolVar(@NotNull Literal boolVar) {
        return new OrToolsType(boolVar, null, null, null);
    }

    /**
     * Create an {@link IntVar} return value.
     *
     * @param intVar the value to encapsulate
     * @return the created return value
     */
    public static OrToolsType intVar(@NotNull IntVar intVar) {
        return new OrToolsType(null, intVar, null, null);
    }

    /**
     * Create an {@link Constraint} return value.
     * A supplier to create the inverse constraint is required.
     *
     * @param constraint      the value to encapsulate
     * @param inverseSupplier the supplier to create the inverse constraint
     * @return the created return value
     */
    public static OrToolsType constraint(@NotNull Constraint constraint, @NotNull Supplier<Constraint> inverseSupplier) {
        return new OrToolsType(null, null, constraint, inverseSupplier);
    }

    private OrToolsType(@Nullable Literal boolVar, @Nullable IntVar intVar, @Nullable Constraint constraint, @Nullable Supplier<Constraint> inverseSupplier) {
        assert constraint == null || inverseSupplier != null;
        this.boolVar = boolVar;
        this.intVar = intVar;
        this.constraint = constraint;
        this.inverseSupplier = inverseSupplier;
        this.inverseConstraint = null;
    }

    /**
     * Returns whether this value is an {@link Literal}.
     *
     * @return {@code true} if this value is an {@link Literal}
     */
    public boolean isBoolVar() {
        return boolVar != null;
    }

    /**
     * Returns the inner {@link Literal}.
     *
     * @return the inner {@link Literal} if it exists, {@code null} otherwise
     */
    public @Nullable Literal getBoolVar() {
        return boolVar;
    }

    /**
     * Returns whether this value is an {@link IntVar}.
     *
     * @return {@code true} if this value is an {@link IntVar}
     */
    public boolean isIntVar() {
        return intVar != null;
    }

    /**
     * Returns the inner {@link IntVar}.
     *
     * @return the inner {@link IntVar} if it exists, {@code null} otherwise
     */
    public @Nullable IntVar getIntVar() {
        return intVar;
    }

    /**
     * Returns whether this value is an {@link Constraint}.
     *
     * @return {@code true} if this value is an {@link Constraint}
     */
    public boolean isConstraint() {
        return constraint != null;
    }

    /**
     * Returns the inner {@link Constraint}.
     *
     * @return the inner {@link Constraint} if it exists, {@code null} otherwise
     */
    public @Nullable Constraint getConstraint() {
        return constraint;
    }

    /**
     * Returns the inverse {@link Constraint}.
     * The first time this method is called the inverse constraint will be created
     * using the provided supplier.
     *
     * @return the inverse {@link Constraint} if it exists, {@code null} otherwise.
     */
    public @Nullable Constraint getInverseConstraint() {
        if (inverseConstraint != null) {
            return inverseConstraint;
        } else if (inverseSupplier != null) {
            inverseConstraint = inverseSupplier.get();
            return inverseConstraint;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (isBoolVar()) {
            return "Literal: " + boolVar;
        } else if (isIntVar()) {
            return "IntVar: " + intVar;
        } else if (isConstraint()) {
            return "Constraint: " + constraint;
        } else {
            return "None";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrToolsType orToolsType = (OrToolsType) o;
        return Objects.equals(boolVar, orToolsType.boolVar) &&
                Objects.equals(intVar, orToolsType.intVar) &&
                Objects.equals(constraint, orToolsType.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boolVar, intVar, constraint);
    }
}
