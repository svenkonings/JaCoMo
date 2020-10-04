package nl.svenkonings.jacomo.solvers.ortools.cpsat;

import com.google.ortools.sat.Constraint;
import com.google.ortools.sat.IntVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents the return value of visited elements using the {@link CpSatVisitor}.
 * The return value can be empty, an {@link IntVar} or a {@link Constraint}.
 */
public class CpSatType {
    private final @Nullable IntVar intVar;
    private final @Nullable Constraint constraint;

    /**
     * Create an empty return value.
     *
     * @return the created return value
     */
    public static CpSatType none() {
        return new CpSatType(null, null);
    }

    /**
     * Create an {@link IntVar} return value.
     *
     * @param intVar the value to encapsulate
     * @return the created return value
     */
    public static CpSatType intVar(@NotNull IntVar intVar) {
        return new CpSatType(intVar, null);
    }

    /**
     * Create an {@link Constraint} return value.
     *
     * @param constraint the value to encapsulate
     * @return the created return value
     */
    public static CpSatType constraint(@NotNull Constraint constraint) {
        return new CpSatType(null, constraint);
    }

    private CpSatType(@Nullable IntVar intVar, @Nullable Constraint constraint) {
        this.intVar = intVar;
        this.constraint = constraint;
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
     * @return the inner {@link IntVar} if it exists, {@code null} otherwise.
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
     * @return the inner {@link Constraint} if it exists, {@code null} otherwise.
     */
    public @Nullable Constraint getConstraint() {
        return constraint;
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
        CpSatType cpSatType = (CpSatType) o;
        return Objects.equals(intVar, cpSatType.intVar) &&
                Objects.equals(constraint, cpSatType.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intVar, constraint);
    }
}
