package nl.svenkonings.jacomo.solvers.chocosolver;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents the return value of visited elements using the {@link ChocoVisitor}.
 * The return value can be empty, an {@link ArExpression}, a {@link ReExpression}
 * or a {@link Constraint}.
 */
public class ChocoType {
    private final @Nullable ArExpression arExpression;
    private final @Nullable ReExpression reExpression;
    private final @Nullable Constraint constraint;

    /**
     * Create an empty return value.
     *
     * @return the created return value
     */
    public static ChocoType none() {
        return new ChocoType(null, null, null);
    }

    /**
     * Create an {@link ArExpression} return value.
     *
     * @param arExpression the value to encapsulate
     * @return the created return value
     */
    public static ChocoType arExpression(@NotNull ArExpression arExpression) {
        return new ChocoType(arExpression, null, null);
    }

    /**
     * Create an {@link ReExpression} return value.
     *
     * @param reExpression the value to encapsulate
     * @return the created return value
     */
    public static ChocoType reExpression(@NotNull ReExpression reExpression) {
        return new ChocoType(null, reExpression, null);
    }

    /**
     * Create an {@link Constraint} return value.
     *
     * @param constraint the value to encapsulate
     * @return the created return value
     */
    public static ChocoType constraint(@NotNull Constraint constraint) {
        return new ChocoType(null, null, constraint);
    }

    private ChocoType(@Nullable ArExpression arExpression, @Nullable ReExpression reExpression, @Nullable Constraint constraint) {
        this.arExpression = arExpression;
        this.reExpression = reExpression;
        this.constraint = constraint;
    }

    /**
     * Returns whether this value is an {@link ArExpression}.
     *
     * @return {@code true} if this value is an {@link ArExpression}
     */
    public boolean isArExpression() {
        return arExpression != null;
    }

    /**
     * Returns the inner {@link ArExpression}.
     *
     * @return the inner {@link ArExpression} if it exists, {@code null} otherwise.
     */
    public @Nullable ArExpression getArExpression() {
        return arExpression;
    }

    /**
     * Returns whether this value is an {@link ReExpression}.
     *
     * @return {@code true} if this value is an {@link ReExpression}
     */
    public boolean isReExpression() {
        return reExpression != null;
    }

    /**
     * Returns the inner {@link ReExpression}.
     *
     * @return the inner {@link ReExpression} if it exists, {@code null} otherwise.
     */
    public @Nullable ReExpression getReExpression() {
        return reExpression;
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
        if (isArExpression()) {
            return "ArExpression: " + arExpression;
        } else if (isReExpression()) {
            return "ReExpression: " + reExpression;
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
        ChocoType chocoType = (ChocoType) o;
        return Objects.equals(arExpression, chocoType.arExpression) &&
                Objects.equals(reExpression, chocoType.reExpression) &&
                Objects.equals(constraint, chocoType.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arExpression, reExpression, constraint);
    }
}
