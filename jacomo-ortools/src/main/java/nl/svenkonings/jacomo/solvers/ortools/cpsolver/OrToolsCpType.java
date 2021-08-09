/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package nl.svenkonings.jacomo.solvers.ortools.cpsolver;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.IntVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents the return value of visited elements using the {@link OrToolsCpVisitor}.
 * The return value can be empty, an {@link IntVar} or a {@link Constraint}.
 */
public class OrToolsCpType {
    private final @Nullable IntVar intVar;
    private final @Nullable Constraint constraint;
    private final @Nullable Supplier<Constraint> inverseSupplier;
    private @Nullable Constraint inverseConstraint;

    /**
     * Create an empty return value.
     *
     * @return the created return value
     */
    public static OrToolsCpType none() {
        return new OrToolsCpType(null, null, null);
    }

    /**
     * Create an {@link IntVar} return value.
     *
     * @param intVar the value to encapsulate
     * @return the created return value
     */
    public static OrToolsCpType intVar(@NotNull IntVar intVar) {
        return new OrToolsCpType(intVar, null, null);
    }

    /**
     * Create an {@link Constraint} return value.
     * A supplier to create the inverse constraint is required.
     *
     * @param constraint      the value to encapsulate
     * @param inverseSupplier the supplier to create the inverse constraint
     * @return the created return value
     */
    public static OrToolsCpType constraint(@NotNull Constraint constraint, @NotNull Supplier<Constraint> inverseSupplier) {
        return new OrToolsCpType(null, constraint, inverseSupplier);
    }

    private OrToolsCpType(@Nullable IntVar intVar, @Nullable Constraint constraint, @Nullable Supplier<Constraint> inverseSupplier) {
        assert constraint == null || inverseSupplier != null;
        this.intVar = intVar;
        this.constraint = constraint;
        this.inverseSupplier = inverseSupplier;
        this.inverseConstraint = null;
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
        if (isIntVar()) {
            return "ArExpression: " + intVar;
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
        OrToolsCpType orToolsCpType = (OrToolsCpType) o;
        return Objects.equals(intVar, orToolsCpType.intVar) &&
                Objects.equals(constraint, orToolsCpType.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intVar, constraint);
    }
}
